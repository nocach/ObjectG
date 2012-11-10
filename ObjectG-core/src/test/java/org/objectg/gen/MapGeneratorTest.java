package org.objectg.gen;

import org.junit.Test;
import org.objectg.ObjectG;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * User: __nocach
 * Date: 23.9.12
 */
public class MapGeneratorTest {

    @Test
    public void canGenerateConcreteImplMap(){
        ClassWithMaps classWithMaps = ObjectG.unique(ClassWithMaps.class);

        HashMap<String, String> hashMapString = classWithMaps.getHashMapString();
        assertNotNull("map must be set", hashMapString);
        Set<String> hashMapKeySet = hashMapString.keySet();
        assertEquals("one key must be set", 1, hashMapKeySet.size());
        String firstKeyValue = hashMapKeySet.iterator().next();
        assertNotNull("key value must not be null", firstKeyValue);
        assertNotNull("value must not be null", hashMapString.get(firstKeyValue));
    }

    public class ClassWithMaps{
        private Map<String, String> mapString;
        private HashMap<String, String> hashMapString;

        public Map<String, String> getMapString() {
            return mapString;
        }

        public void setMapString(Map<String, String> mapString) {
            this.mapString = mapString;
        }

        public HashMap<String, String> getHashMapString() {
            return hashMapString;
        }

        public void setHashMapString(HashMap<String, String> hashMapString) {
            this.hashMapString = hashMapString;
        }
    }
}
