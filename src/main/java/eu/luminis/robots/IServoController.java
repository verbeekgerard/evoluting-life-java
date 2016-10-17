package eu.luminis.robots;

public interface IServoController {
    double getAngle();
    void changeAngle(double acceleration);
}
