package eu.luminis.robots;

public interface IServoController {
    int getAngle();
    void changeAngle(double angularChange);
}
