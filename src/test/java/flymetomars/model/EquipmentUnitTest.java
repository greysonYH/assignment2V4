package flymetomars.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * @author Yuan-Fang Li
 * @version $Id: $
 */
public class EquipmentUnitTest {
    private Equipment e1, e2;

    @Before
    public void setUp() {
        e1 = new Equipment();
        e2 = new Equipment();
    }

    @Test
    public void sameEquipmentNeedsToHaveSameAttributesAndName() {
        e1 = new Equipment("Laser", 10, 2, 20);
        e2 = new Equipment("Laser", 10, 2, 20);
        assertEquals(e1, e2);
    }

    @Test
    public void differentEquipmentCanHaveDifferentAttributes() {
        e1 = new Equipment("Laser", 10, 1, 20);
        e2 = new Equipment("Laser", 10, 2, 20);
        assertNotEquals(e1, e2);
    }

    @Test
    public void differentEquipmentCanHaveDifferentNames() {
        e1 = new Equipment("Laser", 10, 2, 20);
        e2 = new Equipment("laser", 10, 2, 20);
        assertNotEquals(e1, e2);
    }

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void testEqualMethodWithNullVariables() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("variables cannot be null");
        e1 = new Equipment("Laser", 10, 1, 20);
        e2 = new Equipment(null,0,0,0);
        e1.equals(e2);
    }

    @Test
    public void testHashCodeMethodWithNullVariables() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("variables cannot be null");
        e1 = new Equipment("Laser", 10, 1, 20);
        e2 = new Equipment(null,0,0,0);
        e2.hashCode();
    }

    @Test
    public void testSetWeightNegative() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("this number can not be negative");
        e1 = new Equipment("Laser", -10, 1, 1);
    }

    @Test
    public void testSetVolumeNegative() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("this number can not be negative");
        e1 = new Equipment("Laser", 1, -1, 1);
    }

    @Test
    public void testSetCostNegative() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("this number can not be negative");
        e1 = new Equipment("Laser", 1, 1, -1);
    }
}