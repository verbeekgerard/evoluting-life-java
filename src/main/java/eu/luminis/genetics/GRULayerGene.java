package eu.luminis.genetics;

public class GRULayerGene {
    private SRNNLayerGene updateLayerGene;
    private SRNNLayerGene resetLayerGene;
    private SRNNLayerGene outputLayerGene;

    public GRULayerGene(SRNNLayerGene updateLayerGene, SRNNLayerGene resetLayerGene, SRNNLayerGene outputLayerGene) {
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

    public SRNNLayerGene getUpdateLayerGene() {
        return updateLayerGene;
    }

    public SRNNLayerGene getResetLayerGene() {
        return resetLayerGene;
    }

    public SRNNLayerGene getOutputLayerGene() {
        return outputLayerGene;
    }

    public void mutate() {
        updateLayerGene.mutate();
        resetLayerGene.mutate();
        outputLayerGene.mutate();
    }

    public GRULayerGene[] mate(GRULayerGene partner) {
        GRULayerGene[] children = new GRULayerGene[2];

        SRNNLayerGene[] updateLayerGeneChildren = updateLayerGene.mate(partner.updateLayerGene);
        SRNNLayerGene[] resetLayerGeneChildren = resetLayerGene.mate(partner.resetLayerGene);
        SRNNLayerGene[] outputLayerGeneChildren = outputLayerGene.mate(partner.outputLayerGene);

        for (int k = 0; k < 2; k++) {
            children[k] = new GRULayerGene(
                updateLayerGeneChildren[k],
                resetLayerGeneChildren[k],
                outputLayerGeneChildren[k]);
        }

        return children;
    }
}
