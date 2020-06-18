package GUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class AddEnvironmentFrame extends JFrame {
    private Frame mainFrame;
    private JPanel panel;
    private String pathName = "";
    private String fileName = "";
    private JTextArea chosenSounds = new JTextArea("");

    public AddEnvironmentFrame(Frame mainFrame) {
        this.mainFrame = mainFrame;
        GUI(mainFrame);
    }

    private void GUI(Frame mainFrame) {
        panel = new JPanel();
        setVisible(false);
        setResizable(true);
        setAlwaysOnTop(true);
        setTitle("Add new environment");
        setSize(400, 500);
        JLabel enterEnvironmentName = new JLabel("Environment name:");
        JTextField nameInput = new JTextField(20);
        JLabel addSound = new JLabel("Add sound:");
        JButton browse = new JButton("Browse files");
        JButton saveEnvironment = new JButton("Save environment");
        panel.add(enterEnvironmentName);
        panel.add(nameInput);
        panel.add(addSound);
        panel.add(browse);
        panel.add(chosenSounds);
        panel.add(saveEnvironment);
        add(panel);

        browse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    ".wav files", "wav");
            chooser.setFileFilter(filter);
            chooser.setAcceptAllFileFilterUsed(false);
            int returnVal = chooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                chosenSounds.setText(chosenSounds.getText() + selectedFile + "\n");
                fileName += chooser.getSelectedFile().getName() + ",";
            }
        });
        saveEnvironment.addActionListener(e -> {
            String x = nameInput.getText();
            pathName = "." + File.separator + "sounds" + File.separator + x + File.separator;
            System.out.println(pathName);

            File file = new File(pathName);
            if (!file.exists()) {
                if (file.mkdir()) {
                    System.out.println("Folder was created!");
                } else {
                    System.out.println("Failed to create the folder");
                }
            }
            String[] arr = chosenSounds.getText().split("\n");
            String[] fileNameArr = fileName.split(",");
            for (int i = 0; i < arr.length; i++) {
                try {
                    File source = new File(arr[i]);
                    File dest = new File(pathName + fileNameArr[i]);
                    Files.copy(source.toPath(), dest.toPath());
                } catch (IOException o) {
                    System.out.println("File not found");
                }
                mainFrame.removeComboActionListener();
                mainFrame.populateComboBox();
                mainFrame.addActionListeners();
            }
        });
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
}
