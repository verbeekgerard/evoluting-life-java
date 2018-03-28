package eu.luminis.genetics;

import eu.luminis.Options;
import eu.luminis.util.Range;

import java.util.ArrayList;
import java.util.List;

class GRUNeuralNetworkGeneBuilder {
    private int inputSize;
    private int outputSize;

    private GRUNeuralNetworkGeneBuilder() {
    }

    public static GRUNeuralNetworkGeneBuilder create() {
        return new GRUNeuralNetworkGeneBuilder();
    }

    public GRUNeuralNetworkGeneBuilder withInputSize(int inputSize) {
        this.inputSize = inputSize;
        return this;
    }

    public GRUNeuralNetworkGeneBuilder withOuputSize(int outputSize) {
        this.outputSize = outputSize;
        return this;
    }

    public GRUNeuralNetworkGene build() {
        List<GRULayerGene> layers = new ArrayList<>();
        int[] layerSizes = generateLayerSizes();

        layers.add(createInputLayer(layerSizes));
        layers.addAll(createHiddenLayers(layerSizes));
        layers.add(createOutputLayer(layerSizes));

        return new GRUNeuralNetworkGene(layers);
    }

    private int[] generateLayerSizes() {
        int depth = generateDepth();
        int[] layerSizes = new int[depth];
        int minSize = Math.min(this.inputSize, this.outputSize);
        int maxSize = Math.max(this.inputSize, this.outputSize) + 2;

        for (int i=0; i<depth; i++) {
            layerSizes[i] = (int) Math.floor(new Range(minSize, maxSize).random());
        }

        return layerSizes;
    }

    private GRULayerGene createInputLayer(int[] layerSizes) {
        return GRULayerGeneBuilder.create()
            .withSize(layerSizes[0], this.inputSize)
            .build();
    }

    private GRULayerGene createOutputLayer(int[] layerSizes) {
        return GRULayerGeneBuilder.create()
            .withSize(this.outputSize, layerSizes[layerSizes.length-1])
            .build();
    }

    private List<GRULayerGene> createHiddenLayers(int[] layerSizes) {
        List<GRULayerGene> layers = new ArrayList<>();

        for (int i = 0; i < layerSizes.length-1; i++) {
            GRULayerGene layer = GRULayerGeneBuilder.create()
                                    .withSize(layerSizes[i+1], layerSizes[i])
                                    .build();
            layers.add(layer);
        }

        return layers;
    }

    private int generateDepth() {
        return (int)Math.floor(new Range(Options.minHiddenLayers.get(), Options.maxHiddenLayers.get()).random());
    }
}
