package cz.nocach.masaryk.objectg.gen;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * User: __nocach
 * Date: 30.9.12
 */
class Hierarchy {
    private List<Class> hierarchy = new ArrayList<Class>();

    public void push(Class clazz) {
        hierarchy.add(clazz);
    }

    public boolean isCycle() {
        for (int outerIndex = 0; outerIndex < hierarchy.size(); outerIndex++){
            for (int innerIndex = 0; innerIndex < hierarchy.size(); innerIndex++){
                if (outerIndex == innerIndex) continue;
                if (hierarchy.get(outerIndex).equals(hierarchy.get(innerIndex))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void pop() {
        hierarchy.remove(hierarchy.size() - 1);
    }

    public int size() {
        return hierarchy.size();
    }
}
