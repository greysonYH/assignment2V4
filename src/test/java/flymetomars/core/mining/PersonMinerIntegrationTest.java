package flymetomars.core.mining;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import flymetomars.data.ExpertiseDAO;
import flymetomars.data.InvitationDAO;
import flymetomars.data.MissionDAO;
import flymetomars.data.PersonDAO;
import flymetomars.model.Equipment;
import flymetomars.model.Expertise;
import flymetomars.model.Mission;
import flymetomars.model.Person;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.util.*;

import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.intThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

/**
 * Created by greyson on 29/4/17.
 */
public class PersonMinerIntegrationTest {
    private Mission m0,m1,m2,m3;
    private Equipment eq0,eq1,eq2,eq3,eq4;
    private Expertise ex0,ex1,ex2,ex3,ex4,ex5;
    private Person p0,p1,p2,p3,p4,p5,p6;

    private PersonDAO pDao;
    private MissionDAO mDao;
    private ExpertiseDAO eDao;
    private InvitationDAO iDao;

    private MissionMiner mMiner;
    private PersonMiner pMiner;

    @Before
    public void setUp() {
        m0 = Mockito.mock(Mission.class);
        Mockito.when(m0.getName()).thenReturn("mission0");
        m1 = Mockito.mock(Mission.class);
        Mockito.when(m1.getName()).thenReturn("mission1");
        m2 = Mockito.mock(Mission.class);
        Mockito.when(m2.getName()).thenReturn("mission2");
        m3 = Mockito.mock(Mission.class);
        Mockito.when(m3.getName()).thenReturn("mission3");

        p0 = Mockito.mock(Person.class);
        Mockito.when(p0.getEmail()).thenReturn("person0@email.com");
        p1 = Mockito.mock(Person.class);
        Mockito.when(p1.getEmail()).thenReturn("person1@email.com");
        p2 = Mockito.mock(Person.class);
        Mockito.when(p2.getEmail()).thenReturn("person2@email.com");
        p3 = Mockito.mock(Person.class);
        Mockito.when(p3.getEmail()).thenReturn("person3@email.com");
        p4 = Mockito.mock(Person.class);
        Mockito.when(p4.getEmail()).thenReturn("person4@email.com");
        p5 = Mockito.mock(Person.class);
        Mockito.when(p5.getEmail()).thenReturn("person5@email.com");

        ex0 = Mockito.mock(Expertise.class);
        Mockito.when(ex0.getDescription()).thenReturn("aaa");
        ex1 = Mockito.mock(Expertise.class);
        Mockito.when(ex1.getDescription()).thenReturn("bbb");
        ex2 = Mockito.mock(Expertise.class);
        Mockito.when(ex2.getDescription()).thenReturn("ccc");
        ex3 = Mockito.mock(Expertise.class);
        Mockito.when(ex3.getDescription()).thenReturn("ddd");
        ex4 = Mockito.mock(Expertise.class);
        Mockito.when(ex4.getDescription()).thenReturn("eee");
        ex5 = Mockito.mock(Expertise.class);
        Mockito.when(ex5.getDescription()).thenReturn("fff");

        pDao = mock(PersonDAO.class);
        mDao = mock(MissionDAO.class);
        eDao = mock(ExpertiseDAO.class);
        iDao = mock(InvitationDAO.class);

        pMiner = new PersonMiner(pDao, mDao, eDao, iDao);
        mMiner = new MissionMiner(pDao,mDao,eDao,iDao);
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();


    @Test
    public void setKLessEqualThanZero() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("The number of K must at least 1");
        pMiner.getHotshots(0);

    }

    @Test
    public void noMissionAndPersonInDataBase() {
        Set<Mission> emptySet = Collections.emptySet();
        List<Mission> emptyList = Collections.emptyList();
        Mockito.when(mDao.getAllMissions()).thenReturn(emptySet);
        assertEquals(emptyList,mMiner.getExorbitance(1));
        Set<Person> emptySet1 = Collections.emptySet();
        List<Person> emptyList1 = Collections.emptyList();
        Mockito.when(pDao.getAllPerson()).thenReturn(emptySet1);
        assertEquals(emptyList1,pMiner.getHotshots(1));
    }

    /**
     * The top-k persons with the most demanded expertises (required by missions).
     * Mission0 required expertise Ex0 Ex1 Ex2
     * Mission1 required expertise Ex1 Ex2
     * Mission2 required Eq0 Ex1
     * The most required expertise is Ex1
     *
     * Person0 have expertise Ex0 Ex1 Ex2
     * Person1 have expertise Ex0 Ex2
     * Person2 have expertise Ex0 Ex2
     * Person3 have expertise Ex0 Ex2
     *
     * Person0 will be Return
     */
    @Test
    public void personHotShotTest() {
        Set<Expertise> expertiseSet1 =new HashSet<>();
        expertiseSet1.add(ex0);
        expertiseSet1.add(ex1);
        expertiseSet1.add(ex2);
        m0.setExpertiseRequired(expertiseSet1);
        Mockito.when(m0.getExpertiseRequired()).thenReturn(expertiseSet1);

        Set<Expertise> expertiseSet2 =new HashSet<>();
        //expertiseSet2.add(ex0);
        expertiseSet2.add(ex1);
        expertiseSet2.add(ex2);
        m1.setExpertiseRequired(expertiseSet2);
        Mockito.when(m1.getExpertiseRequired()).thenReturn(expertiseSet2);

        Set<Expertise> expertiseSet3 =new HashSet<>();
        //expertiseSet3.add(ex0);
        expertiseSet3.add(ex1);
        //expertiseSet3.add(ex2);
        m2.setExpertiseRequired(expertiseSet3);
        Mockito.when(m2.getExpertiseRequired()).thenReturn(expertiseSet3);

        Set<Expertise> expertiseSet4 =new HashSet<>();
        expertiseSet3.add(ex0);
        //expertiseSet4.add(ex1);
        expertiseSet4.add(ex2);

        Set<Mission> mSet = new HashSet<>();
        mSet.add(m0);
        mSet.add(m1);
        mSet.add(m2);

        p0.setExpertise(expertiseSet1);
        Mockito.when(p0.getExpertise()).thenReturn(expertiseSet1);
        p1.setExpertise(expertiseSet4);
        Mockito.when(p1.getExpertise()).thenReturn(expertiseSet4);
        p2.setExpertise(expertiseSet4);
        Mockito.when(p2.getExpertise()).thenReturn(expertiseSet4);
        p3.setExpertise(expertiseSet4);
        Mockito.when(p3.getExpertise()).thenReturn(expertiseSet4);

        Set<Person> pSet = new HashSet<>();
        pSet.add(p0);
        pSet.add(p1);
        pSet.add(p2);
        pSet.add(p3);

        Mockito.when(pDao.getAllPerson()).thenReturn(pSet);
        Mockito.when(mDao.getAllMissions()).thenReturn(mSet);

        assertEquals(1, pMiner.getHotshots(3).size());
        assertEquals(p0, pMiner.getHotshots(3).get(0));
    }

    /**
     * Return a set of persons who connect mostly-non-interacting social circles.
     *
     * Mission0 participants are Person0 Person1
     * Mission1 participants are Person2 Person3
     * Mission2 participants are Person0 Person1 Person2
     * Mission3 participants are Person3 Person4 Person5
     *
     * Mission2 and Mission3 are two most non interacting mission because they have no same participant
     * Person 2 and Person 3 have participant Mission1
     * Person2 and Person3 will be return as PowerBroker
     * */
    @Test
    public void personPowerBrokerTest() {
        Set<Person> pSet1 = new HashSet<>();
        pSet1.add(p0);
        pSet1.add(p1);
        Mockito.when(m0.getParticipantSet()).thenReturn(pSet1);

        Set<Person> pSet2 = new HashSet<>();
        pSet2.add(p2);
        pSet2.add(p3);
        Mockito.when(m1.getParticipantSet()).thenReturn(pSet2);

        Set<Person> pSet3 = new HashSet<>();
        pSet3.add(p0);
        pSet3.add(p1);
        pSet3.add(p2);
        Mockito.when(m2.getParticipantSet()).thenReturn(pSet3);

        Set<Person> pSet4 = new HashSet<>();
        pSet4.add(p3);
        pSet4.add(p4);
        pSet4.add(p5);
        Mockito.when(m3.getParticipantSet()).thenReturn(pSet4);

        Set<Mission> mSet0 = new HashSet<>();
        mSet0.add(m0);
        mSet0.add(m2);
        Mockito.when(p0.getMissionRegistered()).thenReturn(mSet0);

        Set<Mission> mSet1 = new HashSet<>();
        mSet1.add(m0);
        mSet1.add(m2);
        Mockito.when(p1.getMissionRegistered()).thenReturn(mSet1);

        Set<Mission> mSet2 = new HashSet<>();
        mSet2.add(m1);
        mSet2.add(m2);
        Mockito.when(p2.getMissionRegistered()).thenReturn(mSet2);

        Set<Mission> mSet3 = new HashSet<>();
        mSet3.add(m1);
        mSet3.add(m3);
        Mockito.when(p3.getMissionRegistered()).thenReturn(mSet3);

        Set<Mission> mSet4 = new HashSet<>();
        mSet4.add(m3);
        Mockito.when(p4.getMissionRegistered()).thenReturn(mSet4);

        Set<Mission> mSet5 = new HashSet<>();
        mSet5.add(m3);
        Mockito.when(p5.getMissionRegistered()).thenReturn(mSet5);

        Set<Mission> mSet6 = new HashSet<>();
        mSet6.add(m0);
        mSet6.add(m1);
        mSet6.add(m2);
        mSet6.add(m3);
        Mockito.when(mDao.getAllMissions()).thenReturn(mSet6);

        assertEquals(2, pMiner.getPowerBrokers().size());
        assertTrue(pMiner.getPowerBrokers().contains(p2));
        assertTrue(pMiner.getPowerBrokers().contains(p3));
    }


    /**
     * Given a person, natural numbers k and m,
     * return m other persons that this person is not a friend with up to (and including) k connections.
     *
     * Expertise Role List Ex0 Ex1 Ex2 Ex3 Ex4
     *
     * Mission0 participants are Person0 Person1
     * Mission1 participants are Person2 Person3
     * Mission2 participants are Person0 Person1 Person2
     * Mission3 participants are Person3 Person4 Person5
     *
     * If want to find New Frontier for Person0
     * K = 1 M = 3
     * Person1 and Person 2 have particpant Mission with Person0
     * Person3 Person4 Person5 will be return
     *
     * K = 2 M = 2
     * Person3 have participant Mission with Person2
     * Person4 Person5 will be return
     *
     * K = 3 M = 3
     * Person4 Person5 have participant Mission with Person3
     * No one will be return
     *
     */
    @Test
    public void personNewFrontiersTest() {
        Set<Person> pSet1 = new HashSet<>();
        pSet1.add(p0);
        pSet1.add(p1);
        Mockito.when(m0.getParticipantSet()).thenReturn(pSet1);

        Set<Person> pSet2 = new HashSet<>();
        pSet2.add(p2);
        pSet2.add(p3);
        Mockito.when(m1.getParticipantSet()).thenReturn(pSet2);

        Set<Person> pSet3 = new HashSet<>();
        pSet3.add(p0);
        pSet3.add(p1);
        pSet3.add(p2);
        Mockito.when(m2.getParticipantSet()).thenReturn(pSet3);

        Set<Person> pSet4 = new HashSet<>();
        pSet4.add(p3);
        pSet4.add(p4);
        pSet4.add(p5);
        Mockito.when(m3.getParticipantSet()).thenReturn(pSet4);

        Set<Mission> mSet0 = new HashSet<>();
        mSet0.add(m0);
        mSet0.add(m2);
        Mockito.when(p0.getMissionRegistered()).thenReturn(mSet0);

        Set<Mission> mSet1 = new HashSet<>();
        mSet1.add(m0);
        mSet1.add(m2);
        Mockito.when(p1.getMissionRegistered()).thenReturn(mSet1);

        Set<Mission> mSet2 = new HashSet<>();
        mSet2.add(m1);
        mSet2.add(m2);
        Mockito.when(p2.getMissionRegistered()).thenReturn(mSet2);

        Set<Mission> mSet3 = new HashSet<>();
        mSet3.add(m1);
        mSet3.add(m3);
        Mockito.when(p3.getMissionRegistered()).thenReturn(mSet3);

        Set<Mission> mSet4 = new HashSet<>();
        mSet4.add(m3);
        Mockito.when(p4.getMissionRegistered()).thenReturn(mSet4);

        Set<Mission> mSet5 = new HashSet<>();
        mSet5.add(m3);
        Mockito.when(p5.getMissionRegistered()).thenReturn(mSet5);

        Set<Person> pSet6 = new HashSet<>();
        pSet6.add(p0);
        pSet6.add(p1);
        pSet6.add(p2);
        pSet6.add(p3);
        pSet6.add(p4);
        pSet6.add(p5);

        Mockito.when(pDao.getAllPerson()).thenReturn(pSet6);

        assertEquals(3, pMiner.getNerFronties(p0,1,3).size());
        assertTrue(pMiner.getNerFronties(p0,1,3).contains(p5));
        assertTrue(pMiner.getNerFronties(p0,1,3).contains(p4));
        assertTrue(pMiner.getNerFronties(p0,1,3).contains(p3));

        assertEquals(2, pMiner.getNerFronties(p0,2,2).size());
        assertTrue(pMiner.getNerFronties(p0,2,2).contains(p5));
        assertTrue(pMiner.getNerFronties(p0,2,2).contains(p4));

        assertEquals(0, pMiner.getNerFronties(p0,3,3).size());
        assertFalse(pMiner.getNerFronties(p0,3,3).contains(p5));
        assertFalse(pMiner.getNerFronties(p0,3,3).contains(p4));
        assertFalse(pMiner.getNerFronties(p0,3,3).contains(p3));
        assertFalse(pMiner.getNerFronties(p0,3,3).contains(p2));
        assertFalse(pMiner.getNerFronties(p0,3,3).contains(p1));

    }

    /**
     * Given the required roles of a mission, find a list of persons with expertise that satisfy the roles.
     * Expertise Role List Ex0 Ex1 Ex2 Ex3 Ex4
     *
     * Mission1 required expertise Ex1 Ex2
     * Mission2 required Eq0 Ex1
     * The most required expertise is Ex1
     *
     * Person0 have expertise Ex0
     * Person1 have expertise Ex1 Ex2
     * Person2 have expertise Ex2 Ex3 Ex4
     * Person3 have expertise Ex5
     *
     * Person0 Person1 Person2 will meet Role required
     * Person4 can not meet Role required
     *
     */
    @Test
    public void personRosteringTest() {
        Set<Expertise> expertiseSet0 =new HashSet<>();
        expertiseSet0.add(ex0);
        expertiseSet0.add(ex1);
        expertiseSet0.add(ex2);
        expertiseSet0.add(ex3);
        expertiseSet0.add(ex4);

        Set<Expertise> expertiseSet1 =new HashSet<>();
        expertiseSet1.add(ex0);
        p0.setExpertise(expertiseSet1);
        Mockito.when(p0.getExpertise()).thenReturn(expertiseSet1);

        Set<Expertise> expertiseSet2 =new HashSet<>();
        expertiseSet2.add(ex1);
        expertiseSet2.add(ex2);
        p1.setExpertise(expertiseSet2);
        Mockito.when(p1.getExpertise()).thenReturn(expertiseSet2);

        Set<Expertise> expertiseSet3 =new HashSet<>();
        expertiseSet3.add(ex2);
        expertiseSet3.add(ex3);
        expertiseSet3.add(ex4);
        p2.setExpertise(expertiseSet3);
        Mockito.when(p2.getExpertise()).thenReturn(expertiseSet3);

        Set<Expertise> expertiseSet4 =new HashSet<>();
        expertiseSet4.add(ex5);
        p3.setExpertise(expertiseSet4);
        Mockito.when(p3.getExpertise()).thenReturn(expertiseSet4);

        Set<Person> pSet = new HashSet<>();
        pSet.add(p0);
        pSet.add(p1);
        pSet.add(p2);
        pSet.add(p3);
        Mockito.when(pDao.getAllPerson()).thenReturn(pSet);

        Set<Person> personSet = pMiner.getRostering(expertiseSet0);
        assertTrue(personSet.contains(p0));
        assertTrue(personSet.contains(p1));
        assertTrue(personSet.contains(p2));
        assertFalse(personSet.contains(p3));
    }
}
