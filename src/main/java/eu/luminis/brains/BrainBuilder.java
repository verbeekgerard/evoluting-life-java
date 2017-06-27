package eu.luminis.brains;

import eu.luminis.Options;
import eu.luminis.genetics.BrainGene;
import eu.luminis.genetics.NeuronGene;

import java.util.ArrayList;
import java.util.List;

public class BrainBuilder {
    private BrainGene brainChromosome;

    private BrainBuilder() {

    }

    public static BrainBuilder brain() {
        return new BrainBuilder();
    }

    public BrainBuilder withBrainChromosome(BrainGene brainChromosome) {
        this.brainChromosome = brainChromosome;

        return this;
    }

    public IBrain build() {
        Layer outputLayer = createOutputLayer();
        Layer[] hiddenLayers = createHiddenLayers(outputLayer);

        // The input layer is of a different type
        Layer firstHiddenLayer = hiddenLayers[hiddenLayers.length - 1];
        InputLayer inputLayer = createInputLayer(firstHiddenLayer);

        return new Brain(outputLayer, hiddenLayers, inputLayer);
    }

    private InputLayer createInputLayer(Layer firstHiddenLayer) {
        List<List<NeuronGene>> genLayers = brainChromosome.getLayers();
        List<NeuronGene> genLayer = genLayers.get(genLayers.size() - 1);

        return LayerBuilder
                .layer()
                .withNeuronGenes(genLayer)
                .withTargetLayer(firstHiddenLayer)
                .buildAsInput();
    }

    private Layer createOutputLayer() {
        List<NeuronGene> genLayer = brainChromosome.getLayers().get(0);

        return LayerBuilder
                .layer()
                .withNeuronGenes(genLayer)
                .build();
    }

    private Layer[] createHiddenLayers(Layer outputLayer) {
        List<List<NeuronGene>> genLayers = brainChromosome.getLayers();

        Layer[] layers = new Layer[genLayers.size() - 2]; // exclude the input and output layers
        layers[0] = createHiddenLayer(genLayers.get(1), outputLayer);

        for (int i = 2; i < genLayers.size() - 1; i++) {
            Layer targetLayer = layers[i - 2];
            layers[i - 1] = createHiddenLayer(genLayers.get(i), targetLayer);
        }

        return layers;
    }

    private Layer createHiddenLayer(List<NeuronGene> layerGene, Layer targetLayer) {
        return LayerBuilder
                .layer()
                .withNeuronGenes(layerGene)
                .withTargetLayer(targetLayer)
                .withRecurrence(Options.brainIsRecurrent)
                .build();
    }
}
