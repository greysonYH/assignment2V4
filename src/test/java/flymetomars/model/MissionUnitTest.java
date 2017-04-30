package flymetomars.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertNotNull;

/**
 * Created by greyson on 18/4/17.
 */
public class MissionUnitTest {
    private Mission m;

    @Before
    public void setUp() {
        m = new Mission();
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    @Test
    public void testEqualMethodWithNullVariables() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("variables cannot be null.");
        Mission m2 = new Mission();
        m2.setTime(null);
        m2.setName(null);
        m2.setDuration(0);
        m2.setLocation(null);
        m2.setCaptain(null);
        m2.setDescription(null);
        m2.setMaxAttributes(null);
        m.equals(m2);
    }

    @Test
    public void testHashCodeMethodWithNullVariables() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("variables cannot be null.");
        m.hashCode();
    }

    //Test Name

    @Test
    public void testSetName() {
        m = new Mission();
        m.setName("impossible");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNameEmpty() {
        m = new Mission();
        m.setName("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNameNull() {
        m = new Mission();
        m.setName(null);
    }



    //Test Location

    @Test
    public void testSetLocation() {
        m = new Mission();
        m.setLocation("australia");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLocationEmpty() {
        m = new Mission();
        m.setLocation("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLocationNull() {
        m = new Mission();
        m.setLocation(null);
    }



    //Test Description

    @Test
    public void testSetDescription() {
        m = new Mission();
        m.setDescription("australia");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDescriptionEmpty() {
        m = new Mission();
        m.setDescription("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDescriptionNull() {
        m = new Mission();
        m.setDescription(null);
    }


    //Test Set not null

    @Test
    public void personSetNotNull() { assertNotNull("person not null", m.getParticipantSet());}

    @Test
    public void invitationSetNotNull() {
        assertNotNull("invitation not null", m.getInvitationSet());
    }

    @Test
    public void equipmentSetNotNull() { assertNotNull("equipment not null", m.getEquipmentsRequired());
    }



    //Test Time

    @Test
    public void testSetTime() {
        m = new Mission();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date dateTime = null;
        try {
            dateTime = df.parse("01-09-2010");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        m.setTime(dateTime);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testSetTimeNull() {
        m = new Mission();
        m.setTime(null);
    }

    @Test
    public void testMissionHasDefaultNullTime() {
        m = new Mission();
        Assert.assertNull(m.getTime());
    }

    //Test captain
    @Test
    public void testSetCaptain() {
        m = new Mission();
        m.setCaptain(new Person());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCaptainNull() {
        m = new Mission();
        m.setCaptain(null);
    }

    @Test
    public void testMissionHasDefaultNullCaptain() {
        m = new Mission();
        Assert.assertNull(m.getCaptain());
    }

}
