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
        List<LayerGene> layers = new ArrayList<>();
        int[] layerSizes = generateLayerSizes();

        layers.add(createInputLayer(layerSizes));
        layers.addAll(createHiddenLayers(layerSizes));
        layers.add(createOutputLayer(layerSizes));

        return new NeuralNetworkGene(layers);
    }

    private int[] generateLayerSizes() {
        int[] layerSizes = new int[generateDepth()];
        int minSize = Math.min(this.inputSize, this.outputSize);
        int maxSize = Math.max(this.inputSize, this.outputSize) + 2;

        for (int i=0; i<layerSizes.length; i++) {
            layerSizes[i] = (int) Math.floor(new Range(minSize, maxSize).random());
        }
        return layerSizes;
    }

    private LayerGene createInputLayer(int[] layerSizes) {
        return new LayerGene(layerSizes[1], layerSizes[0]);
    }

    private LayerGene createOutputLayer(int[] layerSizes) {
        return new LayerGene(layerSizes[layerSizes.length-1], layerSizes[layerSizes.length-2]);
    }

    private List<LayerGene> createHiddenLayers(int[] layerSizes) {
        List<LayerGene> layers = new ArrayList<>();

        for (int i = 0; i < layerSizes.length-2; i++) {
            LayerGene layer = new LayerGene(layerSizes[i+2], layerSizes[i+1]);
            layers.add(layer);
        }

        return layers;
    }

    private int generateDepth() {
        return 1 + (int)Math.floor(new Range(Options.minHiddenLayers.get(), Options.maxHiddenLayers.get()).random());
    }
}
