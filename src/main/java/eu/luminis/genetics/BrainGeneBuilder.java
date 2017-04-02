package eu.luminis.genetics;

import java.util.ArrayList;
import java.util.List;

class BrainGeneBuilder {
    private final List<List<NeuronGene>> layers = new ArrayList<>();
    private int inputCount;
    private int outputCount;

    public BrainGeneBuilder(int inputCount, int outputCount) {
        this.inputCount = inputCount;
        this.outputCount = outputCount;
    }

    public BrainGene build() {
        addOutputLayer();
        addHiddenLayers();
        addInputLayer();

        return new BrainGene(layers);
    }

    private void addOutputLayer() {
        List<NeuronGene> layer = createLayer(0, outputCount);
        layers.add(layer);
    }

    private void addInputLayer() {
        int maxTargetCount = layers.get(layers.size() - 1).size();

        List<NeuronGene> layer = createLayer(maxTargetCount, inputCount);
        layers.add(layer);
    }

    private void addHiddenLayers() {
        int minimumHiddenLayerWidth = Math.max(inputCount, outputCount);
        int hiddenLayerCount = getHiddenLayerCount();
        for (int i = 0; i < hiddenLayerCount; i++) {

            int maxTargetCount = layers.get(layers.size() - 1).size();
            int neuronCount = getNeuronCount(minimumHiddenLayerWidth);

            List<NeuronGene> layer = createLayer(maxTargetCount, neuronCount);
            layers.add(layer);
        }
    }

    private int getHiddenLayerCount() {
        return 2;
        // return (int)Math.floor(new Range(Options.minHiddenLayers.get(), Options.maxHiddenLayers.get()).random());
    }

    private int getNeuronCount(int minimumHiddenLayerWidth) {
        return 2 * minimumHiddenLayerWidth;
        // return (int) Math.floor(new Range(2 * minimumHiddenLayerWidth, 3 * minimumHiddenLayerWidth).random());
        // return (int) Math.floor(new Range(minimumHiddenLayerWidth, Options.maxNeuronsPerLayer.get()).random());
    }

    private List<NeuronGene> createLayer(int maxTargetCount, int neuronCount) {
        List<NeuronGene> neuronGenes = new ArrayList<>();
        for (int i = 0; i < neuronCount; i++) {
            neuronGenes.add(new NeuronGene(maxTargetCount));
        }
        return neuronGenes;
    }
}
