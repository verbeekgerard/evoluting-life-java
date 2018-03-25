package eu.luminis.genetics;

import java.util.*;

public class GRULayerGene {
    private GateLayerGene updateLayerGene;
    private GateLayerGene resetLayerGene;
    private GateLayerGene outputLayerGene;

    public GRULayerGene(int rows, int columns) {
        updateLayerGene = new GateLayerGene(rows, columns);
        resetLayerGene = new GateLayerGene(rows, columns);
        outputLayerGene = new GateLayerGene(rows, columns);
    }

    public GRULayerGene(GateLayerGene updateLayerGene, GateLayerGene resetLayerGene, GateLayerGene outputLayerGene) {
        this.updateLayerGene = new GateLayerGene(
            updateLayerGene.getWeights().clone(),
            updateLayerGene.getStateWeights().clone(),
            updateLayerGene.getGains().clone(),
            updateLayerGene.getBiases().clone());
        this.resetLayerGene = new GateLayerGene(
            resetLayerGene.getWeights().clone(),
            resetLayerGene.getStateWeights().clone(),
            resetLayerGene.getGains().clone(),
            resetLayerGene.getBiases().clone());
        this.outputLayerGene =  new GateLayerGene(
            outputLayerGene.getWeights().clone(),
            outputLayerGene.getStateWeights().clone(),
            outputLayerGene.getGains().clone(),
            outputLayerGene.getBiases().clone());
    }

    public GateLayerGene getUpdateLayerGene() {
        return updateLayerGene;
    }

    public GateLayerGene getResetLayerGene() {
        return resetLayerGene;
    }

    public GateLayerGene getOutputLayerGene() {
        return outputLayerGene;
    }

    public void mutate() {
        updateLayerGene.mutate();
        resetLayerGene.mutate();
        outputLayerGene.mutate();
    }

    public List<GRULayerGene> mate(GRULayerGene partner) {
        List<GRULayerGene> children = new ArrayList<>();

        GateLayerGene[] updateLayerGeneChildren = this.updateLayerGene.mate(partner.updateLayerGene);
        GateLayerGene[] resetLayerGeneChildren = this.resetLayerGene.mate(partner.resetLayerGene);
        GateLayerGene[] outputLayerGeneChildren = this.outputLayerGene.mate(partner.outputLayerGene);

        for (int k = 0; k < 2; k++) {
            children.add(new GRULayerGene(
                updateLayerGeneChildren[k],
                resetLayerGeneChildren[0],
                outputLayerGeneChildren[0]));
        }

        return children;
    }
}
