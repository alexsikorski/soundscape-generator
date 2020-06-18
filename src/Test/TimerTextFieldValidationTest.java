package Test;

import GUI.Frame;
import Sound.Environment;
import Sound.SoundPlayer;
import Utilities.UserSettingsHandler;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTextFieldValidationTest {

    Frame frame = new Frame();
    @Test
    public void letterInputValidation(){
        frame.getTimerTextField().setText("2");
        frame.getSetTimerButton().doClick();
        assertTrue(frame.getPlayer().getPlayingClips().size()!= 0);
;
    }

    @Test
    public void NumberInputValidation(){
        frame.getTimerTextField().setText("InvaliInput");
        frame.getSetTimerButton().doClick();
        assertTrue(frame.getPlayer().getPlayingClips().size()== 0);
        ;
    }


}
