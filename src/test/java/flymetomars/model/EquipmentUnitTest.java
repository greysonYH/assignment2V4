package flymetomars.model;

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

    ////
    @Test
    public void setNameNull() {

    }

    @Test
    public void setWeightNull() {

    }

    @Test
    public void setVolumnNull() {

    }

    @Test
    public void setCostNull() {

    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testEqualMethodWithNullVariables() {
        e1 = new Equipment("Laser", 10, 1, 20);
        e2 = new Equipment(null,0,0,0);
        e1.equals(e2);
    }

    @Test
    public void testHashCodeMethodWithNullVariables() {
        //e1 = new Equipment("Laser", 10, 1, 20);
        e2 = new Equipment(null,0,0,0);
        e2.hashCode();
    }
}