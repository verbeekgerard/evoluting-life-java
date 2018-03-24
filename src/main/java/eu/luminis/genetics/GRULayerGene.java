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
        this.Gz = Gz;
        this.Gr = Gr;
        this.Gh = Gh;
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
