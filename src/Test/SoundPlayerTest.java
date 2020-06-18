package Test;

import Sound.Environment;
import Sound.SoundEnvironments;
import Sound.SoundPlayer;
import Utilities.Clip;
import org.junit.Test;

import javax.sound.sampled.FloatControl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SoundPlayerTest {

    @Test
    public void playStopSounds() {
        SoundPlayer player = new SoundPlayer();
        Environment env = SoundEnvironments.loadEnvironment("Beach");

        player.setEnvironment(env);
        player.getQueuedSounds().addAll(env.getClips());
        player.playSounds();

        assertEquals(player.getQueuedSounds(), player.getPlayingClips());

        player.stopPlayingAllSounds();
        assertTrue("Stopped playing sounds: ", player.getPlayingClips().isEmpty());
    }

    @Test
    public void setClipVolume() {
        SoundPlayer player = new SoundPlayer();
        Environment env = SoundEnvironments.loadEnvironment("Beach");

        player.setEnvironment(env);
        player.getQueuedSounds().addAll(env.getClips());
        player.playSounds();

        Clip clip = player.getPlayingClips().get(1);
        FloatControl volumeControl = (FloatControl) clip.getClip().getControl(FloatControl.Type.MASTER_GAIN);

        float correctValue = (float) 70 / 100;

        clip.setVolume(correctValue);
        float correctGain = clip.getGain();

        assertEquals(correctGain, volumeControl.getValue(), 0);
    }

    @Test
    public void changeClipVolume() {
    }

}