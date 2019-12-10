package Utilities;

import Sound.SoundEnvironments;

import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Clip implements Runnable {
    private javax.sound.sampled.Clip audioClip;
    private String clipName;


    private float currDB = 0F;
    private float targetDB = 1F;
    private float fadePerStep = .1F;
    private boolean fading = false;
    private int WAIT_TIME_IN_MS = 35; //around 12 seconds from -33dB to 6dB


    public Clip(File clipFile) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        audioClip = SoundEnvironments.createClipFromFile(clipFile);

        String filename = clipFile.getName();
        clipName = filename.substring(0, filename.lastIndexOf('.'));
    }

    public void startClip() {
        audioClip.loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY);
    }

    public void stopClip() {
        audioClip.stop();
    }

    public void setVolume(float value) {
        FloatControl volumeControl = (FloatControl) this.getClip().getControl(FloatControl.Type.MASTER_GAIN);
        float range = volumeControl.getMaximum() - volumeControl.getMinimum();
        float gain = (range * value) + volumeControl.getMinimum();
        volumeControl.setValue(gain);
        currDB = gain;
    }


    public void fadeVolumeTo(float to) {
        FloatControl volumeControl = (FloatControl) this.getClip().getControl(FloatControl.Type.MASTER_GAIN);
        float range = volumeControl.getMaximum() - volumeControl.getMinimum();
        float gain = (range * to) + volumeControl.getMinimum();

        targetDB = gain;

        //fade to
        if (!fading) {
            new Thread(this).start();
        }
    }

    public float getGain() {
        return currDB;
    }


    @Override
    public void run() {
        fading = true;   // prevent running twice on same sound

        try {
            while (currDB != targetDB) {
                currDB += fadePerStep * ((currDB > targetDB) ? -1 : 1);
                FloatControl volumeControl = (FloatControl) this.getClip().getControl(FloatControl.Type.MASTER_GAIN);

                volumeControl.setValue(currDB);
                sleep(WAIT_TIME_IN_MS);

                //break if we reach a threshhold
                if (Math.abs(currDB - targetDB) < 0.1) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Clip fading interrupted");
        } finally {
            fading = false;
        }
    }

    private void sleep(int durationInMillis) throws InterruptedException {
        Thread.sleep(durationInMillis);
    }

    public javax.sound.sampled.Clip getClip() {
        return audioClip;
    }

    public String getClipName() {
        return clipName;
    }
}
