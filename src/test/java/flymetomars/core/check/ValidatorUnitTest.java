package flymetomars.core.check;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import flymetomars.model.Equipment;
import flymetomars.model.Expertise;
import flymetomars.model.Mission;
import flymetomars.model.Person;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

/**
 * @author Yuan-Fang Li
 * @version $Id: $
 */
public class ValidatorUnitTest {

    private Validator validator;

    @Before
    public void setUp() {
        validator = new Validator();
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testFinalisedMissionWithEmptyVariable() throws ValidationException {
        expectedEx.expect(ValidationException.class);
        expectedEx.expectMessage("variable can not be empty or null");
        Mission mission = new Mission();
        mission.setTime(null);
        mission.setName("");
        mission.setDuration(0);
        mission.setLocation("");

        Map<Equipment.Attribute, Integer> attributes = new HashMap<>();
        attributes.put(Equipment.Attribute.weight, 999);
        attributes.put(Equipment.Attribute.volume, 999);
        attributes.put(Equipment.Attribute.cost, 999);
        mission.setMaxAttributes(attributes);
        validator.finalise(mission);
    }

    @Test
    public void testFinalisedMissionWithNonEmptyNotNullVariable() throws ValidationException {
        Mission mission = new Mission();
        mission.setTime(new Date());
        mission.setName("initial");
        mission.setDuration(2);
        mission.setLocation("melbourne");
        mission.setDescription("latest event");
        Equipment e1 = new Equipment("hamer1", 2, 2, 2);
        mission.setMaxAttributes(e1.getAttributes());
        validator.finalise(mission);
    }


    @Test
    public void testFinalisedMissionWithTwoSameNameEquipment() throws ValidationException{
        expectedEx.expect(ValidationException.class);
        expectedEx.expectMessage("Their are more than one equipments's name same");
        Mission mission = new Mission();
        mission.setTime(new Date());
        mission.setName("initial");
        mission.setDuration(2);
        mission.setLocation("melbourne");
        mission.setDescription("latest event");
        Equipment e1 = new Equipment("hammer1", 2, 2, 2);
        Equipment e2 = new Equipment("hammer1", 3, 3, 3);
        Person p1 = new Person("p1@abc.com");
        p1.setEquipmentOwned(Sets.newHashSet(e1,e2));
        mission.setParticipantSet(Sets.newHashSet(p1));

        Map<Equipment.Attribute, Integer> attributes = new HashMap<>();
        attributes.put(Equipment.Attribute.weight, 999);
        attributes.put(Equipment.Attribute.volume, 999);
        attributes.put(Equipment.Attribute.cost, 999);
        mission.setMaxAttributes(attributes);

        mission.getEquipmentsRequired().add(e1);
        mission.getEquipmentsRequired().add(e2);
        validator.finalise(mission);
    }

    @Test
    public void testFinalisedMissionWithTwoDifferentNameEquipment() throws ValidationException{
        Mission mission = new Mission();
        mission.setTime(new Date());
        mission.setName("initial");
        mission.setDuration(2);
        mission.setLocation("melbourne");
        mission.setDescription("latest event");
        Equipment e1 = new Equipment("hammer1", 2, 2, 2);
        Equipment e2 = new Equipment("hammer2", 2, 2, 2);
        Person p1 = new Person("p1@abc.com");
        p1.setEquipmentOwned(Sets.newHashSet(e1,e2));
        mission.setParticipantSet(Sets.newHashSet(p1));

        Map<Equipment.Attribute, Integer> attributes = new HashMap<>();
        attributes.put(Equipment.Attribute.weight, 999);
        attributes.put(Equipment.Attribute.volume, 999);
        attributes.put(Equipment.Attribute.cost, 999);
        mission.setMaxAttributes(attributes);

        mission.getEquipmentsRequired().add(e1);
        mission.getEquipmentsRequired().add(e2);
        validator.finalise(mission);
    }

    @Test
    public void testFinalisedMissionCanNotSatisfyEquipmentRequirement() throws ValidationException{
        expectedEx.expect(ValidationException.class);
        expectedEx.expectMessage("not all required equipments satisfied");

        Equipment eq1 = new Equipment("hammer1", 2, 2, 2);
        Equipment eq2 = new Equipment("hammer2", 2, 2, 2);
        Equipment eq3 = new Equipment("hammer3", 2, 2, 2);

        Person p1 = new Person("p1@abc.com");
        Person p2 = new Person("p2@abc.com");

        p1.setEquipmentOwned(Sets.newHashSet(eq1,eq2));
        p2.setEquipmentOwned(Sets.newHashSet(eq2));

        Mission mission = new Mission();
        mission.setTime(new Date());
        mission.setName("initial");
        mission.setDuration(2);
        mission.setLocation("melbourne");
        mission.setDescription("latest event");

        Multiset multiset = HashMultiset.create();
        multiset.add(eq1);
        multiset.add(eq2);
        multiset.add(eq3);
        mission.setEquipmentsRequired(multiset);

        Map<Equipment.Attribute, Integer> attributes = new HashMap<>();
        attributes.put(Equipment.Attribute.weight, 999);
        attributes.put(Equipment.Attribute.volume, 999);
        attributes.put(Equipment.Attribute.cost, 999);
        mission.setMaxAttributes(attributes);

        mission.setParticipantSet(Sets.newHashSet(p1,p2));
        validator.finalise(mission);
    }

    @Test
    public void testFinalisedMissionSatisfyEquipmentRequirement() throws ValidationException{
        Equipment eq1 = new Equipment("hammer1", 2, 2, 2);
        Equipment eq2 = new Equipment("hammer2", 2, 2, 2);
        Equipment eq3 = new Equipment("hammer3", 2, 2, 2);

        Person p1 = new Person("p1@abc.com");
        Person p2 = new Person("p2@abc.com");

        p1.setEquipmentOwned(Sets.newHashSet(eq1,eq2));
        p2.setEquipmentOwned(Sets.newHashSet(eq2,eq3));

        Mission mission = new Mission();
        mission.setTime(new Date());
        mission.setName("initial");
        mission.setDuration(2);
        mission.setLocation("melbourne");
        mission.setDescription("latest event");

        Multiset multiset = HashMultiset.create();
        multiset.add(eq1);
        multiset.add(eq2);
        multiset.add(eq3);
        mission.setEquipmentsRequired(multiset);

        Map<Equipment.Attribute, Integer> attributes = new HashMap<>();
        attributes.put(Equipment.Attribute.weight, 999);
        attributes.put(Equipment.Attribute.volume, 999);
        attributes.put(Equipment.Attribute.cost, 999);
        mission.setMaxAttributes(attributes);

        mission.setParticipantSet(Sets.newHashSet(p1,p2));
        validator.finalise(mission);
    }

    @Test
    public void missionEquipmentNotExceedMaximumValue() throws ValidationException{

        Equipment eq1 = new Equipment("hammer1", 2, 2, 2);
        Equipment eq2 = new Equipment("hammer2", 3, 3, 3);
        Equipment eq3 = new Equipment("hammer3", 4, 4, 4);

        Person p1 = new Person("p1@abc.com");
        Person p2 = new Person("p2@abc.com");
        Person p3 = new Person("p3@abc.com");

        p1.setEquipmentOwned(Sets.newHashSet(eq1, eq2));
        p2.setEquipmentOwned(Sets.newHashSet(eq2, eq3));
        p3.setEquipmentOwned(Sets.newHashSet(eq3, eq1));


        Mission mission = new Mission();
        mission.setTime(new Date());
        mission.setName("initial");
        mission.setDuration(2);
        mission.setLocation("melbourne");
        mission.setDescription("latest event");

        Multiset multiset = HashMultiset.create();
        multiset.add(eq1);
        multiset.add(eq2);
        multiset.add(eq3);
        mission.setEquipmentsRequired(multiset);

        Map<Equipment.Attribute, Integer> attributes = new HashMap<>();
        attributes.put(Equipment.Attribute.weight, 18);
        attributes.put(Equipment.Attribute.volume, 18);
        attributes.put(Equipment.Attribute.cost, 18);
        mission.setMaxAttributes(attributes);

        mission.setParticipantSet(Sets.newHashSet(p1, p2,p3));
        validator.finalise(mission);
    }

    @Test
    public void missionEquipmentExceedMaximumValue() throws ValidationException{
        expectedEx.expect(ValidationException.class);
        expectedEx.expectMessage("exceed maximum equipment value");

        Equipment eq1 = new Equipment("hammer1", 2, 2, 2);
        Equipment eq2 = new Equipment("hammer2", 3, 3, 3);
        Equipment eq3 = new Equipment("hammer3", 4, 4, 4);

        Person p1 = new Person("p1@abc.com");
        Person p2 = new Person("p2@abc.com");

        p1.setEquipmentOwned(Sets.newHashSet(eq1, eq2));
        p2.setEquipmentOwned(Sets.newHashSet(eq2, eq3));

        Mission mission = new Mission();
        mission.setTime(new Date());
        mission.setName("initial");
        mission.setDuration(2);
        mission.setLocation("melbourne");
        mission.setDescription("latest event");

        Multiset multiset = HashMultiset.create();
        multiset.add(eq1);
        multiset.add(eq2);
        multiset.add(eq3);
        mission.setEquipmentsRequired(multiset);

        Map<Equipment.Attribute, Integer> attributes = new HashMap<>();
        attributes.put(Equipment.Attribute.weight, 3);
        attributes.put(Equipment.Attribute.volume, 3);
        attributes.put(Equipment.Attribute.cost, 3);
        mission.setMaxAttributes(attributes);

        mission.setParticipantSet(Sets.newHashSet(p1, p2));
        validator.finalise(mission);
    }


    @Test
    public void missionNeedsToSatisfyRequiredExpertiseFail() throws ValidationException{
        expectedEx.expect(ValidationException.class);
        expectedEx.expectMessage("not all required expertise satisfied");

        Expertise e1 = new Expertise("navigation");
        Expertise e2 = new Expertise("cooking");
        Expertise e3 = new Expertise("IT");
        Expertise e4 = new Expertise("medical");

        Person p1 = new Person("p1@abc.com");
        Person p2 = new Person("p2@abc.com");

        p1.addExpertise(e1);
        p2.addExpertise(e2, e3);

        Mission mission = new Mission();
        mission.setTime(new Date());
        mission.setName("initial");
        mission.setDuration(2);
        mission.setLocation("melbourne");
        mission.setDescription("latest event");
        Equipment eq1 = new Equipment("hammer1", 2, 2, 2);
        mission.setMaxAttributes(eq1.getAttributes());
        mission.setParticipantSet(Sets.newHashSet(p1,p2));
        mission.setExpertiseRequired(Sets.newHashSet(e1, e2, e3, e4));
        validator.finalise(mission);
    }

    @Test
    public void missionSatisfyRequiredExpertise() throws ValidationException{
        Expertise e1 = new Expertise("navigation");
        Expertise e2 = new Expertise("cooking");
        Expertise e3 = new Expertise("IT");
        Expertise e4 = new Expertise("medical");

        Person p1 = new Person("p1@abc.com");
        Person p2 = new Person("p2@abc.com");
        Person p3 = new Person("p3@abc.com");//////////

        p1.addExpertise(e1);
        p2.addExpertise(e2, e3);
        p3.addExpertise(e4);/////////

        Mission mission = new Mission();
        mission.setTime(new Date());
        mission.setName("initial");
        mission.setDuration(2);
        mission.setLocation("melbourne");
        mission.setDescription("latest event");
        Equipment eq1 = new Equipment("hammer1", 2, 2, 2);
        mission.setMaxAttributes(eq1.getAttributes());
        mission.setParticipantSet(Sets.newHashSet(p1,p2,p3));/////////
        mission.setExpertiseRequired(Sets.newHashSet(e1, e2, e3, e4));
        validator.finalise(mission);
    }
}