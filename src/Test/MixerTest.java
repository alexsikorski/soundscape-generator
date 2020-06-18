package Test;

import Sound.Environment;
import Sound.SoundEnvironments;
import Utilities.Clip;
import Utilities.Mixer;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;


public class MixerTest {

    @Test
    public void setFadeInFadeOut() {
        Mixer mixer = new Mixer();
        Environment env = SoundEnvironments.loadEnvironment("Beach");

        mixer.setEnvironment(env);

        HashMap<Clip, Integer> fadeInTimings = mixer.getFadeInTimings();
        HashMap<Clip, Integer> fadeOutTimings = mixer.getFadeOutTimings();

        assertEquals(1, (int) fadeInTimings.values().iterator().next());
        assertEquals(49, (int) fadeOutTimings.values().iterator().next());
    }
}
