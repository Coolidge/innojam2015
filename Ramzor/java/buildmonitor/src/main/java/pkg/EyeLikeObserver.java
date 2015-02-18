package pkg;


public class EyeLikeObserver {

    // Configurations:
    private static String EngineDataFile = "C:\\Cloud\\eyeLike\\Shared\\data.txt";
    private static String WebServerURL = "https://eyelikei305845trial.hanatrial.ondemand.com/EyeLikeServlet/MainServlet";
    private final int MonitorPeriod = 5 * 1000; // In seconds
    private int maxParticipants = 4;

    public static void main(final String[] args) {
        FileHandler.filname = EngineDataFile;
        ProxyHandler.Init(WebServerURL);
        new EyeLikeObserver().run();
    }

    private void run() {
        LEDHandler.off();
        do {
            try {
                String lastValue = FileHandler.getLastLine();
                Integer percentage = convertToPercentage(lastValue);
                LEDHandler.changeLedState(percentage);
                ProxyHandler.postValue(percentage);

                Thread.sleep(MonitorPeriod);

            } catch (final InterruptedException interrupt) {
                Thread.currentThread().interrupt();
                LEDHandler.off();

            } catch (final Exception e) {
                System.err.println(e.getMessage());
                LEDHandler.off();
            }
        } while (true);
    }

    private Integer convertToPercentage(String value) {
        Integer currentVal = 0;
        try {
            currentVal = (int) ((Float.valueOf(value) / (float) maxParticipants) * 100);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return currentVal;
    }
}
