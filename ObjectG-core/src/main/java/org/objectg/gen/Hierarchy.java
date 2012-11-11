package org.objectg.gen;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     allows to store information about generated hierarchy
 * </p>
 * <p>
 * User: __nocach
 * Date: 30.9.12
 * </p>
 */
class Hierarchy {
    private List<Class> hierarchyClass = new ArrayList<Class>();
    private List<Object> hierarchyObject = new ArrayList<Object>();

    public void push(Class clazz) {
        push(clazz, null);
    }
    public void push(Class clazz, Object object) {
        hierarchyClass.add(clazz);
        hierarchyObject.add(object);
    }

    public boolean isCycle(Class clazz) {
        return hierarchyClass.contains(clazz);
    }

    public void pop() {
        hierarchyClass.remove(hierarchyClass.size() - 1);
        hierarchyObject.remove(hierarchyObject.size() - 1);
    }

    public int size() {
        return hierarchyClass.size();
    }

    /**
     *
     * @param clazz of what class to pick object
     * @return the most recent object of type class or null if no such object
     */
    public Object pick(Class clazz) {
        for (int i = hierarchyClass.size() - 1; i >=0; i--){
            if (hierarchyClass.get(i).isAssignableFrom(clazz)){
                return hierarchyObject.get(i);
            }
        }
        return null;
    }

    public String dump() {
        StringBuilder dump = new StringBuilder();
        for (int i = 0; i < hierarchyClass.size(); i++){
            int paddingSize = i * 4;
            for (int j = 0; j < paddingSize; j++) dump.append(' ');
            dump.append("class="+hierarchyClass.get(i).getSimpleName());
            Integer objectHashCode = hierarchyObject.get(i) != null ? hierarchyObject.hashCode() : null;
            dump.append(", object.hashCode="+ objectHashCode);
            dump.append("\n");
        }
        return dump.toString();
    }

    public boolean isEmpty() {
        return hierarchyClass.isEmpty();
    }

	public Object getRoot() {
		return hierarchyObject.get(0);
	}

	public int getCycleDepth(final Class clazz) {
		int result = 0;
		for (Class each : hierarchyClass){
			if (each.equals(clazz)) result++;
		}
		return result;
	}
}
