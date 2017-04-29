package flymetomars.model;

import com.google.common.base.Objects;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by yli on 10/03/15.
 */
public class Mission extends SeriablizableEntity {
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        preliminary, finalised
    }

    private Date time;
    private int budget;
    private String name;
    private int duration;
    private Status status;
    private Person captain;
    private String location;
    private String description;

    private Set<Person> participantSet;
    private Set<Invitation> invitationSet;
    private Set<Expertise> expertiseRequired;
    private Multiset<Equipment> equipmentsRequired;

    private Map<Equipment.Attribute, Integer> maxAttributes;


    public Mission(Date time, int budget, String name, int duration, Status status, Person captain, String location, String description, Set<Person> participantSet, Set<Invitation> invitationSet, Set<Expertise> expertiseRequired, Multiset<Equipment> equipmentsRequired, Map<Equipment.Attribute, Integer> maxAttributes) {
        this.time = time;
        this.budget = budget;
        this.name = name;
        this.duration = duration;
        this.status = status;
        this.captain = captain;
        this.location = location;
        this.description = description;
        this.participantSet = participantSet;
        this.invitationSet = invitationSet;
        this.expertiseRequired = expertiseRequired;
        this.equipmentsRequired = equipmentsRequired;
        this.maxAttributes = maxAttributes;
    }

    public Mission() {
        time = null;
        budget = 0;
        name = null;
        duration = 0;
        captain = null;
        location = null;
        description = null;

        status = Status.preliminary;
        invitationSet = new HashSet<>();
        participantSet = new HashSet<>();
        expertiseRequired = new HashSet<>();
        maxAttributes = new HashMap<>();
        equipmentsRequired = HashMultiset.create();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Invitation> getInvitationSet() {
        return invitationSet;
    }

    public void setInvitationSet(Set<Invitation> invitationSet) {
        this.invitationSet = invitationSet;
    }

    public Set<Person> getParticipantSet() {
        return participantSet;
    }

    public void setParticipantSet(Set<Person> participantSet) {
        this.participantSet = participantSet;
    }

    public Person getCaptain() {
        return captain;
    }

    public void setCaptain(Person captain) {
        this.captain = captain;
    }

    public Multiset<Equipment> getEquipmentsRequired() {
        return equipmentsRequired;
    }

    public void setEquipmentsRequired(Multiset<Equipment> equipmentsRequired) {
        this.equipmentsRequired = equipmentsRequired;
    }

    public Set<Expertise> getExpertiseRequired() {
        return expertiseRequired;
    }

    public void setExpertiseRequired(Set<Expertise> expertiseRequired) {
        this.expertiseRequired = expertiseRequired;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Map<Equipment.Attribute, Integer> getMaxAttributes() {
        return maxAttributes;
    }

    public void setMaxAttributes(Map<Equipment.Attribute, Integer> maxAttributes) {
        this.maxAttributes = maxAttributes;
    }

    @Override
    public boolean equals(Object o) {
        Mission mission = (Mission) o;
        checkMissionAttributesNotNull(this);
        checkMissionAttributesNotNull(mission);

        if (!this.time.equals(mission.time)){return false;}
        if (this.budget != mission.budget){return false;}
        if (!this.name.equals(mission.name)){return false;}
        if (this.duration != mission.duration){return false;}
        if (!this.status.equals(mission.status)){return false;}
        if (!this.captain.equals(mission.captain)){return false;}///
        if (!this.location.equals(mission.location)){return false;}
        if (!this.description.equals(mission.description)){return false;}
        return true;
    }

    private boolean checkMissionAttributesNotNull(Mission mission) {
        if (mission.time == null || mission.name == null || mission.status == null || mission.captain == null ||
                mission.location == null || mission.description == null)
        {
            throw new IllegalArgumentException("variables cannot be null.");
        }
        return true;
    }
    @Override
    public int hashCode() {
        checkMissionAttributesNotNull(this);
        return Objects.hashCode(time, name, duration, location, captain, description, maxAttributes);
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }
}
