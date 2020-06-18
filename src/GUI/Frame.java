package GUI;

import Sound.Environment;
import Sound.SoundEnvironments;
import Sound.SoundPlayer;
import Utilities.Constants;
import Utilities.Mixer;
import Utilities.UserSettingsHandler;
import Utilities.UserTimer;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;



public class Frame extends JFrame {
    private static SoundPlayer player = new SoundPlayer();


    private static Mixer mixer = new Mixer();

    public static void main(String[] args) {
        new Frame();

    }


    private JPanel northPanel, southPanel, centrePanel;
    private VolumeControlFrame volumeControlFrame;
    private AddEnvironmentFrame addEnvironmentFrame;
    private JButton start_sound = new JButton("Start");
    private JButton stop_sound = new JButton("Stop");
    private JButton open_controls = new JButton("Volume settings");
    private JButton random_play = new JButton("Play at random");
    private JButton addEnvironment = new JButton("New environment");
    private JButton setTimerButton = new JButton("Start with timer");
    private JTextField timerTextField = new JTextField(5);
    public static JComboBox<String> environmentSelector = new JComboBox<>();
    private JSlider masterVolumeSlider = new JSlider(50, 100);
    private UserSettingsHandler settings;
    private SoundEnvironments soundEnvironment = new SoundEnvironments();
    private PopupMenu popup = new PopupMenu();
    private MenuItem openItem = new MenuItem("Open");
    private MenuItem exitItem = new MenuItem("Exit");
    private MenuItem startItem = new MenuItem("Start");
    private MenuItem stopItem = new MenuItem("Stop");
    private MenuItem randomItem = new MenuItem("Random");
    private static JLabel userFeedbackLabel = new JLabel("No sounds playing");
    private JLabel checkBoxLabel = new JLabel("Mix All Environments");
    private JCheckBox playAllEnvironments = new JCheckBox();
    private UserTimer userTimer;
    private Timer timer;
    private JLabel timeViewer;



    public Frame() {
        settings = new UserSettingsHandler();
        volumeControlFrame = new VolumeControlFrame(this);
        addEnvironmentFrame = new AddEnvironmentFrame(this);
        userTimer = new UserTimer(0, this, player);
        createFrame();
        addActionListeners();
    }

    private void createFrame() {
        setIconImage(Constants.ICON.getImage());
        northPanel = new JPanel();
        southPanel = new JPanel();
        centrePanel = new JPanel();
        JLabel EnvironmentChooser = new JLabel("Choose an environment:");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(1000, 150);
        setLayout(new BorderLayout());
        setResizable(false);
        add(northPanel, BorderLayout.NORTH);
        add(centrePanel, BorderLayout.CENTER);
        centrePanel.add(userFeedbackLabel);
        southPanel.add(EnvironmentChooser);
        add(southPanel, BorderLayout.SOUTH);
        CreateJComboBox();
        createButtons();
        initMasterVolume();
        setTitle("Soundscape Generator");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        createSystemTray();

        timeViewer = new JLabel("Time: ");
        southPanel.add(timeViewer);
    }

    private void createButtons() {
        northPanel.add(random_play);
        northPanel.add(playAllEnvironments);
        northPanel.add(checkBoxLabel);
        northPanel.add(start_sound);
        northPanel.add(stop_sound);
        JLabel volume = new JLabel("Master Volume:");
        northPanel.add(volume);
        northPanel.add(masterVolumeSlider);
        JLabel setTimerMinutes = new JLabel("Enter time (in minutes)");
        northPanel.add(setTimerMinutes);
        northPanel.add(timerTextField);
        northPanel.add(setTimerButton);
        southPanel.add(open_controls);
        southPanel.add(addEnvironment);

        setComponentEditable(false,stop_sound,open_controls,masterVolumeSlider);
        setComponentEditable(true,start_sound,random_play,setTimerButton);
    }

    public void populateComboBox() {
        environmentSelector.removeAllItems();

        java.util.List<String> environments = SoundEnvironments.getEnvironments();


        for (String environment : environments) {
            environmentSelector.addItem(environment);
        }
    }

    private void initMasterVolume() {
        int masterVolume = settings.getMasterVolume();
        masterVolumeSlider.setValue(masterVolume);
    }

    private void CreateJComboBox() {
        populateComboBox();
        String lastSelectedEnvironment = settings.getLastSetEnvironment();
        environmentSelector.setSelectedItem(lastSelectedEnvironment);
        setEnvironment(SoundEnvironments.loadEnvironment(lastSelectedEnvironment));
        environmentSelector.setBounds(50, 50, 50, 50);
        southPanel.add(environmentSelector);
    }

    private void createSystemTray() {
        TrayIcon trayIcon;
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image icon = Constants.SYS_TRAY_ICON;
            popup.add(openItem);
            popup.add(startItem);
            popup.add(randomItem);
            popup.add(stopItem);
            popup.add(exitItem);
            trayIcon = new TrayIcon(icon, "SoundscapeGenerator", popup);
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
        }
    }

    public void showUserMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public JComboBox getEnvironmentSelector() {
        return environmentSelector;
    }

    public UserSettingsHandler getSettings() {
        return settings;
    }

    public SoundPlayer getPlayer() {
        return player;
    }
    public JTextField getTimerTextField(){
        return timerTextField;
    }

    public void setComponentEditable(boolean editable, JComponent ... components){
        for (JComponent component : components){
            component.setEnabled(editable);
        }
    }

    public Timer createTimer(){
        return new Timer();
    }

    public Timer getTimer() {
        return timer;
    }

    public JButton getSetTimerButton(){
        return setTimerButton;
    }

    public double getUserInput() {
        return Double.parseDouble(timerTextField.getText());
    }

    public JButton getStart_sound(){
        return start_sound;
    }

    public JButton getStop_sound(){
        return stop_sound;
    }

    public UserTimer createUserTimer() {
        userTimer = new UserTimer(getUserInput(), this, player);
        return userTimer;
    }

    public UserTimer getUserTimer() {
        return userTimer;
    }

    public static Mixer getMixer() {
        return mixer;
    }

    public static void setUserFeedbackLabel(String value, Color colour) {
        userFeedbackLabel.setForeground(colour);
        userFeedbackLabel.setText(value);
    }

    public VolumeControlFrame getVolumeControlFrame() {
        return volumeControlFrame;
    }

    public AddEnvironmentFrame getAddEnvironmentFrame() {
        return addEnvironmentFrame;
    }

    public void setEnvironment(Environment environment) {
        if (environment.getClips().isEmpty()) {
            showUserMessage("Empty environment", "The environment selected is empty. Please try again with a different environment");
        } else {
            Object selectedEnvironment = environmentSelector.getSelectedItem();
            if (selectedEnvironment != null) {
                getSettings().setEnvironment(selectedEnvironment.toString());
                getPlayer().setEnvironment(environment);
                getMixer().stopMixer();
                getMixer().setEnvironment(environment);
            }
        }
    }

    public void removeComboActionListener() {
        environmentSelector.removeActionListener(environmentSelector.getActionListeners()[0]);
    }

    private void randomPlayHandler() {
        volumeControlFrame.setRandom();
        getPlayer().stopPlayingAllSounds();

        boolean isCheckboxSelected = playAllEnvironments.isSelected();
        getMixer().startMixer(isCheckboxSelected);
        if (isCheckboxSelected) {
            setUserFeedbackLabel("Playing environments (randomly)", Color.BLACK);
        } else {
            String currentEnvironment = String.format("Playing %s environment randomly", environmentSelector.getSelectedItem().toString());
            setUserFeedbackLabel(currentEnvironment, Color.BLACK);
        }

        setComponentEditable(false,masterVolumeSlider);
    }



    public void addActionListeners() {

        environmentSelector.addActionListener(e -> {
            Object environment = environmentSelector.getSelectedItem();
            if (environment != null) {
                String selectedEnvironment = environmentSelector.getSelectedItem().toString();
                setEnvironment(SoundEnvironments.loadEnvironment(selectedEnvironment));

                getVolumeControlFrame().refreshControls();
            }
        });

        start_sound.addActionListener(e -> {
            setComponentEditable(false,start_sound,random_play,setTimerButton);
            setComponentEditable(true,stop_sound,open_controls,masterVolumeSlider);
            getPlayer().playSounds();
            getVolumeControlFrame().refreshControls();
            setUserFeedbackLabel("Playing " + getEnvironmentSelector().getSelectedItem() + " environment", Color.BLACK);
        });

        stop_sound.addActionListener(e -> {
            setComponentEditable(false,stop_sound,open_controls);
            setComponentEditable(true,start_sound,random_play,setTimerButton,masterVolumeSlider);
            getPlayer().stopPlayingAllSounds();
            getVolumeControlFrame().refreshControls();
            getMixer().stopMixer();

            this.getUserTimer().running = false;

            setUserFeedbackLabel("No sound playing", Color.BLACK);
        });


        setTimerButton.addActionListener(e ->{
            setComponentEditable(false,start_sound,random_play,setTimerButton);
            setComponentEditable(true,masterVolumeSlider,stop_sound,open_controls);
            if (timerTextField.getText().equals("")){

                setUserFeedbackLabel("Please enter a valid value into the timer field", Color.RED);
                setComponentEditable(false,stop_sound,open_controls,masterVolumeSlider);
                setComponentEditable(true,start_sound,random_play,setTimerButton);
                getPlayer().playSounds();
            } else {
                try {
                    // Throw an exception if the input is invalid or smaller than 1
                    timer = new Timer();
                    if (getUserInput() < 1) {
                        throw new Exception();
                    }
                    setUserFeedbackLabel("Playing " + getEnvironmentSelector().getSelectedItem() + " environment (timer)", Color.BLACK);

                    this.getTimer().schedule(createUserTimer(), 0, 1000);
                    getPlayer().playSounds();
                    this.getUserTimer().running = true;
                    getVolumeControlFrame().refreshControls();
                }

                //display invalid input message
                catch (Exception ew) {
                    setUserFeedbackLabel("Please enter a valid value into the timer field",Color.RED);
                    setComponentEditable(false,stop_sound,open_controls,masterVolumeSlider);
                    setComponentEditable(true,start_sound,random_play,setTimerButton);
                    timerTextField.setText("");
                }
            }
        });

        masterVolumeSlider.addChangeListener(e -> {
            float masterSoundValue = (float) masterVolumeSlider.getValue() / 100;
            getPlayer().changeMasterVolume(masterSoundValue);
            getSettings().setMasterVolume(masterVolumeSlider.getValue());

            getVolumeControlFrame().setAllClipVolumes(masterVolumeSlider.getValue());

        });

        open_controls.addActionListener((event) -> {
            VolumeControlFrame volumeControlFrame = getVolumeControlFrame();

            if (volumeControlFrame.isShowing()) {
                volumeControlFrame.hideWindow();
            } else {
                volumeControlFrame.showWindow();
            }
        });

        addEnvironment.addActionListener((event) -> {
            AddEnvironmentFrame addEnvironmentFrame = getAddEnvironmentFrame();

            if (addEnvironmentFrame.isShowing()) {
                addEnvironmentFrame.hideWindow();
            } else {
                addEnvironmentFrame.showWindow();
            }
        });

        openItem.addActionListener(e -> {
            setVisible(true);
        });

        exitItem.addActionListener(e -> {
            System.exit(0);
        });

        startItem.addActionListener(e -> {
            getPlayer().playSounds();
            setComponentEditable(false,start_sound,random_play,setTimerButton);
            setComponentEditable(true,stop_sound,open_controls,masterVolumeSlider);
        });

        stopItem.addActionListener(e -> {
            getPlayer().stopPlayingAllSounds();
            getMixer().setAllClipVolumes(0);
            getMixer().stopMixer();
            setComponentEditable(false,stop_sound,open_controls);
            setComponentEditable(true,start_sound,random_play,setTimerButton,masterVolumeSlider);

        });

        randomItem.addActionListener(e -> {
            setComponentEditable(false,start_sound,open_controls,random_play,setTimerButton);
            setComponentEditable(true,stop_sound);
            randomPlayHandler();});

        random_play.addActionListener(e ->{
            setComponentEditable(false,start_sound,open_controls,random_play,setTimerButton);
            setComponentEditable(true,stop_sound);
            randomPlayHandler();
        });
    }

    public JLabel getTimeViewer() {
        return timeViewer;
    }


}