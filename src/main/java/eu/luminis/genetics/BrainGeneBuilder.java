package eu.luminis.genetics;

import eu.luminis.Options;
import eu.luminis.util.Range;

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
        List<NeuronGene> layer = createLayer(0, outputCount, false);
        layers.add(layer);
    }

    private void addInputLayer() {
        int maxTargetCount = layers.get(layers.size() - 1).size();

        List<NeuronGene> layer = createLayer(maxTargetCount, inputCount, false);
        layers.add(layer);
    }

    private void addHiddenLayers() {
        int minimumHiddenLayerWidth = Math.min(inputCount, outputCount);
        int maximumHiddenLayerWidth = Math.max(inputCount, outputCount); //Options.maxNeuronsPerLayer.get();
        int hiddenLayerCount = getHiddenLayerCount();
        for (int i = 0; i < hiddenLayerCount; i++) {

            int maxTargetCount = layers.get(layers.size() - 1).size();
            int neuronCount = getNeuronCount(minimumHiddenLayerWidth, maximumHiddenLayerWidth);

            List<NeuronGene> layer = createLayer(maxTargetCount, neuronCount, Options.brainIsRecurrent);
            layers.add(layer);
        }
    }

    private int getHiddenLayerCount() {
        return (int)Math.floor(new Range(Options.minHiddenLayers.get(), Options.maxHiddenLayers.get()).random());
    }

    private int getNeuronCount(int minimumHiddenLayerWidth, int maximumHiddenLayerWidth) {
        return (int) Math.floor(new Range(minimumHiddenLayerWidth, maximumHiddenLayerWidth).random());
    }

    private List<NeuronGene> createLayer(int maxTargetCount, int neuronCount, boolean recurrent) {
        int axonCount = recurrent ? maxTargetCount + neuronCount : maxTargetCount;

        List<NeuronGene> neuronGenes = new ArrayList<>();
        for (int i = 0; i < neuronCount; i++) {
            neuronGenes.add(new NeuronGene(axonCount));
        }

        return neuronGenes;
    }
}
