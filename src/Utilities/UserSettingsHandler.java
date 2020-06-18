package Utilities;

import java.util.prefs.Preferences;


public class UserSettingsHandler {

    private Preferences userPreferences;
    static final String LAST_SET_ENVIRONMENT = "lastEnvironment";
    public static final String DEFAULT_VALUE = "Beach";
    public static final String MASTER_VOLUME = "masterVolume";
    public static final float DEFAULT_VOLUME = 0.75f;

    public UserSettingsHandler() {
        userPreferences = Preferences.userRoot().node(this.getClass().getName());
    }

    public boolean valueExists(String key) {
        return key.equals(LAST_SET_ENVIRONMENT);
    }

    public void saveVolumeForClip(Clip clip, float volume) {
        userPreferences.putFloat(clip.getClipName(), volume);
    }

    public float getVolumeForClip(Clip clip) {
        return userPreferences.getFloat(clip.getClipName(), DEFAULT_VOLUME);
    }

    public int getMasterVolume() {
        return userPreferences.getInt(MASTER_VOLUME, 100);
    }

    public void setMasterVolume(int volume) {
        userPreferences.putInt(MASTER_VOLUME, volume);
    }

    public void setEnvironment(String environment) {
        userPreferences.put(LAST_SET_ENVIRONMENT, environment);
    }

    public String getLastSetEnvironment() {
        return userPreferences.get(LAST_SET_ENVIRONMENT, DEFAULT_VALUE);
    }

    public void setDefaultEnvironment() {
        userPreferences.put(LAST_SET_ENVIRONMENT, DEFAULT_VALUE);
    }
}
