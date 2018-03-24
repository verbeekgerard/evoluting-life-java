package eu.luminis.genetics;

import eu.luminis.*;
import java.util.ArrayList;
import java.util.List;

public class NeuralNetworkGene {

    private final List<GRULayerGene> layers = new ArrayList<>();

    NeuralNetworkGene(List<GRULayerGene> stateLayers) {
        for (GRULayerGene stateLayer : stateLayers) {
            layers.add(
                new GRULayerGene(
                    stateLayer.getGz(),
                    stateLayer.getGr(),
                    stateLayer.getGh()));
        }
    }

    public void mutate() {
        for (GRULayerGene layer : layers) {
            layer.mutate();
        }

        if (Math.random() < Options.neuralNetworkMutationRate.get()) {
            int newIndex = (int)Math.floor(layers.size() * Math.random());
            int size = layers.get(newIndex).getGz().getColumns();
            GRULayerGene newLAyer = GRULayerGeneBuilder.create()
                .withSize(size)
                .build();
            layers.add(newIndex, newLAyer);
        }
    }

    public List<NeuralNetworkGene> mate(NeuralNetworkGene partner) {
        List<GRULayerGene> smallest = this.layers.size() < partner.layers.size() ? this.layers : partner.layers;
        List<GRULayerGene> largest = this.layers.size() < partner.layers.size() ? partner.layers : this.layers;

        List<NeuralNetworkGene> children = new ArrayList<>();
        children.add(new NeuralNetworkGene(new ArrayList<>()));
        children.add(new NeuralNetworkGene(new ArrayList<>()));

        for(int i=0; i<smallest.size(); i++) {
            List<GRULayerGene> childLayers = largest.get(i).mate(smallest.get(i));
            for (int k = 0; k < 2; k++) {
                children.get(k).layers.add(childLayers.get(k));
            }
        }

        for(int i=smallest.size(); i<largest.size(); i++) {
            GRULayerGene layer = largest.get(i);
            children.get(0).layers.add(
                new GRULayerGene(
                    layer.getGz(), 
                    layer.getGr(),
                    layer.getGh()));
        }

        return children;
    }

    public List<GRULayerGene> getLayers() {
        return layers;
    }
}