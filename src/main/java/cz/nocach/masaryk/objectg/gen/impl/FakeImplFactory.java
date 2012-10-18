package cz.nocach.masaryk.objectg.gen.impl;

import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.beans.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * User: __nocach
 * Date: 18.10.12
 */
class FakeImplFactory {
    private Logger logger = LoggerFactory.getLogger(FakeImplFactory.class);
    public <T> T create(Class<T> interfaze) {
        try {
            Assert.notNull(interfaze);
            Assert.isTrue(interfaze.isInterface(), "must be interface");
            ClassPool classPool = ClassPool.getDefault();
            final Class fakeImplementationClass;
            try{
                return (T)Class.forName(getFakeImplementationName(interfaze)).newInstance();
            } catch (ClassNotFoundException e) {
                CtClass originalInterface = classPool.get(interfaze.getName());
                CtClass fakeImplementationCtClass = classPool.makeClass(getFakeImplementationName(interfaze));

                addBeanMethods(interfaze, classPool, fakeImplementationCtClass);
                addNotBeanMethodsStub(interfaze, classPool, fakeImplementationCtClass);

                fakeImplementationCtClass.addInterface(originalInterface);
                fakeImplementationClass = fakeImplementationCtClass.toClass();
                //free our memory
                fakeImplementationCtClass.detach();
                return (T) fakeImplementationClass.newInstance();
            }
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        } catch (CannotCompileException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> void addNotBeanMethodsStub(Class<T> interfaze, ClassPool classPool, CtClass fakeImplementationCtClass) throws NotFoundException, CannotCompileException {
        Set<Method> methodsToExclude = getMethodsToExclude(interfaze);
        for (Method each : interfaze.getMethods()){
            if (methodsToExclude.contains(each)) continue;
            CtClass returnType = classPool.get(each.getReturnType().getName());
            CtClass[] parameters = toCtClassArray(classPool, each.getParameterTypes());
            CtMethod stubMethod = new CtMethod(returnType, each.getName(), parameters, fakeImplementationCtClass);
            stubMethod.setBody(null);
            fakeImplementationCtClass.addMethod(stubMethod);
        }
    }

    private CtClass[] toCtClassArray(ClassPool classPool, Class<?>[] parameterTypes) throws NotFoundException {
        CtClass[] result = new CtClass[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++){
            result[i] = classPool.get(parameterTypes[i].getName());
        }
        return result;
    }

    private <T> Set<Method> getMethodsToExclude(Class<T> interfaze) {
        Set<Method> methodsToExclude = new HashSet<Method>();
        for (Class eachInterface : getAllInterfaces(interfaze)){
            try{
                addMethodsToExlude(methodsToExclude, eachInterface);
            }
            catch (IntrospectionException e) {
                logger.error("could not extract methods from " + eachInterface, e);
            }
        }
        return methodsToExclude;
    }

    private void addMethodsToExlude(Set<Method> methodsToExclude, Class fromInterface) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(fromInterface);
        for(PropertyDescriptor each : beanInfo.getPropertyDescriptors()){
            if (each.getReadMethod() != null) methodsToExclude.add(each.getReadMethod());
            if (each.getWriteMethod() != null) methodsToExclude.add(each.getWriteMethod());
        }
    }

    private <T> void addBeanMethods(Class<T> interfaze, ClassPool classPool, CtClass fakeImplementationCtClass) throws NotFoundException, CannotCompileException {
        for (Class eachInterface : getAllInterfaces(interfaze)){
            try {
                Set<String> doneProperties = new HashSet<String>();
                addFieldsFromInterface(classPool, fakeImplementationCtClass, eachInterface, doneProperties);
            } catch (IntrospectionException e) {
                logger.error("could not extract properties from " + eachInterface, e);
            }
        }
    }

    private <T> Class<?>[] getAllInterfaces(Class<T> interfaze) {
        Class<?>[] result = Arrays.copyOf(interfaze.getInterfaces(), interfaze.getInterfaces().length + 1);
        result[interfaze.getInterfaces().length] = interfaze;
        return result;
    }

    private void addFieldsFromInterface(ClassPool classPool, CtClass fakeImplementationCtClass, Class eachInterface, Set<String> doneProperties) throws IntrospectionException, NotFoundException, CannotCompileException {
        BeanInfo beanInfo = Introspector.getBeanInfo(eachInterface);
        for (PropertyDescriptor each : beanInfo.getPropertyDescriptors()){
            if (doneProperties.contains(each.getName())) continue;
            CtField propertyField = addPropertyField(classPool, fakeImplementationCtClass, each);
            addPropertyGetter(fakeImplementationCtClass, each, propertyField);
            addPropertySetter(fakeImplementationCtClass, each, propertyField);
            doneProperties.add(each.getName());
        }
    }

    private void addPropertySetter(CtClass fakeImplementationCtClass, PropertyDescriptor each, CtField propertyField) throws CannotCompileException {
        if (each.getWriteMethod() != null){
            CtMethod fieldSetter = CtNewMethod.setter(each.getWriteMethod().getName(), propertyField);
            fakeImplementationCtClass.addMethod(fieldSetter);
        }
    }

    private void addPropertyGetter(CtClass fakeImplementationCtClass, PropertyDescriptor each, CtField propertyField) throws CannotCompileException {
        if (each.getReadMethod() != null){
            CtMethod fieldGetter = CtNewMethod.getter(each.getReadMethod().getName(), propertyField);
            fakeImplementationCtClass.addMethod(fieldGetter);
        }
    }

    private CtField addPropertyField(ClassPool classPool, CtClass fakeImplementationCtClass, PropertyDescriptor each) throws NotFoundException, CannotCompileException {
        CtClass propertyType = classPool.get(each.getPropertyType().getName());
        CtField propertyField = new CtField(propertyType, each.getName(), fakeImplementationCtClass);
        fakeImplementationCtClass.addField(propertyField);
        return propertyField;
    }

    private <T> String getFakeImplementationName(Class<T> interfaze) {
        return "objectgFakeInterface" + interfaze.getName();
    }
}
