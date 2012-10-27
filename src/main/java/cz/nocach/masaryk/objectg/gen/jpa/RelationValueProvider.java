package cz.nocach.masaryk.objectg.gen.jpa;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: __nocach
 * Date: 12.10.12
 */
public class RelationValueProvider {
    private Map<Class, List<Relation>> relationsForClass = new ConcurrentHashMap<Class, List<Relation>>();
    private RelationInspector relationInspector;

    public RelationValueProvider(){
        relationInspector = new RelationInspector();
    }

    public List<Relation> getRelationsFor(Class clazz){
        if (!relationInspector.isEntity(clazz)) return Collections.emptyList();
        if (!relationsForClass.containsKey(clazz)){
            Map<Class, List<Relation>> relations = relationInspector.fromEntity(clazz);
            relationsForClass.putAll(relations);
        }
        return relationsForClass.get(clazz);
    }
}
