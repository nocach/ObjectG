package org.objectg.gen.impl;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.objectg.BaseObjectGTest;
import org.objectg.fixtures.IMethods;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * User: __nocach
 * Date: 18.10.12
 */
public class FakeInterfaceFactoryTest extends BaseObjectGTest {

    private FakeInterfaceFactory factory;

    @Before
    public void setup(){
        factory = new FakeInterfaceFactory();
    }

    @Test
    public void canCreateEmptyInterface(){
        EmptyInterface emptyInterface = factory.create(EmptyInterface.class);

        assertNotNull(emptyInterface);
    }

    @Test
    public void canCreateEmptyInterfaceTwice(){
        factory.create(EmptyInterface.class);
        factory.create(EmptyInterface.class);
    }

    @Test
    public void canCreateFunctionalBean(){
        BeanPropertiesInterface beanPropertiesInterface = factory.create(BeanPropertiesInterface.class);
        Object objectPropertyValue = new Object();
        beanPropertiesInterface.setObjectProperty(objectPropertyValue);

        assertEquals(objectPropertyValue, beanPropertiesInterface.getObjectProperty());
    }

    @Test
    public void canCreateNotBeanInterface() throws IOException {
        NotBeanPropertiesInterface notBeanPropertiesInterface = factory.create(NotBeanPropertiesInterface.class);

        notBeanPropertiesInterface.booleanReturningMethod();
        notBeanPropertiesInterface.booleanObjectReturningMethod();
        notBeanPropertiesInterface.byteReturningMethod();
        notBeanPropertiesInterface.byteObjectReturningMethod();
        notBeanPropertiesInterface.shortReturningMethod();
        notBeanPropertiesInterface.shortObjectReturningMethod();
        notBeanPropertiesInterface.charReturningMethod();
        notBeanPropertiesInterface.charObjectReturningMethod();
        notBeanPropertiesInterface.intReturningMethod();
        notBeanPropertiesInterface.integerReturningMethod();
        notBeanPropertiesInterface.longReturningMethod();
        notBeanPropertiesInterface.longObjectReturningMethod();
        notBeanPropertiesInterface.floatReturningMethod();
        notBeanPropertiesInterface.floatObjectReturningMethod();
        notBeanPropertiesInterface.doubleReturningMethod();
        notBeanPropertiesInterface.doubleObjectReturningMethod();
        notBeanPropertiesInterface.objectReturningMethod();
        notBeanPropertiesInterface.objectReturningMethodNoArgs();
        notBeanPropertiesInterface.oneArg(true);
        notBeanPropertiesInterface.oneArg(new Boolean(true));
        notBeanPropertiesInterface.forBoolean(null);
        notBeanPropertiesInterface.oneArg(1);
        notBeanPropertiesInterface.oneArg(new Byte("1"));
        notBeanPropertiesInterface.forByte(new Byte("1"));
        notBeanPropertiesInterface.oneArg(1);
        notBeanPropertiesInterface.oneArg(new Short("1"));
        notBeanPropertiesInterface.forShort(new Short("1"));
        notBeanPropertiesInterface.oneArg('1');
        notBeanPropertiesInterface.oneArg(new Character('1'));
        notBeanPropertiesInterface.forCharacter(new Character('1'));
        notBeanPropertiesInterface.oneArg(1);
        notBeanPropertiesInterface.oneArg(Integer.valueOf(1));
        notBeanPropertiesInterface.forInteger(Integer.valueOf(1));
        notBeanPropertiesInterface.oneArg(1l);
        notBeanPropertiesInterface.oneArg(Long.valueOf(1));
        notBeanPropertiesInterface.forLong(Long.valueOf(1));
        notBeanPropertiesInterface.oneArg(1f);
        notBeanPropertiesInterface.oneArg(new Float(1f));
        notBeanPropertiesInterface.forFloat(new Float(1f));
        notBeanPropertiesInterface.oneArg(1d);
        notBeanPropertiesInterface.oneArg(new Double(1d));
        notBeanPropertiesInterface.forDouble(new Double(1d));
        notBeanPropertiesInterface.oneArg(new Object());
        notBeanPropertiesInterface.oneArg("str");
        notBeanPropertiesInterface.throwsNothing(true);
        notBeanPropertiesInterface.throwsIOException(1);
        notBeanPropertiesInterface.throwsError(1);
        notBeanPropertiesInterface.simpleMethod();
        notBeanPropertiesInterface.differentMethod();
        notBeanPropertiesInterface.differentMethod("str");
        notBeanPropertiesInterface.otherMethod();
        notBeanPropertiesInterface.simpleMethod("str");
        notBeanPropertiesInterface.simpleMethod(new ArrayList());
        notBeanPropertiesInterface.simpleMethod(new Object());
        notBeanPropertiesInterface.simpleMethod(1);
        notBeanPropertiesInterface.simpleMethod("str", Integer.valueOf(1));
        notBeanPropertiesInterface.simpleMethod("one", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1));
        notBeanPropertiesInterface.simpleMethod("one", new String[]{});
        notBeanPropertiesInterface.threeArgumentMethod(1, new Object(), "str");
    }

    @Test
    public void canInstantiateBeanInterface(){
        BeanInterface beanInterface = factory.create(BeanInterface.class);

        Object objectProperty = new Object();
        beanInterface.setObjectProperty(objectProperty);
        //setter works
        assertEquals(objectProperty, beanInterface.getObjectProperty());

        //can invoke method
        beanInterface.oneArg(1);
    }

	@Test
	public void willWorkWithOverriddenMethods(){
		final SamePropertyDerived samePropertyDerived = factory.create(SamePropertyDerived.class);
	}

    public static interface EmptyInterface{
    }

    public static interface BeanPropertiesInterface {
        public void setObjectProperty(Object object);
        public Object getObjectProperty();
    }

    public static interface NotBeanPropertiesInterface extends IMethods{
    }

    public static interface BeanInterface extends BeanPropertiesInterface, NotBeanPropertiesInterface {
    }

	public static interface SamePropertyBase{
		public Long getId();
		public void setId(Long id);
	}

	public static interface SamePropertyDerived extends SamePropertyBase{
		@Override
		Long getId();
		@Override
		void setId(Long id);
	}
}
