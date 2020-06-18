package Test;

import Utilities.UserSettingsHandler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PreferencesTest {

    private UserSettingsHandler handler = new UserSettingsHandler();

    private void setDefaultValues() {
        handler.setDefaultEnvironment();
    }

    @Test
    public void testDefaultValues() {
        setDefaultValues();
        String defaultValue1 = "Beach";
        String staticDefaultValue = UserSettingsHandler.DEFAULT_VALUE;
        assertEquals(handler.getLastSetEnvironment(), defaultValue1);
        assertEquals(handler.getLastSetEnvironment(), staticDefaultValue);
    }

    @Test
    public void testSetValue() {
        setDefaultValues();
        String newEnvironment = "Beach";
        handler.setEnvironment(newEnvironment);

        assertEquals(handler.getLastSetEnvironment(), newEnvironment);
    }
}
