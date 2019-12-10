package GUI;

import Sound.SoundPlayer;
import Utilities.Clip;
import Utilities.UserSettingsHandler;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class VolumeControlFrame extends JFrame {

    private Frame mainFrame;
    private JPanel panel;

    public VolumeControlFrame(Frame mainFrame) {
        this.mainFrame = mainFrame;
        createGUI();
        createVolumeControls();
        pack();
        setLocationRelativeTo(mainFrame);
    }

    private void createGUI() {
        panel = new JPanel(new GridLayout(3, 2, 8, 8));
        add(panel);
        setVisible(false);
        setResizable(false);
        setAlwaysOnTop(true);
        setTitle("Volume Controls");
    }

    public void refreshControls() {
        panel.removeAll();
        createVolumeControls();
        repaint();
        revalidate();
        pack();
    }

    public void setRandom() {
        for (Component comp : panel.getComponents()) {
            if (comp.getClass() == JLabel.class) {
                ((JLabel) comp).setText("Playing randomly, controls not available.");
            }
        }

        repaint();
        revalidate();
        pack();
    }

    public void showWindow() {
        setVisible(true);
    }

    public void hideWindow() {
        setVisible(false);
    }

    public boolean isShowing() {
        return isVisible();
    }

    public void setAllClipVolumes(int volume) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JSlider) {
                JSlider slider = (JSlider) component;
                slider.setValue(volume);
            }
        }
    }

    private void createVolumeControls() {
        SoundPlayer player = mainFrame.getPlayer();
        Vector<Clip> playingClips = player.getPlayingClips();
        UserSettingsHandler settings = mainFrame.getSettings();

        for (Clip clip : playingClips) {
            javax.sound.sampled.Clip soundClip = clip.getClip();
            JSlider clipSlider = new JSlider(50, 100);

            JLabel label = new JLabel(clip.getClipName(), SwingConstants.CENTER);

            float loadedClipVolume = settings.getVolumeForClip(clip);
            clip.setVolume(loadedClipVolume);

            float convertFloatToInt = loadedClipVolume * 100;
            int loadedClipVolumeInteger = (int) convertFloatToInt;
            clipSlider.setValue(loadedClipVolumeInteger);

            clipSlider.addChangeListener((event) -> {
                float value = (float) clipSlider.getValue() / 100;
                clip.setVolume(value);
                settings.saveVolumeForClip(clip, value);
            });

            panel.add(label);
            panel.add(clipSlider);
        }

        if (playingClips.size() == 0) {
            panel.add(new JLabel("No currently playing clips"));
        }
    }
}
