package eu.luminis.robots.core;

public interface IServoController extends IAngleRetriever, IAngleModifier {
    double getViewAngle();
}
