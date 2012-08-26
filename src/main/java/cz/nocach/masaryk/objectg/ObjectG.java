package cz.nocach.masaryk.objectg;

import cz.nocach.masaryk.objectg.gen.CompositeGenerator;
import cz.nocach.masaryk.objectg.gen.Generator;
import cz.nocach.masaryk.objectg.gen.PrimitiveUniqueGenerator;

/**
 * User: __nocach
 * Date: 26.8.12
 */
public class ObjectG {
    private Generator globalUniqueGenerator = new CompositeGenerator(
            new PrimitiveUniqueGenerator()
    );
    public static <T> T unique(Class<T> clazz){
        //TODO: implement
        return null;
    }
}
