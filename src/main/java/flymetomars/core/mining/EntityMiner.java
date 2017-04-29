package flymetomars.core.mining;


import com.google.common.collect.Lists;
import flymetomars.data.ExpertiseDAO;
import flymetomars.data.InvitationDAO;
import flymetomars.data.MissionDAO;
import flymetomars.data.PersonDAO;
import flymetomars.model.Mission;
import flymetomars.model.Person;

import java.util.List;
import java.util.Set;

/**
 * @author Yuan-Fang Li
 * @version $Id: $
 */
public class EntityMiner {
    private PersonDAO personDAO;
    private MissionDAO missionDAO;
    private ExpertiseDAO expertiseDAO;
    private InvitationDAO invitationDAO;

    public EntityMiner(PersonDAO personDAO, MissionDAO missionDAO, ExpertiseDAO expertiseDAO, InvitationDAO invitationDAO) {
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
        // TODO: implement and test this!
        return Lists.newArrayList();

    }

    /**
     * Return the top-k persons with the most demanded expertises (required by missions).
     */
    public List<Person> getHotshots(int k) {
        // TODO: implement and test this!
        return Lists.newArrayList();
    }
}
