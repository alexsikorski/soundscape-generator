package Sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SoundEnvironments {

    public static List<String> getEnvironments() {
        File appDir = new File("./sounds");

        File[] filesAndDirectories = appDir.listFiles();

        ArrayList<String> environmentDirectories = new ArrayList<>();

        if (filesAndDirectories != null) {
            for (File f : filesAndDirectories) {
                if (f.isDirectory()) {
                    environmentDirectories.add(f.getName());
                }
            }
        }

        return environmentDirectories;
    }

    public static List<String> getClipPaths(String environment) {
        //return string array of all clip paths inside environment folder
        File envDir = new File("sounds/" + environment);

        List<String> clipPathLists = new ArrayList<>();

        File[] filesInDirectory = envDir.listFiles();
        if (filesInDirectory != null) {
            for (File f : filesInDirectory) {
                clipPathLists.add(f.getAbsolutePath());
            }
        }
        return clipPathLists;
    }

    public static Environment loadEnvironment(String environment) {
        return new Environment(environment, getClipPaths(environment));
    }

    public static javax.sound.sampled.Clip createClipFromFile(File file) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        javax.sound.sampled.Clip clip = AudioSystem.getClip();
        AudioInputStream ais = AudioSystem.getAudioInputStream(file);
        clip.open(ais);

        return clip;
    }

}

