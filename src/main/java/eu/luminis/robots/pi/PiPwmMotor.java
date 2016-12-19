package eu.luminis.robots.pi;

import jdk.dio.DeviceConfig;
import jdk.dio.DeviceManager;
import jdk.dio.gpio.GPIOPin;
import jdk.dio.gpio.GPIOPinConfig;
import jdk.dio.pwm.PWMChannel;
import jdk.dio.pwm.PWMChannelConfig;

import java.io.IOException;

public class PiPwmMotor {

    private static final int pulsePeriod = 50;

    private final PWMChannel forwardChannel;
    private final GPIOPin reversePin;
    private final double pulseWidthFactor;

    public PiPwmMotor(int forwardPinNumber, int reversePinNumber, double maxVelocity) throws IOException {
        System.out.println("Initialize motor on pin " + forwardPinNumber);

        this.forwardChannel = DeviceManager.open(new PWMChannelConfig(DeviceConfig.DEFAULT, forwardPinNumber,
                PWMChannelConfig.IDLE_STATE_LOW, pulsePeriod, PWMChannelConfig.ALIGN_LEFT,
                new GPIOPinConfig(DeviceConfig.DEFAULT, forwardPinNumber, GPIOPinConfig.DIR_OUTPUT_ONLY,
                        GPIOPinConfig.MODE_OUTPUT_PUSH_PULL, GPIOPinConfig.TRIGGER_NONE, false)));
        this.reversePin = DeviceManager.open(new GPIOPinConfig(DeviceConfig.DEFAULT, reversePinNumber,
                GPIOPinConfig.DIR_OUTPUT_ONLY, GPIOPinConfig.MODE_OUTPUT_PUSH_PULL, GPIOPinConfig.TRIGGER_NONE, false));
        this.pulseWidthFactor = pulsePeriod / maxVelocity;
    }

    public void stop() {
        try {
            forwardChannel.stopGeneration();
            reversePin.setValue(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void forward(double velocity) {
        try {
            forwardChannel.startGeneration((int) (velocity * pulseWidthFactor));
            reversePin.setValue(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reverse(double velocity) {
        try {
            forwardChannel.stopGeneration();
            reversePin.setValue(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() throws IOException {
        forwardChannel.stopGeneration();
        reversePin.setValue(false);

        forwardChannel.close();
        reversePin.close();
    }
}
