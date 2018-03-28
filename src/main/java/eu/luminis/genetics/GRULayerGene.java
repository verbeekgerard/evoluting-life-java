package eu.luminis.genetics;

import java.util.*;

public class GRULayerGene {
    private GateLayerGene updateLayerGene;
    private GateLayerGene resetLayerGene;
    private GateLayerGene outputLayerGene;

    public GRULayerGene(GateLayerGene updateLayerGene, GateLayerGene resetLayerGene, GateLayerGene outputLayerGene) {
        this.updateLayerGene = updateLayerGene;
        this.resetLayerGene = resetLayerGene;
        this.outputLayerGene = outputLayerGene;
    }

    public GRULayerGene Clone() {
        return new GRULayerGene(
            updateLayerGene.Clone(),
            resetLayerGene.Clone(),
            outputLayerGene.Clone()
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

        GateLayerGene[] updateLayerGeneChildren = updateLayerGene.mate(partner.updateLayerGene);
        GateLayerGene[] resetLayerGeneChildren = resetLayerGene.mate(partner.resetLayerGene);
        GateLayerGene[] outputLayerGeneChildren = outputLayerGene.mate(partner.outputLayerGene);

        for (int k = 0; k < 2; k++) {
            children.add(new GRULayerGene(
                updateLayerGeneChildren[k],
                resetLayerGeneChildren[k],
                outputLayerGeneChildren[k]));
        }

        return children;
    }
}
