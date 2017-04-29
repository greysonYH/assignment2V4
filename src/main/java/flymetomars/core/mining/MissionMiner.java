package flymetomars.core.mining;

import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import flymetomars.data.ExpertiseDAO;
import flymetomars.data.InvitationDAO;
import flymetomars.data.MissionDAO;
import flymetomars.data.PersonDAO;
import flymetomars.model.Equipment;
import flymetomars.model.Expertise;
import flymetomars.model.Mission;
import sun.nio.cs.ext.MS874;

import java.util.*;

/**
 * Created by greyson on 22/4/17.
 */
public class MissionMiner {
    private PersonDAO personDAO;
    private MissionDAO missionDAO;
    private ExpertiseDAO expertiseDAO;
    private InvitationDAO invitationDAO;

    public MissionMiner(PersonDAO personDAO, MissionDAO missionDAO, ExpertiseDAO expertiseDAO, InvitationDAO invitationDAO) {
        this.personDAO = personDAO;
        this.missionDAO = missionDAO;
        this.expertiseDAO = expertiseDAO;
        this.invitationDAO = invitationDAO;
    }

    /**
     * Return the top-k most expensive missions. The cost of a mission is the sum
     * of the costs of its required equipments.
     * @return
     */
    public List<Mission> getExorbitance(int k) {
        if(k <= 0){
            throw new IllegalArgumentException("The number of K must at least 1");
        }

        Set<Mission> missionSet = missionDAO.getAllMissions();
        if (missionSet.isEmpty()) { return Collections.emptyList(); }
        List<Mission> missionList = new ArrayList<>(missionSet);

        Collections.sort(missionList, new Comparator<Mission>() {
            @Override
            public int compare(Mission m1, Mission m2) {
                return getSumOfMissionCost(m2) - getSumOfMissionCost(m1);
            }
        });
        if (missionList.size() < k) {
            return missionList;
        }
        return missionList.subList(0,k);
    }

    public int getSumOfMissionCost(Mission mission) {
        int sum = 0;
        Multiset<Equipment> equipmentMultiset = mission.getEquipmentsRequired();
        Iterator<Equipment> iterator1 = equipmentMultiset.iterator();
        while(iterator1.hasNext()) {
            Equipment equipment = iterator1.next();
            int cost = equipment.getAttributes().get(Equipment.Attribute.cost);
            sum += cost;
        }
        return sum;
    }

}
