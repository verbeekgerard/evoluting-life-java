package eu.luminis.genetics;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.luminis.Options;

public class BrainGene {

    private final List<List<NeuronGene>> layers = new ArrayList<>();

    BrainGene(List<List<NeuronGene>> stateLayers) {
        for (List<NeuronGene> stateLayer : stateLayers) {
            List<NeuronGene> layer = stateLayer.stream()
                    .map(neuronGene ->
                            new NeuronGene(
                                    neuronGene.getThreshold(),
                                    neuronGene.getRelaxation(),
                                    neuronGene.getAxons()))
                    .collect(Collectors.toList());

            layers.add(layer);
        }
    }

    public void mutate() {
        for (int i = 0; i < layers.size(); i++) {
            List<NeuronGene> stateLayer = layers.get(i);

            for (int j = 0; j < stateLayer.size(); j++) {
                NeuronGene gene = stateLayer.get(j);

                if (Math.random() < Options.neuronReplacementRate.get()) {
                    int targetCount = gene.getAxons().length;
                    stateLayer.set(j, new NeuronGene(targetCount));
                    continue;
                }

                if (Math.random() < Options.neuronMutationRate.get()) {
                    gene.mutate();
                }
            }
        }
    }

    public List<BrainGene> mate(BrainGene partner) {
        List<List<NeuronGene>> a = this.layers;
        List<List<NeuronGene>> b = partner.layers;

        if (a.size() != b.size()) {
            return createChildClones(this, partner);
        }

        List<BrainGene> children = new ArrayList<>();
        children.add(new BrainGene(getEmptyChild(a.size())));
        children.add(new BrainGene(getEmptyChild(a.size())));

        for (int i = 0; i < a.size(); i++) {
            List<NeuronGene> layerA = a.get(i);
            List<NeuronGene> layerB = b.get(i);

            if (layerA.size() != layerB.size()) {
                return createChildClones(this, partner);
            }

            for (int j = 0; j < layerA.size(); j++) {
                NeuronGene geneA = layerA.get(j);
                NeuronGene geneB = layerB.get(j);
                NeuronGene[] childNeurons = geneA.mate(geneB);

                for (int k = 0; k < 2; k++) {
                    children.get(k).layers.get(i).add(childNeurons[k]);
                }
            }
        }

        return children;
    }

    public List<List<NeuronGene>> getLayers() {
        return layers;
    }

    private List<BrainGene> createChildClones(BrainGene a, BrainGene b) {
        List<BrainGene> children = new ArrayList<>();

        if (Math.random() < 0.5) {
            children.add(a);
            children.add(b);
        } else {
            children.add(b);
            children.add(a);
        }

        return children;
    }

    private List<List<NeuronGene>> getEmptyChild(int layerCount) {
        List<List<NeuronGene>> stateLayers = new ArrayList<>();
        for (int j = 0; j < layerCount; j++) {
            stateLayers.add(new ArrayList<>());
        }
        return stateLayers;
    }
}