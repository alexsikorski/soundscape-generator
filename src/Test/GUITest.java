package Test;

import GUI.Frame;
import Sound.SoundEnvironments;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GUITest {
    Frame gui = new Frame();


    @Test
    public void testStartButtonCreation() {
        assertEquals(gui.getStart_sound().getText(), "Start");
    }

    @Test
    public void testStopButtonCreation() {
        assertEquals(gui.getStop_sound().getText(), "Stop");
    }

    @Test
    public void testComboBoxCreation() {
        SoundEnvironments.loadEnvironment("Beach");
        gui.populateComboBox();

        assertEquals(gui.getEnvironmentSelector().getSelectedItem(), "Beach");
    }


}
