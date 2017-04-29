package flymetomars.core.mining;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import flymetomars.data.ExpertiseDAO;
import flymetomars.data.InvitationDAO;
import flymetomars.data.MissionDAO;
import flymetomars.data.PersonDAO;
import flymetomars.model.Expertise;
import flymetomars.model.Mission;
import flymetomars.model.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Yuan-Fang Li
 * @version $Id: $
 */
public class EntityMinerTest {
    private Person p1;
    private Mission m1;
    private Expertise e1;

    private PersonDAO pDao;
    private MissionDAO mDao;
    private ExpertiseDAO eDao;
    private InvitationDAO iDao;

    private EntityMiner miner;

    @Before
    public void setUp() {
        p1 = new Person();
        p1.setEmail("p1@example.com");

        m1 = new Mission();

        e1 = new Expertise();
        e1.setDescription("Levitation");

        pDao = mock(PersonDAO.class);
        mDao = mock(MissionDAO.class);
        eDao = mock(ExpertiseDAO.class);
        iDao = mock(InvitationDAO.class);

        miner = new EntityMiner(pDao, mDao, eDao, iDao);
    }

    @Test
    public void oneSinglePersonIsAHotshot() {
        p1.addExpertise(e1);
        m1.getParticipantSet().add(p1);
        m1.getExpertiseRequired().add(e1);

        when(mDao.getAll()).thenReturn(Lists.newArrayList(m1));

        List<Person> hotshots = miner.getHotshots(2);
        assertEquals(1, hotshots.size());
        assertEquals(p1, hotshots.get(0));
    }
}