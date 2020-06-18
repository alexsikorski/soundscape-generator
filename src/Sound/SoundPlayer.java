package Sound;

import Utilities.Clip;

import javax.sound.sampled.FloatControl;
import java.util.Vector;

public class SoundPlayer {
    private Vector<Clip> playingSounds = new Vector<>();
    private Vector<Clip> queuedSounds = new Vector<>();

    public void playSounds() {
        try {
            if (playingSounds.size() < 1) {
                for (Clip clip : queuedSounds) {
                    clip.startClip();
                    playingSounds.add(clip);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void stopPlayingAllSounds() {
        for (Clip clip : playingSounds) {
            clip.stopClip();
        }
        playingSounds.clear();
    }

    public void changeMasterVolume(float value) {
        for (Clip clip : playingSounds) {
            FloatControl volumeControl = (FloatControl) clip.getClip().getControl(FloatControl.Type.MASTER_GAIN);
            float range = volumeControl.getMaximum() - volumeControl.getMinimum();
            float gain = (range * value) + volumeControl.getMinimum();
            volumeControl.setValue(gain);
        }
    }

    public void changeClipVolume(javax.sound.sampled.Clip clip, float value) {
        FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float range = volumeControl.getMaximum() - volumeControl.getMinimum();
        float gain = (range * value) + volumeControl.getMinimum();
        volumeControl.setValue(gain);
    }

    public Vector<Clip> getPlayingClips() {
        return playingSounds;
    }

    public boolean isPlaying() {
        return getPlayingClips().size() > 0;
    }

    public Vector<Clip> getQueuedSounds() {
        return queuedSounds;
    }

    public void setEnvironment(Environment environment) {
        try {
            stopPlayingAllSounds();
            playingSounds.clear();
            queuedSounds.clear();

            queuedSounds.addAll(environment.getClips());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int SoundRandomizer() {
        double randomizer = Math.random();
        System.out.println(randomizer);
        randomizer = randomizer * 100;
        int randomTime = (int) randomizer;
        System.out.println(randomTime);
        return randomTime;
    }

}