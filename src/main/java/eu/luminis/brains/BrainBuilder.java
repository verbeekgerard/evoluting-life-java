package eu.luminis.brains;

import eu.luminis.genetics.BrainGene;
import eu.luminis.genetics.NeuronGene;

import java.util.ArrayList;
import java.util.List;

public class BrainBuilder {
    private List<List<NeuronGene>> genLayers;

    public BrainBuilder(BrainGene brainGene) {
        this.genLayers = brainGene.getLayers();
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
        return new InputLayer(genLayers.get(genLayers.size() - 1), firstHiddenLayer);
    }

    private Layer createOutputLayer() {
        return new Layer(genLayers.get(0));
    }

    private List<Layer> createHiddenLayers(Layer outputLayer) {
        List<Layer> layers = new ArrayList<>();
        layers.add(outputLayer);

        for (int i = 1; i < genLayers.size() - 1; i++) {
            Layer layer = new Layer(genLayers.get(i), layers.get(layers.size() - 1));
            layers.add(layer);
        }

        layers.remove(0);

        return layers;
    }
}
