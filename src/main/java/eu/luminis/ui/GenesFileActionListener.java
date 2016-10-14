package eu.luminis.ui;

import eu.luminis.general.Simulation;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

abstract class GenesFileActionListener implements ActionListener {
    private static final String FILE_EXTENSION = "json";

    private JFileChooser fileChooser;
    private Component component;
    private Simulation simulation;

    protected GenesFileActionListener(Component component, Simulation simulation) {
        this.component = component;
        this.simulation = simulation;

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("*." + FILE_EXTENSION, FILE_EXTENSION));
    }

    @Override
    public abstract void actionPerformed(ActionEvent e);

    public Simulation getSimulation() {
        return simulation;
    }

    protected File getFileToSave() {
        int returnVal = fileChooser.showSaveDialog(component);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }

        throw new Error("No file to save");
    }

    protected File getFileToOpen() {
        int returnVal = fileChooser.showOpenDialog(component);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }

        throw new Error("No file to open");
    }
}