package eu.luminis.ui;

import eu.luminis.Simulation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

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
            String json = new String(readAllBytes(get(file.getPath())));
            getSimulation().importPopulation(json);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
