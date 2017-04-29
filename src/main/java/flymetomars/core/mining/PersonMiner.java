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
        //most expertise
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
        System.out.print(personWithExpertiseList.get(0).getEmail());
        return personWithExpertiseList;
    }

    public String getMostDemandedExpertise(List<Mission> missionList) {
        Iterator<Mission> iterator = missionList.iterator();
        Map<String, Integer> demandedExpertise = new HashMap<>();
        //int sum = 0;
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
        //for (String key : demandedExpertise.keySet()) {
          //  demandedExpertise.merge(key, 0,Integer::sum);
        //}
        int max = Collections.max(demandedExpertise.values());

        Map.Entry<String, Integer> maxMap = demandedExpertise.entrySet().stream()
                .max(Map.Entry.comparingByValue(Integer::compareTo)).get();
        return maxMap.getKey();
        ///Comparator.comparing(Map.Entry::getValue)
        //System.out.print(maxMap);
    }
}
