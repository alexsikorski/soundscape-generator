package Utilities;

import GUI.Frame;
import Sound.SoundPlayer;

import java.util.Timer;
import java.util.TimerTask;


public class UserTimer extends TimerTask {
    double userTime;
    private int startTime;
    public boolean running = true;
    Frame frame;
    SoundPlayer player;

    Timer timer = new Timer();

    public UserTimer(double seconds, Frame frame, SoundPlayer player) {
        this.userTime = seconds * 60;
        this.frame = frame;
        this.player = player;
        startTime = 0;
    }

    @Override
    public void run() {
        if (running) {
            frame.getTimeViewer().setText("Time: " + startTime++);
            if (startTime > userTime) {
                running = false;
            }
        } else {
            player.stopPlayingAllSounds();
            frame.getTimeViewer().setText("Time: ");
            frame.getTimer().cancel();
        }
        frame.repaint();
    }

}