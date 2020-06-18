package Sound;

import Utilities.Clip;

import java.io.File;
import java.util.List;
import java.util.Vector;

public class Environment {
    private Vector<Clip> clips;
    private String envName;

    public Environment(String name, List<String> clipPaths) {
        clips = new Vector<>();
        envName = name;
        try {

            for (String clipPath : clipPaths) {
                if (clipPath.endsWith(".wav")) {
                    clips.add(new Clip(new File(clipPath)));
                    System.out.println("Added: " + clipPath);
                } else {
                    System.out.println(clipPath + " is not a .wav file!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vector<Clip> getClips() {
        return clips;
    }

    public String getEnvName() {
        return envName;
    }
}
