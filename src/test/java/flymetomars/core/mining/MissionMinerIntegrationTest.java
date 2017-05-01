package flymetomars.core.mining;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import flymetomars.data.ExpertiseDAO;
import flymetomars.data.InvitationDAO;
import flymetomars.data.MissionDAO;
import flymetomars.data.PersonDAO;
import flymetomars.model.Equipment;
import flymetomars.model.Expertise;
import flymetomars.model.Mission;
import flymetomars.model.Person;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.intThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Every.everyItem;
/**
 * Created by greyson on 22/4/17.
 */
public class MissionMinerIntegrationTest {
    private Mission m0,m1,m2,m3;
    private Equipment eq0,eq1,eq2,eq3,eq4;

    private PersonDAO pDao;
    private MissionDAO mDao;
    private ExpertiseDAO eDao;
    private InvitationDAO iDao;

    private MissionMiner mMiner;

    @Before
    public void setUp() {
        m0 = Mockito.mock(Mission.class);
        Mockito.when(m0.getName()).thenReturn("mission0");
        m1 = Mockito.mock(Mission.class);
        Mockito.when(m1.getName()).thenReturn("mission1");
        m2 = Mockito.mock(Mission.class);
        Mockito.when(m2.getName()).thenReturn("mission2");

        eq0 = Mockito.mock(Equipment.class,RETURNS_DEEP_STUBS);
        Mockito.when(eq0.getAttributes().get(Equipment.Attribute.cost)).thenReturn(1);
        eq1 = Mockito.mock(Equipment.class,RETURNS_DEEP_STUBS);
        Mockito.when(eq1.getAttributes().get(Equipment.Attribute.cost)).thenReturn(2);
        eq2 = Mockito.mock(Equipment.class,RETURNS_DEEP_STUBS);
        Mockito.when(eq2.getAttributes().get(Equipment.Attribute.cost)).thenReturn(3);
        eq3 = Mockito.mock(Equipment.class,RETURNS_DEEP_STUBS);
        Mockito.when(eq3.getAttributes().get(Equipment.Attribute.cost)).thenReturn(4);

        pDao = mock(PersonDAO.class);
        mDao = mock(MissionDAO.class);
        eDao = mock(ExpertiseDAO.class);
        iDao = mock(InvitationDAO.class);

        mMiner = new MissionMiner(pDao,mDao,eDao,iDao);
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void setKLessEqualThanZero() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("The number of K must at least 1");
        mMiner.getExorbitance(0);
    }

    @Test
    public void noMissionInDataBase() {
        Set<Mission> emptySet = Collections.emptySet();
        List<Mission> emptyList = Collections.emptyList();
        Mockito.when(mDao.getAllMissions()).thenReturn(emptySet);
        assertEquals(emptyList,mMiner.getExorbitance(1));
    }

    /**
     * The top-k most expensive missions. The cost of a mission is the sum of the costs of its required equipments.
     * Mission0 required Eq0 Eq1 , total cost is 3
     * Mission1 required Eq0 Eq1 Eq2 Eq3, total cost is 10
     * Mission2 required Eq0 Eq1 Eq2, total cost is 6
     *
     * Mission1 is the most expensive Mission
     * Mission2 place second place
     * Mission0 place third place
     */
    @Test
    public void missionExorbitanceTest() {
        Multiset<Equipment> multisetEq1 = HashMultiset.create();
        multisetEq1.add(eq0);
        multisetEq1.add(eq1);
        m0.setEquipmentsRequired(multisetEq1);
        Mockito.when(m0.getEquipmentsRequired()).thenReturn(multisetEq1);

        Multiset<Equipment> multisetEq2 = HashMultiset.create();
        multisetEq2.add(eq0);
        multisetEq2.add(eq1);
        multisetEq2.add(eq2);
        multisetEq2.add(eq3);
        m1.setEquipmentsRequired(multisetEq2);
        Mockito.when(m1.getEquipmentsRequired()).thenReturn(multisetEq2);

        Multiset<Equipment> multisetEq3 = HashMultiset.create();
        multisetEq3.add(eq0);
        multisetEq3.add(eq1);
        multisetEq3.add(eq2);
        m2.setEquipmentsRequired(multisetEq3);
        Mockito.when(m2.getEquipmentsRequired()).thenReturn(multisetEq3);

        Set<Mission> mSet = new HashSet<>();
        mSet.add(m0);//3
        mSet.add(m1);//10
        mSet.add(m2);//6
        Mockito.when(mDao.getAllMissions()).thenReturn(mSet);

        List<Mission> result = mMiner.getExorbitance(3 );
        assertEquals(3, result.size());
        assertEquals("mission1",result.get(0).getName());
        assertEquals("mission2",result.get(1).getName());
        assertEquals("mission0",result.get(2).getName());
    }

    /**
     * The top-k most expensive missions. The cost of a mission is the sum of the costs of its required equipments.
     * Mission0 required Eq0 Eq1 , total cost is 3
     * Mission1 required Eq0 Eq1 Eq2 Eq3, total cost is 10
     * Mission2 required Eq0 Eq1 Eq2, total cost is 6
     *
     * Mission1 is the most expensive Mission
     * Mission2 place second place
     * Mission0 place third place
     *
     * If k = 2
     * Will Return Mission1 and Mission2
     */
    @Test
    public void numOfMissionRecordsLessThanK() {
        Multiset<Equipment> multisetEq1 = HashMultiset.create();
        multisetEq1.add(eq0);
        multisetEq1.add(eq1);
        m0.setEquipmentsRequired(multisetEq1);
        Mockito.when(m0.getEquipmentsRequired()).thenReturn(multisetEq1);

        Multiset<Equipment> multisetEq2 = HashMultiset.create();
        multisetEq2.add(eq0);
        multisetEq2.add(eq1);
        multisetEq2.add(eq2);
        multisetEq2.add(eq3);
        m1.setEquipmentsRequired(multisetEq2);
        Mockito.when(m1.getEquipmentsRequired()).thenReturn(multisetEq2);

        Multiset<Equipment> multisetEq3 = HashMultiset.create();
        multisetEq3.add(eq0);
        multisetEq3.add(eq1);
        multisetEq3.add(eq2);
        m2.setEquipmentsRequired(multisetEq3);
        Mockito.when(m2.getEquipmentsRequired()).thenReturn(multisetEq3);

        Set<Mission> mSet = new HashSet<>();
        mSet.add(m0);//3
        mSet.add(m1);//10
        mSet.add(m2);//6
        Mockito.when(mDao.getAllMissions()).thenReturn(mSet);

        List<Mission> result = mMiner.getExorbitance(2 );
        assertEquals(2, result.size());
        assertEquals("mission1",result.get(0).getName());
        assertEquals("mission2",result.get(1).getName());
    }

}
