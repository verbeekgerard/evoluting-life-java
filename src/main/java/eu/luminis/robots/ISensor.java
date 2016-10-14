package eu.luminis.robots;

public interface ISensor {
    Double sense();
    Double sense(long msTimeout);
}
