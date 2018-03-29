package eu.luminis.genetics;

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

    public GRULayerGene[] mate(GRULayerGene partner) {
        GRULayerGene[] children = new GRULayerGene[2];

        GateLayerGene[] updateLayerGeneChildren = updateLayerGene.mate(partner.updateLayerGene);
        GateLayerGene[] resetLayerGeneChildren = resetLayerGene.mate(partner.resetLayerGene);
        GateLayerGene[] outputLayerGeneChildren = outputLayerGene.mate(partner.outputLayerGene);

        for (int k = 0; k < 2; k++) {
            children[k] = new GRULayerGene(
                updateLayerGeneChildren[k],
                resetLayerGeneChildren[k],
                outputLayerGeneChildren[k]);
        }

        return children;
    }
}
