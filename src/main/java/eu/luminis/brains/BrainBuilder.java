package eu.luminis.brains;

import eu.luminis.Options;
import eu.luminis.genetics.BrainGene;
import eu.luminis.genetics.NeuronGene;

import java.util.ArrayList;
import java.util.List;

public class BrainBuilder {
    private BrainGene brainChromosome;

    private BrainBuilder () {

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
        List<Layer> hiddenLayers = createHiddenLayers(outputLayer);

        // The input layer is of a different type
        Layer firstHiddenLayer = hiddenLayers.get(hiddenLayers.size() - 1);
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

    private List<Layer> createHiddenLayers(Layer outputLayer) {
        List<List<NeuronGene>> genLayers = brainChromosome.getLayers();
        List<Layer> layers = new ArrayList<>();
        layers.add(outputLayer);

        for (int i = 1; i < genLayers.size() - 1; i++) {
            Layer targetLayer = layers.get(layers.size() - 1);
            Layer layer = LayerBuilder
                    .layer()
                    .withNeuronGenes(genLayers.get(i))
                    .withTargetLayer(targetLayer)
                    .withRecurrence(Options.brainIsRecurrent)
                    .build();
            layers.add(layer);
        }

        layers.remove(0);

        return layers;
    }
}
