package cz.nocach.masaryk.objectg.gen.jpa;

/**
 * <p>
 *     Represents relation between two entities.
 * </p>
 * <p>
 * User: __nocach
 * Date: 13.10.12
 * </p>
 */
public class Relation {

    private Class owner;
    private String ownerProperty;
    private Class target;
    private String targetProperty;

    public Relation(Class owner, String ownerProperty, Class target, String targetProperty) {
        this.owner = owner;
        this.ownerProperty = ownerProperty;
        this.target = target;
        this.targetProperty = targetProperty;
    }

    public Class getOwner() {
        return owner;
    }

    public void setOwner(Class owner) {
        this.owner = owner;
    }

    public String getOwnerProperty() {
        return ownerProperty;
    }

    public void setOwnerProperty(String ownerProperty) {
        this.ownerProperty = ownerProperty;
    }

    public Class getTarget() {
        return target;
    }

    public void setTarget(Class target) {
        this.target = target;
    }

    public String getTargetProperty() {
        return targetProperty;
    }

    public void setTargetProperty(String targetProperty) {
        this.targetProperty = targetProperty;
    }
}
