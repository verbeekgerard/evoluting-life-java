package eu.luminis.genetics;

import eu.luminis.*;
import java.util.ArrayList;
import java.util.List;

public class SRNNGene {

    private final List<SRNNLayerGene> layers = new ArrayList<>();

    SRNNGene(List<SRNNLayerGene> stateLayers) {
        for (SRNNLayerGene stateLayer : stateLayers) {
            layers.add(
                new SRNNLayerGene(
                    stateLayer.getRows(),
                    stateLayer.getColumns()));
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
                .builIdentity();
            layers.add(newIndex, newLAyer);
        }
    }

    public SRNNGene[] mate(SRNNGene partner) {
        List<SRNNLayerGene> smallest = this.layers.size() < partner.layers.size() ? this.layers : partner.layers;
        List<SRNNLayerGene> largest = this.layers.size() < partner.layers.size() ? partner.layers : this.layers;

        SRNNGene[] children = new SRNNGene[2];
        children[0] = new SRNNGene(new ArrayList<>());
        children[1] = new SRNNGene(new ArrayList<>());

        for(int i=0; i<smallest.size(); i++) {
            SRNNLayerGene[] childLayers = largest.get(i).mate(smallest.get(i));
            for (int k = 0; k < 2; k++) {
                children[k].layers.add(childLayers[k]);
            }
        }

        for(int i=smallest.size(); i<largest.size(); i++) {
            SRNNLayerGene layer = largest.get(i);
            children[0].layers.add(layer.Clone());
        }

        return children;
    }

    public List<SRNNLayerGene> getLayers() {
        return layers;
    }
}
