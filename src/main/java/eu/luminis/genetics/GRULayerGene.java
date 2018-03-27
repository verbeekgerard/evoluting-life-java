package eu.luminis.genetics;

import java.util.*;

public class GRULayerGene {
    private GateLayerGene updateLayerGene;
    private GateLayerGene resetLayerGene;
    private GateLayerGene outputLayerGene;

    public GRULayerGene(int rows, int columns) {
        updateLayerGene = new GateLayerGene(rows, columns, 1.5);
        resetLayerGene = new GateLayerGene(rows, columns, 1.5);
        outputLayerGene = new GateLayerGene(rows, columns);
    }

    public GRULayerGene(GateLayerGene updateLayerGene, GateLayerGene resetLayerGene, GateLayerGene outputLayerGene) {
        this.updateLayerGene = updateLayerGene;
        this.resetLayerGene = resetLayerGene;
        this.outputLayerGene = outputLayerGene;
    }

    public GRULayerGene Clone() {
        return new GRULayerGene(
            this.updateLayerGene.Clone(),
            this.resetLayerGene.Clone(),
            this.outputLayerGene.Clone()
        );
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
                resetLayerGeneChildren[k],
                outputLayerGeneChildren[k]));
        }

        return children;
    }
}
