package pkg;


import javax.swing.*;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EyeLikeObserver {

    public static final int ESCAPE_KEY = 13;
    // Configurations:
    private static String EngineDataFile = "data.txt";
    private static String WebServerURL = "https://eyelikei305845trial.hanatrial.ondemand.com/EyeLikeServlet/MainServlet";
    private static final int MonitorPeriod = 1 * 1000; // In seconds
    private static int maxParticipants = 4;
    private static int read;

    public static void main(final String[] args) throws InterruptedException {
        FileHandler.filname = EngineDataFile;
        ProxyHandler.Init(WebServerURL);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override public void run() {
                allLights();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                do {
                    try {
                        String lastValue = FileHandler.getLastLine();
                        Integer percentage = convertToPercentage(lastValue);
                        LEDHandler.changeLedState(percentage);
                        ProxyHandler.postValue(percentage);

                        Thread.sleep(MonitorPeriod);

                    } catch (final Exception e) {
                        System.err.println(e.getMessage());
                        LEDHandler.off();
                    }
                } while (true);
            }
        });
        executorService.shutdown();
        try {
            read = System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        allLights();
        System.exit(0);
    }

    private static void allLights() {
        LEDHandler.allLights();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LEDHandler.off();
    }

    private static Integer convertToPercentage(String value) {
        Integer currentVal = 0;
        try {
            currentVal = (int) ((Float.valueOf(value) / (float) maxParticipants) * 100);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return currentVal;
    }
}
