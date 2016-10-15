package eu.luminis.robots;

public interface IServo {
    void moveTo(int angle);
    int getAngle();
}
