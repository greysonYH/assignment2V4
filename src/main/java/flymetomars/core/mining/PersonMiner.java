package flymetomars.core.mining;

import com.google.common.collect.Lists;
import flymetomars.data.ExpertiseDAO;
import flymetomars.data.InvitationDAO;
import flymetomars.data.MissionDAO;
import flymetomars.data.PersonDAO;
import flymetomars.model.Equipment;
import flymetomars.model.Expertise;
import flymetomars.model.Mission;
import flymetomars.model.Person;
import jdk.nashorn.internal.ir.WhileNode;

import java.util.*;

/**
 * Created by greyson on 22/4/17.
 */
public class PersonMiner {
    private PersonDAO personDAO;
    private MissionDAO missionDAO;
    private ExpertiseDAO expertiseDAO;
    private InvitationDAO invitationDAO;

    public PersonMiner(PersonDAO personDAO, MissionDAO missionDAO, ExpertiseDAO expertiseDAO, InvitationDAO invitationDAO) {
        this.personDAO = personDAO;
        this.missionDAO = missionDAO;
        this.expertiseDAO = expertiseDAO;
        this.invitationDAO = invitationDAO;
    }

    /**
     * Return the top-k persons with the most demanded expertises (required by missions).
     */
    public List<Person> getHotshots(int k) {
        if(k <= 0){
            throw new IllegalArgumentException("The number of K must at least 1");
        }
        Set<Person> personSet = personDAO.getAllPerson();
        if (personSet.isEmpty()) { return Collections.emptyList(); }
        Set<Mission> missionSet = missionDAO.getAllMissions();
        if (missionSet.isEmpty()) { return Collections.emptyList(); }
        List<Person> personList = new ArrayList<>(personSet);
        List<Mission> missionList = new ArrayList<>(missionSet);
        List<Person> personWithExpertiseList = new ArrayList<>();
        String mostDemandedExpertise = getMostDemandedExpertise(missionList);
        Iterator<Person> iterator = personList.iterator();
        while (iterator.hasNext()) {
            Person person = iterator.next();
            Iterator<Expertise> iterator1 = person.getExpertise().iterator();
            while (iterator1.hasNext()) {
                Expertise expertise = iterator1.next();
                if (expertise.getDescription().equals(mostDemandedExpertise)) {
                    personWithExpertiseList.add(person);
                    break;
                }
            }
        }
        return personWithExpertiseList;
    }

    public String getMostDemandedExpertise(List<Mission> missionList) {
        Iterator<Mission> iterator = missionList.iterator();
        Map<String, Integer> demandedExpertise = new HashMap<>();
        while(iterator.hasNext()){
            Mission m = iterator.next();
            Iterator<Expertise> iterator1 = m.getExpertiseRequired().iterator();
            while (iterator1.hasNext()){
                Expertise expertise = iterator1.next();
                if (demandedExpertise.containsKey(expertise.getDescription())) {
                    int val = demandedExpertise.get(expertise.getDescription());
                    demandedExpertise.put(expertise.getDescription(), val + 1);
                }
                else demandedExpertise.put(expertise.getDescription(), 1);
            }
        }
        Map.Entry<String, Integer> maxMap = demandedExpertise.entrySet().stream()
                .max(Map.Entry.comparingByValue(Integer::compareTo)).get();
        return maxMap.getKey();
    }

    public List<Mission> getTwoMostNonInteractionMissions () {
        int size1 = 0;
        int size2 = 0;
        Set<Mission> missionSet = missionDAO.getAllMissions();
        List<Mission> missionList = new ArrayList<>(missionSet);
        List<Mission> emptyMissionList = new ArrayList<>();
        if(missionList.size()<2) {
            return Collections.emptyList();
        }
        Set<Person> participantIntersection = new HashSet<Person>();
        Iterator<Mission> iterator =  missionList.iterator();
        while (iterator.hasNext()) {
            Mission m1 = iterator.next();
            Iterator<Mission> iterator1 = missionList.iterator();
            while (iterator1.hasNext()) {
                Mission m2 = iterator1.next();
                if (!m1.equals(m2)) {
                    participantIntersection.addAll(m1.getParticipantSet());
                    participantIntersection.retainAll(m2.getParticipantSet());
                    if (participantIntersection.isEmpty()) {
                        if (m1.getParticipantSet().size() + m2.getParticipantSet().size() > size1 + size2) {
                            emptyMissionList.clear();
                            emptyMissionList.add(m1);
                            emptyMissionList.add(m2);
                            size1 = m1.getParticipantSet().size();
                            size2 = m2.getParticipantSet().size();
                        }
                    }
                }
            }
        }
        return emptyMissionList;
    }

    public List<Person> getPowerBrokers() {
        int size1 = 0;
        List<Mission> missionList = getTwoMostNonInteractionMissions();
        if(missionList.isEmpty()) {
            return Collections.emptyList();
        }
        Mission m1 = missionList.get(0);
        Mission m2 = missionList.get(1);
        Set<Person> participant1 = m1.getParticipantSet();
        Set<Person> participant2 = m2.getParticipantSet();
        Set<Mission> missionsIntersection = new HashSet<>();
        List<Person> powerBrokers = new ArrayList<>();
        Iterator<Person> iterator = participant1.iterator();
        while (iterator.hasNext()) {
            Person person1 = iterator.next();
            Set<Mission> missions1 = person1.getMissionRegistered();
            Iterator<Person> iterator1 = participant2.iterator();
            while (iterator1.hasNext()) {
                Person person2 = iterator1.next();
                Set<Mission> missions2 = person2.getMissionRegistered();
                missionsIntersection.addAll(missions1);
                missionsIntersection.retainAll(missions2);
                if (!missionsIntersection.isEmpty()) {
                    if (missionsIntersection.size() > size1) {
                        size1 = missionsIntersection.size();
                        powerBrokers.add(person1);
                        powerBrokers.add(person2);
                    }
                }
            }
        }
        return powerBrokers;
    }

    public Set<Person> getConnectionPerson(Person person) {
        if(person == null)
            throw new IllegalArgumentException("Person cannot be null");
        Set<Person> connectionPerson = new HashSet<>();
        Iterator<Mission> iterator = person.getMissionRegistered().iterator();
        while (iterator.hasNext()) {
            Mission mission = iterator.next();
            connectionPerson.addAll(mission.getParticipantSet());
        }
        connectionPerson.remove(person);
        return connectionPerson;
    }

    public Set<Person> getNerFronties(Person person, int k, int m) {
        if(person == null)
            throw new IllegalArgumentException("Person cannot be null");
        if(k <= 0)
            throw new IllegalArgumentException("k cannot less or equal than zero");
        if(m <= 0)
            throw new IllegalArgumentException("m cannot less or equal than zero");
        Set<Person> personSet = personDAO.getAllPerson();
        List<Person> personList = new ArrayList<>(personSet);
        Set<Person> newFrontiersSet = new HashSet<>();
        if(personList.size()<2) {
            return Collections.emptySet();
        }

        Set<Person> connectedPerson = new HashSet<>();
        Set<Person> emptyPerson = new HashSet<>();
        connectedPerson.addAll(getConnectionPerson(person));

        for(int i = 1; i < k; i++) {
            Set<Person> connectedKPerson = new HashSet<>();
            Iterator<Person> iterator = connectedPerson.iterator();
            while (iterator.hasNext()) {
                Person person1 = iterator.next();
                connectedKPerson = getConnectionPerson(person1);
                emptyPerson.addAll(connectedKPerson);
            }
            connectedPerson.addAll(emptyPerson);
        }

        personList.remove(person);
        personList.removeAll(connectedPerson);

        if (m <= personList.size()) {
            m = m;
        } else {
            m = personList.size();
        }
        for (int i = 0; i < m; i++) {
            newFrontiersSet.add(personList.get(i));
        }
        return newFrontiersSet;
    }

    public Set<Person> getRostering(Set<Expertise> expertiseSet) {
        if(expertiseSet == null)
            throw new IllegalArgumentException("expertise role cannot be null");
        int size0 = expertiseSet.size();
        Set<Person> personSet = personDAO.getAllPerson();
        Set<Person> rosteringSet = new HashSet<>();
        List<Expertise> expertiseTemp = new ArrayList<>(expertiseSet);
        List<Expertise> expertiseTemp1 = new ArrayList<>(expertiseSet);
        for (Person p :personSet) {
            expertiseTemp.retainAll(p.getExpertise());
            if (!expertiseTemp.isEmpty()) {
                if (expertiseTemp.size() == size0) {
                    rosteringSet.add(p);
                    return rosteringSet;
                }
                expertiseTemp1.removeAll(expertiseTemp);
                expertiseTemp.clear();
                expertiseTemp.addAll(expertiseTemp1);
                rosteringSet.add(p);
                size0 = expertiseTemp.size();
            } else {
                expertiseTemp.addAll(expertiseTemp1);
            }
        }
        if (expertiseTemp.size() != size0) {
            System.out.print("can not fulfill all expertise");
        }
        return  rosteringSet;
    }
}
