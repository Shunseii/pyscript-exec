/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pyscriptexec;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 *
 * @author SK-po
 */
public class PyScriptExec {
    
    public static JMenuItem[] fileMenuItems;
    public static JLabel label;
    public static File file;
    public static JFrame frame;
    public static JPanel panel;
    public static JTextField name;
    public static JButton saveButton;
    public static JFileChooser chooser;
    public static final int GUI_WIDTH = 320;
    public static final int GUI_HEIGHT = 150;
    
    public static void main(String[] args) {
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        label = new JLabel("Please load a file from the File menu.");
        frame = new JFrame("PyScriptExec");
        
        createMenu(frame);
        panel.add(label);
        panel.add(name);
        panel.add(saveButton);
        
        name.setEnabled(false);
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
        fileMenuItems = new JMenuItem[2];
        chooser = new JFileChooser(System.getProperty("user.home") + "/Desktop");
        
        //Create JMenuItems for File menu
        fileMenuItems[0] = new JMenuItem("Open");
        fileMenuItems[1] = new JMenuItem("Exit");
        
        
        //Add menu compoenents to JMenuBar and to JMenus
        menuBar.add(fileMenu);
        
        char letter;
        
        for (int i = 0; i < fileMenuItems.length; i++) {
            letter = fileMenuItems[i].getText().charAt(0);
            fileMenuItems[i].setAccelerator(KeyStroke.getKeyStroke(letter, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            fileMenu.add(fileMenuItems[i]);

            if (i == 0) {
                fileMenu.addSeparator();
            }

            fileMenuItems[i].addActionListener(new FileMenuAction());
        }
        
        frame.setJMenuBar(menuBar);
    }
    
    public static class FileMenuAction implements ActionListener {
        
        String absPath, savePath, fileName;
        String[] cmd = new String[2];
        
        @Override
        public void actionPerformed(ActionEvent e) {
            FileNameExtensionFilter filter = new FileNameExtensionFilter(".tsv, .txt", "tsv", "txt");
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileFilter(filter);
            
            switch (e.getActionCommand()) {
            // Program selecting a file and run Python script on the selected file
                case "Open":
                    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        file = chooser.getSelectedFile();
                        absPath = file.getAbsolutePath();
                        
                        label.setText("Please choose a name to save the file as.");
                        saveButton.setEnabled(true);
                        name.setEnabled(true);
                    }
                    break;
            // Program saving file with specified name
                case "Save":
                    cmd = new String[] {
                            "C:/Python27/ArcGIS10.4/python.exe",
                            "Users/SK-po/Desktop/Sufyan Khan/Programming/Java/Projects/PyScriptExec/scripts/myscript.py",
                            /*this.absPath, file.getParent(), 
                            ((JButton)e.getSource()).getText()*/
                        };
                    try {
                        Runtime.getRuntime().exec(cmd);
                        label.setText("Python script successfully run!");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        label.setText("Error loading Python script.");
                    }
                    break;
                default:
                    System.exit(0);
            }
        }
    }
    
}
