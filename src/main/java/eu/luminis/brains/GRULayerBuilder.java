package eu.luminis.brains;

import eu.luminis.genetics.*;

import org.apache.commons.math3.analysis.*;

/**
 * Builds up a layer of the gated recurrent unit neural network
 */
class GRULayerBuilder {
    private GRULayerGene gene;
    private UnivariateFunction resetActivation = new HardSigmoid();
    private UnivariateFunction updateActivation = new HardSigmoid();
    private UnivariateFunction outputActivation = new HardTanh();

    private GRULayerBuilder() {
    }

    public static GRULayerBuilder create() {
        return new GRULayerBuilder();
    }

    public GRULayerBuilder withGene(GRULayerGene gene) {
        this.gene = gene;
        return this;
    }

    public GRULayerBuilder withResetActivation(UnivariateFunction activation) {
        this.resetActivation = activation;
        return this;
    }

    public GRULayerBuilder withUpdateActivation(UnivariateFunction activation) {
        this.updateActivation = activation;
        return this;
    }

    public GRULayerBuilder withOutputActivation(UnivariateFunction activation) {
        this.outputActivation = activation;
        return this;
    }

    public GRULayer build() {
        GateLayer updateLayer = GateLayerBuilder.create()
            .withGene(gene.getUpdateLayerGene())
            .withActivation(updateActivation)
            .build();

        GateLayer resetLayer = GateLayerBuilder.create()
            .withGene(gene.getResetLayerGene())
            .withActivation(resetActivation)
            .build();

        GateLayer outputLayer = GateLayerBuilder.create()
            .withGene(gene.getOutputLayerGene())
            .withActivation(outputActivation)
            .build();

        return new GRULayer(updateLayer, resetLayer, outputLayer);
    }
}
