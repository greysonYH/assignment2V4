package flymetomars.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by yli on 16/03/15.
 */
public class PersonUnitTest {
    private Person p;

    @Before
    public void setUp() {
        p = new Person();
    }

    @Test
    public void expertiseNotNull() {
        assertNotNull("expertise not null", p.getExpertise());
    }

    @Test
    public void passwordNotNullOrEmpty() {
        try {
            p.setPassword(null);
            fail("No exception thrown for null password");
        } catch (Exception e) {
            assertTrue("Throws IAE", e instanceof IllegalArgumentException);
            assertTrue("Message contains null", e.getMessage().contains("null"));
        }

        try {
            p.setPassword("");
            fail("No exception thrown for empty password");
        } catch (Exception e) {
            assertTrue("Throws IAE", e instanceof IllegalArgumentException);
            assertTrue("Message contains empty", e.getMessage().contains("empty"));
        }
    }

    @Test
    public void differentPersonNotEqual() {
        p.setEmail("abc@abc.net.au");
        Person q = new Person();
        assertNotEquals("q doesn't have an email", p, q);

        q.setEmail("abc@abc.net");
        assertNotEquals("Different emails", p, q);
    }

    @Test
    public void sameEmailSamePerson() {
        String email = "abc@abc.net.au";
        p.setEmail(email);

        Person q = new Person();
        q.setEmail(email);

        assertEquals("Same person", p, q);

        p.setLastName("Foo");
        q.setLastName("Bar");

        assertEquals("Names don't matter", p, q);
    }

    @Test
    public void newNullExpertiseThrowsIAE() {
        try {
            p.addExpertise(null);
            fail("No exception thrown for null expertise");
        } catch (Exception e) {
            assertTrue("Throws IAE", e instanceof IllegalArgumentException);
            assertTrue("Message contains null", e.getMessage().contains("null"));
        }
    }

    @Test
    public void newExpertiseNullDescriptionThrowsIAE() {
        // mock an Expertise object with expected behaviour
        Expertise exp = mock(Expertise.class);
        when(exp.getDescription()).thenReturn(null);

        try {
            p.addExpertise(exp);
            fail("No exception thrown for null expertise description");
        } catch (Exception e) {
            assertTrue("Throws IAE", e instanceof IllegalArgumentException);
            assertTrue("Message contains null", e.getMessage().contains("null"));
            assertTrue("Message contains description", e.getMessage().contains("description"));
        }
    }

    @Test
    public void newExpertiseWithNonemptyDescription() {
        // mock an Expertise object with expected behaviour
        Expertise exp = mock(Expertise.class);
        when(exp.getDescription()).thenReturn("fishing");

        assertEquals("Empty collection", 0, p.getExpertise().size());
        p.addExpertise(exp);
        assertEquals("1 expertise", 1, p.getExpertise().size());
    }


    @Test
    public void MissionRegisteredNotNull() {
        assertNotNull("expertise not null", p.getMissionRegistered());
    }

    @Test
    public void InvitationRecievedNotNull() {
        assertNotNull("expertise not null", p.getInvitationsReceived());
    }

    @Test
    public void equipmentOwendNotNull() {
        assertNotNull("expertise not null", p.getEquipmentOwned());
    }


    //Test First Name

    @Test
    public void testSetFirstName() {
        p.setFirstName("greyson");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFirstNameEmpty() {
        p.setFirstName("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFirstNameNull() {
        p.setFirstName(null);
    }

    @Test
    public void testPersonHasDefaultEmptyFirstName() {
        Assert.assertTrue(p.getFirstName().isEmpty());
    }

    //Test Last Name

    @Test
    public void testSetLastName() {
        p.setLastName("greyson");
    }

    @Test
    public void testSetLastNameEmpty() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("variables cannot be empty.");
        p.setLastName("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLastNameNull() {
        p.setLastName(null);
    }

    @Test
    public void testPersonHasDefaultEmptyLastName() {
        Assert.assertTrue(p.getLastName().isEmpty());
    }

    //Test Email

    @Test
    public void testSetEmail() {
        p.setEmail("greyson@greyson.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmailEmpty() {
        p.setEmail("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullEmail() {
        p.setEmail(null);
    }

    @Test
    public void testPersonHasDefaultEmptyEmail() {
        Assert.assertTrue(p.getEmail().isEmpty());
    }

    @Test
    public void testSetInvalidEmail() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("illegal email format.");
        p.setEmail("asdfasdf");  //no "@" and "."
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetBadEmail() {
        p.setEmail("@.");
    }



    //Test Password
    @Test
    public void testSetPassword() {
        p.setPassword("as12345678"); //12 characters
    }

    @Test
    public void testSetBlankPassword() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("variables cannot be empty.");
        p.setPassword("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullPassword() {
        p.setPassword(null);
    }

    @Test
    public void testPersonHasDefaultEmptyPassword() {
        Assert.assertTrue(p.getPassword().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTooLongPassword() {
        p.setPassword("asd1234567890");  //13 characters
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTooShortPassword() {
        p.setPassword("asd123456");  //9 characters
    }

    ///
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    @Test
    public void testEqualMethodWithNullVariables() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("variables cannot be null.");
        Person p1 = new Person();
        p1.setFirstName(null);
        p1.setLastName(null);
        p1.setEmail(null);
        p1.setPassword(null);
        p.equals(p1);
    }

    @Test
    public void testHashCodeMethodWithNullVariables() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("variables cannot be null.");
        Person p1 = new Person();
        p1.setFirstName(null);
        p1.setLastName(null);
        p1.setEmail(null);
        p1.setPassword(null);
        p1.hashCode();
    }
}