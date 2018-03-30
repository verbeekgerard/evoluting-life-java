/** package eu.luminis.genetics;

import eu.luminis.*;
import java.util.ArrayList;
import java.util.List;

public abstract class NNGene<TLayerGene extends IReproducible> {

    private final List<TLayerGene> layers = new ArrayList<>();

    public abstract NNGene<TLayerGene> create();

    public void mutate() {
        for (TLayerGene layer : layers) {
            layer.mutate();
        }

        if (Math.random() < Options.neuralNetworkMutationRate.get()) {
            int newIndex = (int)Math.floor(layers.size() * Math.random());
            int size = layers.get(newIndex).getColumns();
            TLayerGene newLAyer = SRNNLayerGeneBuilder.create()
                .withSize(size)
                .builIdentity();
            layers.add(newIndex, newLAyer);
        }
    }

    public NNGene<TLayerGene>[] mate(NNGene<TLayerGene> partner) {
        List<TLayerGene> smallest = this.layers.size() < partner.layers.size() ? this.layers : partner.layers;
        List<TLayerGene> largest = this.layers.size() < partner.layers.size() ? partner.layers : this.layers;

        NNGene<TLayerGene>[] children = new NNGene<TLayerGene>[2];
        children[0] = create();
        children[1] = create();

        for(int i=0; i<smallest.size(); i++) {
            TLayerGene[] childLayers = largest.get(i).mate(smallest.get(i));
            for (int k = 0; k < 2; k++) {
                children[k].getLayers().add(childLayers[k]);
            }
        }

        for(int i=smallest.size(); i<largest.size(); i++) {
            TLayerGene layer = largest.get(i);
            children[0].getLayers().add(layer.Clone());
        }

        return children;
    }

    public List<TLayerGene> getLayers() {
        return layers;
    }
}

// ILayer
// mate
// mutate

// Clone
// getColums

*/