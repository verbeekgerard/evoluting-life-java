package eu.luminis.robots.pi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import static eu.luminis.robots.pi.util.SleepUtil.sleep;

public class PiServo {
    private static final double steps_0_Degrees = 56;
    private static final double steps_90_Degrees = 144;
    private static final double steps_180_Degrees = 244;
    private static final double msDelayPerDegree = 2.34;

    private final int servoblasterId;
    private int currentAngle = 0;
        
    public PiServo(int servoblasterId) {
        this.servoblasterId = servoblasterId;
    }

    public void moveTo(int angle) {
        try (OutputStream out = new FileOutputStream("/dev/servoblaster");
                OutputStreamWriter writer = new OutputStreamWriter(out)) {

            writer.write(servoblasterId + "=" + getSteps(angle) + "\n");
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Make sure the servo has time to turn
        sleep(Math.round(1.0 + Math.abs(currentAngle - angle) * msDelayPerDegree));
        currentAngle = angle;
    }

    public void shutdown() {
        moveTo(90);
    }

    private long getSteps(int angle) {
        double steps = angle >= 90 ?
                (steps_180_Degrees - steps_90_Degrees) / 90 * (angle-90) + steps_90_Degrees :
                (steps_90_Degrees - steps_0_Degrees) / 90 * angle + steps_0_Degrees;

        return Math.round(steps);
    }
}