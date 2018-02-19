package eu.luminis.genetics;

import eu.luminis.*;
import java.util.ArrayList;
import java.util.List;

public class NeuralNetworkGene {

    private final List<LayerGene> layers = new ArrayList<>();

    NeuralNetworkGene(List<LayerGene> stateLayers) {
        for (LayerGene stateLayer : stateLayers) {
            layers.add(
                new LayerGene(
                    stateLayer.getWeights().clone(),
                    stateLayer.getBiases().clone()));
        }
    }

    public void mutate() {
        for (LayerGene layer : layers) {
            layer.mutate();
        }

        if (Math.random() < Options.neuralNetworkMutationRate.get()) {
            int newIndex = (int)Math.floor(layers.size() * Math.random());
            int size = layers.get(newIndex).getColumns();
            LayerGene newLAyer = LayerGeneBuilder.create()
                .withSize(size)
                .build();
            layers.add(newIndex, newLAyer);
        }
    }

    public List<NeuralNetworkGene> mate(NeuralNetworkGene partner) {
        List<LayerGene> smallest = this.layers.size() < partner.layers.size() ? this.layers : partner.layers;
        List<LayerGene> largest = this.layers.size() < partner.layers.size() ? partner.layers : this.layers;

        List<NeuralNetworkGene> children = new ArrayList<>();
        children.add(new NeuralNetworkGene(new ArrayList<>()));
        children.add(new NeuralNetworkGene(new ArrayList<>()));

        for(int i=0; i<smallest.size(); i++) {
            LayerGene[] childLayers = largest.get(i).mate(smallest.get(i));
            for (int k = 0; k < 2; k++) {
                children.get(k).layers.add(childLayers[k]);
            }
        }

        for(int i=smallest.size(); i<largest.size(); i++) {
            LayerGene layer = largest.get(i);
            children.get(0).layers.add(
                new LayerGene(
                    layer.getWeights().clone(), 
                    layer.getBiases().clone()));
        }

        return children;
    }

    public List<LayerGene> getLayers() {
        return layers;
    }
}