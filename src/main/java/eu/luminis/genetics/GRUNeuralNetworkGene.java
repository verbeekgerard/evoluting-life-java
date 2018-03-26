package eu.luminis.genetics;

import eu.luminis.*;
import java.util.ArrayList;
import java.util.List;

public class GRUNeuralNetworkGene {

    private final List<GRULayerGene> layers = new ArrayList<>();

    GRUNeuralNetworkGene(List<GRULayerGene> stateLayers) {
        for (GRULayerGene stateLayer : stateLayers) {
            layers.add(
                new GRULayerGene(
                    stateLayer.getUpdateLayerGene(),
                    stateLayer.getResetLayerGene(),
                    stateLayer.getOutputLayerGene()));
        }
    }

    public void mutate() {
        for (GRULayerGene layer : layers) {
            layer.mutate();
        }

        if (Math.random() < Options.neuralNetworkMutationRate.get()) {
            int newIndex = (int)Math.floor(layers.size() * Math.random());
            int size = layers.get(newIndex).getUpdateLayerGene().getColumns();
            GRULayerGene newLAyer = GRULayerGeneBuilder.create()
                .withSize(size)
                .build();
            layers.add(newIndex, newLAyer);
        }
    }

    public List<GRUNeuralNetworkGene> mate(GRUNeuralNetworkGene partner) {
        List<GRULayerGene> smallest = this.layers.size() < partner.layers.size() ? this.layers : partner.layers;
        List<GRULayerGene> largest = this.layers.size() < partner.layers.size() ? partner.layers : this.layers;

        List<GRUNeuralNetworkGene> children = new ArrayList<>();
        children.add(new GRUNeuralNetworkGene(new ArrayList<>()));
        children.add(new GRUNeuralNetworkGene(new ArrayList<>()));

        for(int i=0; i<smallest.size(); i++) {
            List<GRULayerGene> childLayers = largest.get(i).mate(smallest.get(i));
            for (int k = 0; k < 2; k++) {
                children.get(k).layers.add(childLayers.get(k));
            }
        }

        for(int i=smallest.size(); i<largest.size(); i++) {
            GRULayerGene layer = largest.get(i);
            children.get(0).layers.add(layer.Clone());
        }

        return children;
    }

    public List<GRULayerGene> getLayers() {
        return layers;
    }
}
