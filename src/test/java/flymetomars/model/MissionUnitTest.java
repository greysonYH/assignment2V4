package flymetomars.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by greyson on 18/4/17.
 */
public class MissionUnitTest {
    private Mission m1;

    @Before
    public void setUp() {
        m1 = new Mission();
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
        m1.equals(m2);
    }

    @Test
    public void testHashCodeMethodWithNullVariables() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("variables cannot be null.");
        m1.hashCode();
    }
}
