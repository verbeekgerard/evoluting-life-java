package eu.luminis.ui;

import eu.luminis.Simulation;
import eu.luminis.genetics.Genome;
import eu.luminis.util.GenesFile;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

class GenesFileReader extends GenesFileActionListener {

    public GenesFileReader(Component component, Simulation simulation) {
        super(component, simulation);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File file = getFileToOpen();

        if (file == null) return;

        importFile(file);
    }

    private void importFile(File file) {
        try {
            GenesFile genesFile = new GenesFile(file.getPath());
            List<Genome> genomes = genesFile.read();

            getSimulation().importPopulation(genomes);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
