package eu.luminis.genetics;

import java.util.*;

public class GRULayerGene {
    private GateLayerGene Gz;
    private GateLayerGene Gr;
    private GateLayerGene Gh;

    public GRULayerGene(int rows, int columns) {
        Gz = new GateLayerGene(rows, columns);
        Gr = new GateLayerGene(rows, columns);
        Gh = new GateLayerGene(rows, columns);
    }

    public GRULayerGene(GateLayerGene Gz, GateLayerGene Gr, GateLayerGene Gh) {
        this.Gz = new GateLayerGene(
            Gz.getWeights().clone(),
            Gz.getStateWeights().clone(),
            Gz.getGains().clone(),
            Gz.getBiases().clone());
        this.Gr = new GateLayerGene(
            Gr.getWeights().clone(),
            Gr.getStateWeights().clone(),
            Gr.getGains().clone(),
            Gr.getBiases().clone());
        this.Gh =  new GateLayerGene(
            Gh.getWeights().clone(),
            Gh.getStateWeights().clone(),
            Gh.getGains().clone(),
            Gh.getBiases().clone());
    }

    public GateLayerGene getGz() {
        return Gz;
    }

    public GateLayerGene getGr() {
        return Gr;
    }

    public GateLayerGene getGh() {
        return Gh;
    }

    public void mutate() {
        Gz.mutate();
        Gr.mutate();
        Gh.mutate();
    }

    public List<GRULayerGene> mate(GRULayerGene partner) {
        List<GRULayerGene> children = new ArrayList<>();

        GateLayerGene[] gzChildren = this.Gz.mate(partner.Gz);
        GateLayerGene[] grChildren = this.Gr.mate(partner.Gr);
        GateLayerGene[] ghChildren = this.Gh.mate(partner.Gh);

        for (int k = 0; k < 2; k++) {
            children.add(new GRULayerGene(gzChildren[k], grChildren[0], ghChildren[0]));
        }

        return children;
    }
}
