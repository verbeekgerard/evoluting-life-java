package eu.luminis.robots.pi;

import jdk.dio.DeviceConfig;
import jdk.dio.DeviceManager;
import jdk.dio.gpio.GPIOPinConfig;
import jdk.dio.pwm.PWMChannel;
import jdk.dio.pwm.PWMChannelConfig;

import java.io.IOException;

public class PiPwmMotor {

    private static final int pulsePeriod = 50;

    private final PWMChannel forwardChannel;
    private final PWMChannel reverseChannel;
    private final double pulseWidthFactor;

    public PiPwmMotor(int forwardPinNumber, int reversePinNumber, double maxVelocity) throws IOException {
        this.forwardChannel = DeviceManager.open(new PWMChannelConfig(DeviceConfig.DEFAULT, forwardPinNumber,
                PWMChannelConfig.IDLE_STATE_LOW, pulsePeriod, PWMChannelConfig.ALIGN_LEFT,
                new GPIOPinConfig(DeviceConfig.DEFAULT, forwardPinNumber, GPIOPinConfig.DIR_OUTPUT_ONLY,
                        GPIOPinConfig.MODE_OUTPUT_PUSH_PULL, GPIOPinConfig.TRIGGER_NONE, false)));
        this.reverseChannel = DeviceManager.open(new PWMChannelConfig(DeviceConfig.DEFAULT, reversePinNumber,
                PWMChannelConfig.IDLE_STATE_LOW, pulsePeriod, PWMChannelConfig.ALIGN_LEFT,
                new GPIOPinConfig(DeviceConfig.DEFAULT, reversePinNumber, GPIOPinConfig.DIR_OUTPUT_ONLY,
                        GPIOPinConfig.MODE_OUTPUT_PUSH_PULL, GPIOPinConfig.TRIGGER_NONE, false)));
        this.pulseWidthFactor = pulsePeriod / maxVelocity;
    }

    public void stop() {
        try {
            forwardChannel.stopGeneration();
            reverseChannel.stopGeneration();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void forward(double velocity) {
        try {
            forwardChannel.startGeneration((int) (velocity * pulseWidthFactor));
            reverseChannel.stopGeneration();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reverse(double velocity) {
        try {
            forwardChannel.stopGeneration();
            reverseChannel.startGeneration((int) (velocity * pulseWidthFactor));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() throws IOException {
        forwardChannel.stopGeneration();
        reverseChannel.stopGeneration();

        forwardChannel.close();
        reverseChannel.close();
    }
}
