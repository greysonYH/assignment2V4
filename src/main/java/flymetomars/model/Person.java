package flymetomars.model;

import com.google.common.base.Objects;
import flymetomars.core.check.Validator;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by yli on 10/03/15.
 */
public class Person extends SeriablizableEntity {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private Set<Expertise> expertise;
    private Set<Equipment> equipmentOwned;
    private Set<Mission> missionRegistered;
    private Set<Invitation> invitationsReceived;

    private Validator validator;

    public Person() {
        this.firstName = "";////
        this.lastName = "";
        this.email = "";
        this.password = "";

        expertise = new HashSet<>();
        missionRegistered = new HashSet<>();
        invitationsReceived = new HashSet<>();
        equipmentOwned = new HashSet<>();
        validator = new Validator();
    }

    public Person(String email) {
        this();
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        validator.checkForNullEmpty(firstName);
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        validator.checkForNullEmpty(lastName);
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        validator.checkForNullEmpty(password);
        validator.checkPassword(password);
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        validator.checkForNullEmpty(email);
        validator.checkEmailFormat(email);
        this.email = email;
    }

    public Set<Mission> getMissionRegistered() {
        return missionRegistered;
    }

    public void setMissionRegistered(Set<Mission> missionRegistered) {
        this.missionRegistered = missionRegistered;
    }

    public Set<Invitation> getInvitationsReceived() {
        return invitationsReceived;
    }

    public void setInvitationsReceived(Set<Invitation> invitationsReceived) {
        this.invitationsReceived = invitationsReceived;
    }

    public Set<Expertise> getExpertise() {
        return expertise;
    }

    public void setExpertise(Set<Expertise> expertise) {
        this.expertise = expertise;
    }

    public void addExpertise(Expertise... experties) {
        if (null == experties) {
            throw new IllegalArgumentException("Expertise cannot be null.");
        }
        for (Expertise exp : experties) {
            if (null == exp.getDescription()) {
                throw new IllegalArgumentException("Expertise cannot have null description.");
            } else if (exp.getDescription().trim().isEmpty()) {
                throw new IllegalArgumentException("Expertise cannot have empty description.");
            }
            expertise.add(exp);
        }
    }

    public Set<Equipment> getEquipmentOwned() {
        return equipmentOwned;
    }

    public void setEquipmentOwned(Set<Equipment> equipmentOwned) {
        this.equipmentOwned = equipmentOwned;
    }

    @Override
    public boolean equals(Object o) {
        Person person = (Person) o;
        checkPersonAttributesNotNull(this);
        checkPersonAttributesNotNull(person);
        //if (!this.firstName.equals(person.firstName)){return false;}
        //if (!this.lastName.equals(person.lastName)){return false;}
        if (!this.email.equals(person.email)){return false;}///
        if (!this.password.equals(person.password)){return false;}///
        return true;
    }

    private boolean checkPersonAttributesNotNull(Person person) {
        if (person.firstName == null || person.lastName == null ||
                person.email == null || person.password == null)
        {
            throw new IllegalArgumentException("variables cannot be null.");
        }
        return true;
    }

    @Override
    public int hashCode() {
        checkPersonAttributesNotNull(this);
        return Objects.hashCode(email);
    }
}
