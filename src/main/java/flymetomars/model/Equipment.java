package flymetomars.model;

import com.google.common.base.Objects;
import flymetomars.core.check.Validator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yuan-Fang Li
 * @version $Id: $
 */
public class Equipment {

    private String name;
    private Map<Attribute, Integer> attributes;
    private Validator validator;

    public enum Attribute {
        weight, volume, cost
    }
    public void setAttributes(Map<Attribute, Integer> attributes) {
        this.attributes = attributes;
    }

    public Map<Attribute, Integer> getAttributes() {
        return attributes;
    }

    public Equipment() {///////
        name = "";
        attributes = new HashMap<>();
        attributes.put(Attribute.weight, 0);
        attributes.put(Attribute.volume, 0);
        attributes.put(Attribute.cost, 0);
        validator = new Validator();
    }

    public Equipment(String name, int weight, int volume, int cost) {
        this.name = name;
        this.attributes = new HashMap<>();
        attributes.put(Attribute.weight, weight);
        attributes.put(Attribute.volume, volume);
        attributes.put(Attribute.cost, cost);
        validator = new Validator();
        validator.checkNumber(weight);
        validator.checkNumber(volume);
        validator.checkNumber(cost);
        validator.checkForNullEmpty(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipment equipment = (Equipment) o;
        return Objects.equal(name, equipment.name) &&
                Objects.equal(attributes, equipment.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, attributes);
    }

}
