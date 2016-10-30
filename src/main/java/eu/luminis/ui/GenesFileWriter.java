package eu.luminis.ui;

import com.google.gson.Gson;
import eu.luminis.Simulation;
import eu.luminis.genetics.Genome;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

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
        PrintWriter out = null;

        try {
            out = new PrintWriter(file);

            List<Genome> genomes = getSimulation().exportPopulation();
            String json = new Gson().toJson(genomes);

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
