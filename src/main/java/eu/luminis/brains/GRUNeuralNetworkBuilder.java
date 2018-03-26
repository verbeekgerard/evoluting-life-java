package eu.luminis.brains;

import eu.luminis.genetics.*;
import java.util.List;

public class GRUNeuralNetworkBuilder {
    private GRUNeuralNetworkGene neuralNetworkGene;

    private GRUNeuralNetworkBuilder() {
    }

    public static GRUNeuralNetworkBuilder create() {
        return new GRUNeuralNetworkBuilder();
    }

    public GRUNeuralNetworkBuilder withNeuralNetworkGene(GRUNeuralNetworkGene neuralNetworkGene) {
        this.neuralNetworkGene = neuralNetworkGene;

        return this;
    }

    public IBrain build() {
        List<GRULayerGene> layerGenes = this.neuralNetworkGene.getLayers();
        GRULayer[] layers = new GRULayer[layerGenes.size()];

        for (int i=0; i<layers.length-1; i++) {
            GRULayerGene layerGene = layerGenes.get(i);
            GRULayer layer = GRULayerBuilder.create()
                .withLayerGene(layerGene)
                .build();
            layers[i] = layer;
        }

        GRULayer outputLayer = GRULayerBuilder.create()
            .withLayerGene(layerGenes.get(layers.length-1))
            .buildAsOutput();
        layers[layers.length-1] = outputLayer;

        return new NeuralNetwork(layers);
    }
}
