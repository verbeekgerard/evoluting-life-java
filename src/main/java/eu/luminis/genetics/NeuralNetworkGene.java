package eu.luminis.genetics;

import eu.luminis.*;
import java.util.ArrayList;
import java.util.List;

public class NeuralNetworkGene {

    private final List<SRNNLayerGene> layers = new ArrayList<>();

    NeuralNetworkGene(List<SRNNLayerGene> stateLayers) {
        for (SRNNLayerGene stateLayer : stateLayers) {
            layers.add(
                new SRNNLayerGene(
                    stateLayer.getWeights().clone(),
                    stateLayer.getBiases().clone(),
                    stateLayer.getStateWeights().clone(),
                    stateLayer.getGains().clone()));
        }
    }

    public void mutate() {
        for (SRNNLayerGene layer : layers) {
            layer.mutate();
        }

        if (Math.random() < Options.neuralNetworkMutationRate.get()) {
            int newIndex = (int)Math.floor(layers.size() * Math.random());
            int size = layers.get(newIndex).getColumns();
            SRNNLayerGene newLAyer = SRNNLayerGeneBuilder.create()
                .withSize(size)
                .build();
            layers.add(newIndex, newLAyer);
        }
    }

    public List<NeuralNetworkGene> mate(NeuralNetworkGene partner) {
        List<SRNNLayerGene> smallest = this.layers.size() < partner.layers.size() ? this.layers : partner.layers;
        List<SRNNLayerGene> largest = this.layers.size() < partner.layers.size() ? partner.layers : this.layers;

        List<NeuralNetworkGene> children = new ArrayList<>();
        children.add(new NeuralNetworkGene(new ArrayList<>()));
        children.add(new NeuralNetworkGene(new ArrayList<>()));

        for(int i=0; i<smallest.size(); i++) {
            SRNNLayerGene[] childLayers = largest.get(i).mate(smallest.get(i));
            for (int k = 0; k < 2; k++) {
                children.get(k).layers.add(childLayers[k]);
            }
        }

        for(int i=smallest.size(); i<largest.size(); i++) {
            SRNNLayerGene layer = largest.get(i);
            children.get(0).layers.add(
                new SRNNLayerGene(
                    layer.getWeights().clone(), 
                    layer.getBiases().clone(),
                    layer.getStateWeights().clone(),
                    layer.getGains().clone()));
        }

        return children;
    }

    public List<SRNNLayerGene> getLayers() {
        return layers;
    }
}