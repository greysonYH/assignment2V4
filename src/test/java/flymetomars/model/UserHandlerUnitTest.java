package flymetomars.model;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by greyson on 11/4/17.
 */
public class UserHandlerUnitTest {
    private UserHandler userHandler;
    @Before
    public void setUp() {
        userHandler = new UserHandler();
    }

    @Test
    public void updatePassword() {
        userHandler = mock(UserHandler.class);
        //when(userHandler.updatePassword()).thenReturn("fishing");
    }
}