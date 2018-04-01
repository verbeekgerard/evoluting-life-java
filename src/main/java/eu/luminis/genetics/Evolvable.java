package eu.luminis.genetics;

abstract class Evolvable {
    public abstract double[] getInitiateProperties();
    public abstract <TEvolvable extends Evolvable> TEvolvable initiate(double[] properties);
    public abstract <TEvolvable extends Evolvable> TEvolvable[] newArray(int size);
}