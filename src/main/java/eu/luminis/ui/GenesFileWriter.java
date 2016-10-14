package eu.luminis.ui;

import eu.luminis.general.Simulation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

class GenesFileWriter extends GenesFileActionListener {
    public GenesFileWriter(Component component, Simulation simulation) {
        super(component, simulation);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File file = getFileToSave();

        if (file == null) return;

        exportFile(file);
    }

    private void exportFile(File file) {
        String json = getSimulation().exportPopulation();
        PrintWriter out = null;

        try {
            out = new PrintWriter(file);
            out.println(json);
        }
        catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
