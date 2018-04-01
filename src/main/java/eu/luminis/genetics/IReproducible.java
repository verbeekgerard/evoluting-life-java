package eu.luminis.genetics;

public interface IReproducible {
    void mutate();
    <T extends IReproducible> T[] mate(IReproducible partner);
}