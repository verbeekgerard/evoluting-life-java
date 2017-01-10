package eu.luminis.export;

import com.google.gson.Gson;
import eu.luminis.Simulation;
import eu.luminis.ui.GenesFileActionListener;

import java.awt.*;
import java.awt.event.ActionEvent;

public class PopulationExporter extends GenesFileActionListener {
    public PopulationExporter(Component component, Simulation simulation) {
        super(component, simulation);
    }

    public String export() {
        return new Gson().toJson(getSimulation().exportPopulation());
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
