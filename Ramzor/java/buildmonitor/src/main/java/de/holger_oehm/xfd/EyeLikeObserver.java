package de.holger_oehm.xfd;

import de.holger_oehm.usb.leds.USBLeds;
import de.holger_oehm.xfd.jenkins.ProxyHandler;
import de.holger_oehm.xfd.jenkins.State;

public class EyeLikeObserver {
    private final int DELAY = 2 * 1000; // X * seconds

    private static final USBLeds LEDS = USBLeds.Factory.enumerateLedDevices().next();

    public static void main(final String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                LEDS.close();
            }
        });
        new EyeLikeObserver().run();
    }

    private final ProxyHandler monitor;

    public EyeLikeObserver() {
        monitor = new ProxyHandler();
    }

    private void run() {
        LEDS.off();
        do {
            try {
                Thread.sleep(1000);
                final State buildState = monitor.state();
                switch (buildState) {
                    case GREEN:
                        LEDS.green();
                        break;

                    case RED:
                        LEDS.red();
                        break;

                    case YELLOW:
                        LEDS.yellow();
                        break;

                    default:
                        LEDS.red();
                        break;
                }
                Thread.sleep(DELAY);
            } catch (final InterruptedException interrupt) {
                Thread.currentThread().interrupt();
                LEDS.off();
                return;
            } catch (final Exception e) {
                System.err.println(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
                LEDS.off();
            }
        } while (true);
    }
}
