package eu.luminis.genetics;

import eu.luminis.Options;
import eu.luminis.util.Range;

import java.util.ArrayList;
import java.util.List;

class NeuralNetworkGeneBuilder {
    private int inputSize;
    private int outputSize;

    private NeuralNetworkGeneBuilder() {
    }

    public static NeuralNetworkGeneBuilder create() {
        return new NeuralNetworkGeneBuilder();
    }

    public NeuralNetworkGeneBuilder withInputSize(int inputSize) {
        this.inputSize = inputSize;
        return this;
    }

    public NeuralNetworkGeneBuilder withOuputSize(int outputSize) {
        this.outputSize = outputSize;
        return this;
    }

    public NeuralNetworkGene build() {
        List<GRULayerGene> layers = new ArrayList<>();
        int[] layerSizes = generateLayerSizes();

        layers.add(createInputLayer(layerSizes));
        layers.addAll(createHiddenLayers(layerSizes));
        layers.add(createOutputLayer(layerSizes));

        return new NeuralNetworkGene(layers);
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
        return new GRULayerGene(layerSizes[0], this.inputSize);
    }

    private GRULayerGene createOutputLayer(int[] layerSizes) {
        return new GRULayerGene(this.outputSize, layerSizes[layerSizes.length-1]);
    }

    private List<GRULayerGene> createHiddenLayers(int[] layerSizes) {
        List<GRULayerGene> layers = new ArrayList<>();

        for (int i = 0; i < layerSizes.length-1; i++) {
            GRULayerGene layer = new GRULayerGene(layerSizes[i+1], layerSizes[i]);
            layers.add(layer);
        }

        return layers;
    }

    private int generateDepth() {
        return (int)Math.floor(new Range(Options.minHiddenLayers.get(), Options.maxHiddenLayers.get()).random());
    }
}
