package pkg;

import java.io.File;

public class FileHandler {

    public static String filname = null;

    public static String getLastLine() {
        File file = new File(filname);
        StringBuilder s = new StringBuilder();
        try {
            Process p = Runtime.getRuntime().exec("tail -" + 1 + " " + file);
            java.io.BufferedReader input = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = input.readLine()) != null) {
                s.append(line + '\n');
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        String val = s.toString().trim();
        System.out.println("Last line in File:" + val);
        return val;
    }
}
