/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pyscriptexec;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 *
 * @author SK-po
 */
public class PyScriptExec {
    
    public static JMenuItem[] fileMenuItems;
    public static JLabel label, pythonLoad;
    public static File file, pyFile;
    public static JFrame frame;
    public static JPanel panel;
    public static JTextField name;
    public static JButton saveButton;
    public static JFileChooser pyChooser, chooser;
    public static final int GUI_WIDTH = 320;
    public static final int GUI_HEIGHT = 150;
    
    public static void main(String[] args) {
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        label = new JLabel("Please select your Python installation directory.");
        frame = new JFrame("PyScriptExec");
        
        createMenu(frame);
        panel.add(label);
        panel.add(name);
        panel.add(saveButton);
        
        name.setEnabled(false);
        fileMenuItems[1].setEnabled(false);
        saveButton.setEnabled(false);
        saveButton.setActionCommand("Save");
        saveButton.addActionListener(new FileMenuAction());
        
        frame.add(panel);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(GUI_WIDTH, GUI_HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }
    
    public static void createMenu(JFrame frame) {
        //Create JMenuBar and JMenuBar components
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        saveButton = new JButton("Save As");
        name = new JTextField("File Name", 15);
        fileMenuItems = new JMenuItem[3];
        chooser = new JFileChooser(System.getProperty("user.home") + "/Desktop");
        pyChooser = new JFileChooser();
        
        //Create JMenuItems for File menu
        fileMenuItems[0] = new JMenuItem("Python Directory");
        fileMenuItems[1] = new JMenuItem("Choose File");
        fileMenuItems[2] = new JMenuItem("Exit");
        
        
        //Add menu compoenents to JMenuBar and to JMenus
        menuBar.add(fileMenu);
        
        char letter;
        
        for (int i = 0; i < fileMenuItems.length; i++) {
            letter = fileMenuItems[i].getText().charAt(0);
            fileMenuItems[i].setAccelerator(KeyStroke.getKeyStroke(letter, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            fileMenu.add(fileMenuItems[i]);

            if (i == fileMenuItems.length - 1) {
                fileMenu.addSeparator();
            }

            fileMenuItems[i].addActionListener(new FileMenuAction());
        }
        
        frame.setJMenuBar(menuBar);
    }
    
    public static class FileMenuAction implements ActionListener {
        
        static String absPath, pythonDir;
        
        @Override
        public void actionPerformed(ActionEvent e) {
            FileNameExtensionFilter filter = new FileNameExtensionFilter(".tsv, .txt", "tsv", "txt");
            FileNameExtensionFilter pyFilter = new FileNameExtensionFilter(".exe", "exe");
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileFilter(filter);
            pyChooser.setAcceptAllFileFilterUsed(false);
            pyChooser.setFileFilter(pyFilter);
            
            switch (e.getActionCommand()) {
            // Program selects the specified file
                case "Choose File":
                    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        file = chooser.getSelectedFile();
                        absPath = file.getAbsolutePath();
                        label.setText("Please choose a name to save the file as.");
                        saveButton.setEnabled(true);
                        name.setEnabled(true);
                    }
                    break;
            // Program calls Python script
                case "Save":
                    try {
                        URL resource = PyScriptExec.class.getResource("../scripts/FIJIExcel.py");
                        File scriptFile =  Paths.get(resource.toURI()).toFile();
                        String scriptAddr = scriptFile.getAbsolutePath();
                        
                        ProcessBuilder pb = new ProcessBuilder(
                                pythonDir, scriptAddr, absPath, 
                                name.getText(), file.getParent());
                        Process p = pb.start();
                        /*
                        BufferedReader er = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

                        System.out.println("Output: " + in.readLine());
                        System.out.println("Errors: " + er.readLine());
                        */
                        label.setText("Python script successfully run!");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        label.setText("Error loading Python script.");
                    }
                    break;
                case "Python Directory":
                    if (pyChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        pyFile = pyChooser.getSelectedFile();
                        pythonDir = pyFile.getAbsolutePath();
                        label.setText("Please load a file from the File menu.");
                        fileMenuItems[1].setEnabled(true);
                    }
                    break;
                default:
                    System.exit(0);
            }
        }
    }
    
}
