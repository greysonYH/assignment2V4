package flymetomars.core.check;

import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import flymetomars.model.Equipment;
import flymetomars.model.Expertise;
import flymetomars.model.Mission;
import flymetomars.model.Person;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Yuan-Fang Li
 * @version $Id: $
 */
public class Validator {

    /**
     * Needs to validate the following.
     *
     * 1. A mission needs to have a time, a name, a duration, a location, a description,
     *    as well as a non-empty maxAttributes field variable.
     * 2. A mission needs satisfy its equipment requirements. For this purpose, two pieces
     *    of equipments are considered the same if they have the same name.
     * 3. The sum of a particular attribute value of all equipments in a mission cannot exceed
     *    the maximally allowed value of that attribute of the mission.
     * 4. A mission needs to satisfy all its required expertise to be viable.
     *
     * @param mission
     * @return
     * @throws ValidationException
     */

    public Mission finalise(Mission mission) throws ValidationException {
        Set<ValidationError> validationErrors = Sets.newHashSet();
        String attribute = "";
        String message = "";
        String exception = "";

        if (!checkMissionFieldVariable(mission))
        {
            validationErrors.add(new ValidationError(
                "Null or Empty",
                "variable can not be empty or null"));
        }
        if (!checkRequiredEquipmentSame(mission)) {
            validationErrors.add(new ValidationError(
                    "RequiredEquipmentSame",
                    "Their are more than one equipments's name same"));
        }

        if (!checkRequiredEquipment(mission)) {
            validationErrors.add(new ValidationError(
                    "RequiredEquipment",
                    "not all required equipments satisfied"));
        }

        if (!checkRequiredExpertise(mission)) {
            validationErrors.add(new ValidationError(
                    "RequiredExpertise",
                    "not all required expertise satisfied"));
        }

        if (!checkExceedMaximumEquipmentValue(mission)) {
            validationErrors.add(new ValidationError(
                    "Maximum Exceed",
                    "exceed maximum equipment value"));
        }

        if (validationErrors.isEmpty()) {
            mission.setStatus(Mission.Status.finalised);
            return mission;
        } else {
            Iterator<ValidationError> iterator = validationErrors.iterator();
            while(iterator.hasNext()){
                ValidationError validationError = iterator.next();
                attribute = validationError.getAttribute();
                message = validationError.getMessage();
                exception = message;
            }
            throw new ValidationException(exception, validationErrors);
        }
    }

    private boolean checkMissionFieldVariable(Mission mission) {
        if (mission.getTime() == null || mission.getTime().equals("")) {return false;}
        if (mission.getDuration() == 0){return false;}
        if (mission.getLocation() == null || mission.getLocation().equals("")){return false;}
        if (mission.getDescription() == null || mission.getDescription().equals("")){return false;}
        if (mission.getMaxAttributes().isEmpty()){return false;}
        return true;
    }

    private boolean checkRequiredEquipment(Mission mission) {
        Set<String> participantEquipment = new HashSet<>();
        Set<String> requiredEquipment = new HashSet<>();

        Iterator<Equipment> iterator1 = mission.getEquipmentsRequired().iterator();
        while(iterator1.hasNext()){
            Equipment eq = iterator1.next();
            String name = eq.getName();
            requiredEquipment.add(name);
        }

        Set<Person> participantSet = mission.getParticipantSet();
        Iterator<Person> iterator2 = participantSet.iterator();
        while(iterator2.hasNext()){
            Person p = iterator2.next();
            Iterator<Equipment> iterator3 = p.getEquipmentOwned().iterator();
            while (iterator3.hasNext()){
                Equipment eq = iterator3.next();
                participantEquipment.add(eq.getName());
            }
        }
        if (!requiredEquipment.equals(participantEquipment)) { return false;}
        return true;
    }

    private boolean checkExceedMaximumEquipmentValue(Mission mission) {
        Map<Equipment.Attribute, Integer> maxAttributes = mission.getMaxAttributes();
        Map<Equipment.Attribute, Integer> participantAttributes = new HashMap<>();

        Set<Person> participantSet = mission.getParticipantSet();
        Iterator<Person> iterator = participantSet.iterator();
        while(iterator.hasNext()){
            Person p = iterator.next();
            Iterator<Equipment> iterator1 = p.getEquipmentOwned().iterator();
            while (iterator1.hasNext()){
                Equipment eq = iterator1.next();

                eq.getAttributes().forEach((k, v) -> participantAttributes.merge(k, v, Integer::sum));
            }
        }
        participantAttributes.forEach((k,v) -> maxAttributes.merge(k,v, Math::subtractExact));
        int volume = maxAttributes.get(Equipment.Attribute.volume);
        int weight = maxAttributes.get(Equipment.Attribute.weight);
        int cost = maxAttributes.get(Equipment.Attribute.cost);
        if(volume < 0 && weight < 0 && cost < 0){return false;}
        return true;
    }

    private boolean checkRequiredEquipmentSame(Mission mission) {
        Set<String> equipmentNewSet = new HashSet<>();
        int length1 = mission.getEquipmentsRequired().size();
        Iterator<Equipment> iterator = mission.getEquipmentsRequired().iterator();
        while(iterator.hasNext()){
            Equipment eq = iterator.next();
            String name = eq.getName();
            equipmentNewSet.add(name);
        }
        int length2 = equipmentNewSet.size();
        if (length1 > length2) { return false;}
        return true;
    }

    private boolean checkRequiredExpertise(Mission mission) {
        Set<String> participantExpertise = new HashSet<>();
        Set<String> requiredExpertise = new HashSet<>();

        Iterator<Expertise> iterator1 = mission.getExpertiseRequired().iterator();
        while(iterator1.hasNext()){
            Expertise exp = iterator1.next();
            String description = exp.getDescription();
            requiredExpertise.add(description);
        }

        Set<Person> participantSet = mission.getParticipantSet();
        Iterator<Person> iterator2 = participantSet.iterator();
        while(iterator2.hasNext()){
            Person p = iterator2.next();
            Iterator<Expertise> iterator3 = p.getExpertise().iterator();
            while (iterator3.hasNext()){
                Expertise exp = iterator3.next();
                participantExpertise.add(exp.getDescription());
            }
        }
        if (!requiredExpertise.equals(participantExpertise)) { return false;}
        return true;
    }



    public void checkForNullEmpty(Object obj) {
        if (null == obj) {
            throw new IllegalArgumentException("variables cannot be null.");
        }
        if (obj.toString().trim().equals("")) {
            throw new IllegalArgumentException("variables cannot be empty.");
        }
    }

    public void checkEmailFormat(String email) {
        String patternString = ".+@.+\\..+";
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        boolean matches = matcher.matches();
        if (matches == false) {
            throw new IllegalArgumentException(" illegal email format.");
        }
    }

    public void checkPassword(String password) {
        if (password.trim().length() <= 9 || password.trim().length() >= 13) {
            throw new IllegalArgumentException(" password length must between 10-12 characters. and no empty");
        }
    }

    public void checkNumber(int number) {
        if (number < 0) {
            throw new IllegalArgumentException(" this number can not be negative");
        }
    }
}
