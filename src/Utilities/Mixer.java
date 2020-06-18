package Utilities;

import GUI.Frame;
import Sound.Environment;
import Sound.SoundEnvironments;

import java.util.*;

public class Mixer {
    static final int TOTAL_DURATION = 50;
    static final int FADE_OUT_TIME = TOTAL_DURATION;
    static final double RANDOMIZE_RANGE = TOTAL_DURATION;
    public static ArrayList<String> environmentPlayOrder = new ArrayList<>();
    HashMap<Clip, Integer> fadeInTimings = new HashMap<>();
    HashMap<Clip, Integer> fadeOutTimings = new HashMap<>();

    static int fadeInOrOut;
    static int count = 0;
    private Timer mixerTimer;
    private TimerTask timerTask;
    private boolean mixerRunning = false;

    private static int genRandom() {
        double randomizer = Math.random();
        randomizer = randomizer * RANDOMIZE_RANGE;
        int randomTime = (int) randomizer;
        return randomTime;
    }

    static HashMap<Clip, Integer> generateRandomTimings(Vector<Clip> environmentClips) {
        HashMap<Clip, Integer> clipTiming = new HashMap<>();
        try {
            for (Clip clip : environmentClips) {
                clipTiming.put(clip, genRandom());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return clipTiming;
    }

    static void setOneClipToStartInstantly(Map<Clip, Integer> clipMap) {
        Clip randomClip = getRandomClipFromMap(clipMap);
        clipMap.put(randomClip, 1);
    }

    static Clip getRandomClipFromMap(Map<Clip, Integer> clipMap) {
        Clip clip = null;
        Random r = new Random();
        int randomNumber = 0;

        int bound = clipMap.size() > 2 ? clipMap.size() : 1;
        randomNumber = r.nextInt(bound);

        if (randomNumber == 0) {
            randomNumber = 1;
        }

        for (int i = 0; i < randomNumber; i++) {
            clip = clipMap.keySet().iterator().next();
        }


        return clip;
    }

    static void setOneClipToFinishLast(Map<Clip, Integer> clipMap) {
        Clip randomClip = getRandomClipFromMap(clipMap);
        clipMap.put(randomClip, TOTAL_DURATION - 1);
    }

    private static void setEnvironmentOrder() {
        environmentPlayOrder.clear();
        List<String> environments = SoundEnvironments.getEnvironments();
        environmentPlayOrder.addAll(environments);
        System.out.println("UNSHUFFLED " + environmentPlayOrder);
        Collections.shuffle(environmentPlayOrder);
        System.out.println("SHUFFLED " + environmentPlayOrder);
    }

    public void stopMixer() {
        if (mixerTimer != null) {
            mixerTimer.cancel();
            timerTask.cancel();
            mixerTimer.purge();

            mixerRunning = false;

            count = 0;

            for (Clip clip : fadeInTimings.keySet()) {
                clip.stopClip();
            }
        }
    }

    public void startMixer(boolean playAllEnvironments) {
        if (!mixerRunning) {
            if (playAllEnvironments) {
                loadRandomEnv();
            } else {
                Object selectedItem = Frame.environmentSelector.getSelectedItem();

                if (selectedItem != null) {
                    Environment env = SoundEnvironments.loadEnvironment(selectedItem.toString());
                    setEnvironment(env);
                }
            }


            setAllClipVolumes(0.3f);

            mixerRunning = true;
            int delay = 0;
            int period = 1000;
            mixerTimer = new Timer();
            timerTask = new SoundMixerTask(this, playAllEnvironments);
            mixerTimer.scheduleAtFixedRate(timerTask, delay, period);
        }
    }

    public void setAllClipVolumes(float vol) {
        if (vol > 1) {
            vol = 1;
        }
        if (vol < 0) {
            vol = 0;
        }

        for (Clip clip : fadeInTimings.keySet()) {
            clip.setVolume(vol);
        }
    }

    private void showClipTimings() {
        for (Map.Entry clip : fadeInTimings.entrySet()) {
            System.out.println(clip.getKey() + "\t" + clip.getValue());
        }
        for (Map.Entry clip : fadeOutTimings.entrySet()) {
            System.out.println(clip.getKey() + "\t" + clip.getValue());
        }
    }

    public void setEnvironment(Environment environment) {
        try {
            fadeInTimings = generateRandomTimings(environment.getClips());
            fadeOutTimings = generateRandomTimings(environment.getClips());

            setOneClipToStartInstantly(fadeInTimings);
            setOneClipToFinishLast(fadeOutTimings);

            setAllClipVolumes(0.4f);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadRandomEnv() {
        setEnvironmentOrder();
        String firstEnv = environmentPlayOrder.get(0);
        System.out.println(firstEnv);
        setEnvironment(SoundEnvironments.loadEnvironment(environmentPlayOrder.get(0)));
        showClipTimings();
    }

    public HashMap<Clip, Integer> getFadeInTimings() {
        return fadeInTimings;
    }

    public HashMap<Clip, Integer> getFadeOutTimings() {
        return fadeOutTimings;
    }
}

class SoundMixerTask extends TimerTask {
    private Mixer mixer;
    private boolean playAllEnvironments;

    SoundMixerTask(Mixer mixer, boolean playAllEnvironments) {
        this.mixer = mixer;
        this.playAllEnvironments = playAllEnvironments;
    }

    @Override
    public void run() {
        if (Mixer.fadeInOrOut == 0) {
            {
                System.out.println("Fading in..." + Mixer.count + "s");
                for (Clip clip : mixer.fadeInTimings.keySet()) {
                    Integer clipValue = mixer.fadeInTimings.get(clip);
                    if (clipValue == Mixer.count) {
                        System.out.println("PLAYING CLIP");
                        clip.startClip();
                        clip.fadeVolumeTo(1f);
                    }
                }
                if (Mixer.count == Mixer.FADE_OUT_TIME) {
                    Mixer.fadeInOrOut = 1;
                    Mixer.count = 0;
                }
            }
        }
        if (Mixer.fadeInOrOut == 1) {
            {
                System.out.println("Fading out..." + Mixer.count + "s");
                for (Clip clip : mixer.fadeOutTimings.keySet()) {
                    Integer clipValue = mixer.fadeOutTimings.get(clip);
                    if (clipValue == Mixer.count) {
                        System.out.println("STOPPING CLIP");
                        clip.fadeVolumeTo(0f); // volume down 0
                    }
                }
                if (Mixer.count == Mixer.TOTAL_DURATION) {
                    restartTimer();
                }
            }
        }
        Mixer.count++;
    }


    private void restartTimer() {
        //restart when limit reached and regen random

        if (playAllEnvironments) {
            mixer.loadRandomEnv();
        }
        mixer.count = 0;
        mixer.fadeInOrOut = 0;
        Vector<Clip> fadeInClips = new Vector<>(mixer.fadeInTimings.keySet());
        mixer.fadeInTimings = Mixer.generateRandomTimings(fadeInClips);

        Vector<Clip> fadeOutClips = new Vector<>(mixer.fadeOutTimings.keySet());
        mixer.fadeOutTimings = Mixer.generateRandomTimings(fadeOutClips);

        Mixer.setOneClipToStartInstantly(mixer.fadeInTimings);
        Mixer.setOneClipToFinishLast(mixer.fadeOutTimings);
    }
}
