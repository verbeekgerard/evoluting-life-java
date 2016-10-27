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
        List<NeuronGene> layer = createLayer(0, outputCount);
        layers.add(layer);
    }

    private void addInputLayer() {
        int maxTargetCount = layers.get(layers.size() - 1).size();

        List<NeuronGene> layer = createLayer(maxTargetCount, inputCount);
        layers.add(layer);
    }

    private void addHiddenLayers() {
        double minGenesPerHL = Math.max(inputCount, outputCount);
        double numberOfHL = Math.floor(new Range(Options.minHiddenLayers.get(), Options.maxHiddenLayers.get()).random());
        for (int i = 0; i < numberOfHL; i++) {

            int maxTargetCount = layers.get(layers.size() - 1).size();
            int neuronCount = (int) Math.floor(new Range(minGenesPerHL, Options.maxNeuronsPerLayer.get()).random());

            List<NeuronGene> layer = createLayer(maxTargetCount, neuronCount);
            layers.add(layer);
        }
    }

    private List<NeuronGene> createLayer(int maxTargetCount, int neuronCount) {
        List<NeuronGene> neuronGenes = new ArrayList<>();
        for (int i = 0; i < neuronCount; i++) {
            neuronGenes.add(new NeuronGene(maxTargetCount));
        }
        return neuronGenes;
    }
}
