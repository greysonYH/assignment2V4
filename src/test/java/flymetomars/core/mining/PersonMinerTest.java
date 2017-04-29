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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.intThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

/**
 * Created by greyson on 29/4/17.
 */
public class PersonMinerTest {
    private Mission m0,m1,m2,m3;
    private Equipment eq0,eq1,eq2,eq3,eq4;
    private Expertise ex0,ex1,ex2,ex3;
    private Person p0,p1,p2,p3,p4;

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

        p0 = Mockito.mock(Person.class);
        Mockito.when(p0.getEmail()).thenReturn("person0@email.com");
        p1 = Mockito.mock(Person.class);
        Mockito.when(p1.getEmail()).thenReturn("person1@email.com");
        p2 = Mockito.mock(Person.class);
        Mockito.when(p2.getEmail()).thenReturn("person2@email.com");
        p3 = Mockito.mock(Person.class);
        Mockito.when(p3.getEmail()).thenReturn("person3@email.com");

        ex0 = Mockito.mock(Expertise.class);
        Mockito.when(ex0.getDescription()).thenReturn("aaa");
        ex1 = Mockito.mock(Expertise.class);
        Mockito.when(ex1.getDescription()).thenReturn("bbb");
        ex2 = Mockito.mock(Expertise.class);
        Mockito.when(ex2.getDescription()).thenReturn("ccc");
        ex3 = Mockito.mock(Expertise.class);
        Mockito.when(ex3.getDescription()).thenReturn("ddd");

        pDao = mock(PersonDAO.class);
        mDao = mock(MissionDAO.class);
        eDao = mock(ExpertiseDAO.class);
        iDao = mock(InvitationDAO.class);

        pMiner = new PersonMiner(pDao, mDao, eDao, iDao);
        mMiner = new MissionMiner(pDao,mDao,eDao,iDao);
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test//
    public void setKLessEqualThanZero() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("The number of K must at least 1");
        mMiner.getExorbitance(intThat(lessThanOrEqualTo(0)));
    }

    @Test//
    public void noMissionInDataBase() {
        Set<Mission> mSet = new HashSet<>();
        Mockito.when(mDao.getAllMissions()).thenReturn(mSet);
        assertEquals(mSet,mMiner.getExorbitance(1));
    }

    @Test
    public void personHotShotTest() {
        Set<Expertise> expertiseSet1 =new HashSet<>();
        expertiseSet1.add(ex0);
        expertiseSet1.add(ex1);
        expertiseSet1.add(ex2);
        m0.setExpertiseRequired(expertiseSet1);
        Mockito.when(m0.getExpertiseRequired()).thenReturn(expertiseSet1);

        Set<Expertise> expertiseSet2 =new HashSet<>();
        expertiseSet2.add(ex0);
        //expertiseSet2.add(ex1);
        expertiseSet2.add(ex2);

        m1.setExpertiseRequired(expertiseSet2);
        Mockito.when(m1.getExpertiseRequired()).thenReturn(expertiseSet2);

        Set<Expertise> expertiseSet3 =new HashSet<>();
        expertiseSet3.add(ex0);
        expertiseSet3.add(ex1);
        //expertiseSet3.add(ex2);
        m2.setExpertiseRequired(expertiseSet3);
        Mockito.when(m2.getExpertiseRequired()).thenReturn(expertiseSet3);

        Set<Expertise> expertiseSet4 =new HashSet<>();
        //expertiseSet3.add(ex0);
        expertiseSet4.add(ex1);
        expertiseSet4.add(ex2);
        //m2.setExpertiseRequired(expertiseSet3);
        //Mockito.when(m2.getExpertiseRequired()).thenReturn(expertiseSet3);

        Set<Mission> mSet = new HashSet<>();
        mSet.add(m0);//3
        mSet.add(m1);//10
        mSet.add(m2);//6

        //p0.setExpertise(expertiseSet1);
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

        pMiner.getHotshots(3);

    }
}
