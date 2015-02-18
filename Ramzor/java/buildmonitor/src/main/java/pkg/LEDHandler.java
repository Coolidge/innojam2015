package pkg;

import de.holger_oehm.usb.leds.USBLeds;

public class LEDHandler {

    private static final USBLeds LEDS = USBLeds.Factory.enumerateLedDevices().next();

    public static void off() {
        LEDS.off();
    }

    public static void green() {
        LEDS.green();
    }

    public static void red() {
        LEDS.red();
    }

    public static void yellow() {
        LEDS.yellow();
    }

    public static void changeLedState(Integer percentage) {
        if (percentage <= 30) {
            LEDHandler.red();
        } else if (percentage <= 60) {
            LEDHandler.yellow();
        } else {
            LEDHandler.green();
        }

    }
}
