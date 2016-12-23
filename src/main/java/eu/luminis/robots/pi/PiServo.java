package eu.luminis.robots.pi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import static eu.luminis.robots.pi.util.SleepUtil.sleep;

public class PiServo {

    private int servoblasterId;

    private double msPulse_0_Degrees;
    private double msPulse_90_Degrees;
    private double msPulse_180_Degrees;
    private double msDelayPerDegree;

    private int currentAngle = 0;
        
    public PiServo(int servoblasterId, double msPulse_0_Degrees, double msPulse_90_Degrees, double msPulse_180_Degrees,
            double msDelayPerDegree) {
        this.servoblasterId = servoblasterId;
        this.msPulse_0_Degrees = msPulse_0_Degrees;
        this.msPulse_90_Degrees = msPulse_90_Degrees;
        this.msPulse_180_Degrees = msPulse_180_Degrees;
        this.msDelayPerDegree = msDelayPerDegree;
    }

    public void moveTo(int angle) {
        try (OutputStream out = new FileOutputStream("/dev/servoblaster");
                OutputStreamWriter writer = new OutputStreamWriter(out)) {

            writer.write(servoblasterId + "=" + getPulse(angle) + "\n");
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Make sure the servo has time to turn
        sleep(Math.round(Math.abs(currentAngle - angle) * this.msDelayPerDegree));
        currentAngle = angle;
    }

    public void shutdown() {
        moveTo(90);
    }

    private long getPulse(int angle) {
        double pulseInMs = angle >= 90 ?
                (this.msPulse_180_Degrees - this.msPulse_90_Degrees) / 90 * (angle-90) + this.msPulse_90_Degrees :
                (this.msPulse_90_Degrees - this.msPulse_0_Degrees) / 90 * angle + this.msPulse_0_Degrees;

        return Math.round(pulseInMs * 100);
    }
}