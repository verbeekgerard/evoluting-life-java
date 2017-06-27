package eu.luminis.genetics;

abstract class Gene {
    public abstract double[] getInitiateProperties();
    public abstract <TGene extends Gene> TGene initiate(double[] properties);
    public abstract <TGene extends Gene> TGene[] newArray(int size);
}