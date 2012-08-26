package cz.nocach.masaryk.objectg.gen;

/**
 * User: __nocach
 * Date: 26.8.12
 */
public class CompositeGenerator implements Generator{
    private Generator[] generators;
    public CompositeGenerator(Generator... generators){
        this.generators = generators;
    }

    @Override
    public Object generate(Class type) {
        for (Generator each : generators){
            if (each.supportsType(type)){
                return each.generate(type);
            }
        }
        throw new IllegalArgumentException("can't generate value for type " + type);
    }

    @Override
    public boolean supportsType(Class type) {
        for (Generator each : generators){
            if (each.supportsType(type)) return true;
        }
        return false;
    }
}
