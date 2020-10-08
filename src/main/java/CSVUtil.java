package es.smvarela;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Helper class to write CSV file from List
 */
public class CSVUtil {

    private static final String SEPARATOR = ",";

    /*
    * Writes the values from List to CSV file
    * */
    public void writeToFile(List<Float> log) throws IOException {
        String outputDirectory = getCurrentDirectory() + "/log/";
        String outputFile = getCurrentTimeStamp() + ".csv";

        try {

            File dir = new File(outputDirectory);
            if (!dir.exists()) {
                if (!dir.mkdir()) throw new IOException();
            }

            File file = new File(dir, outputFile);
            if (!file.exists()) {
                if (!file.createNewFile()) throw new IOException();
            }

            FileWriter fw;
            BufferedWriter bw;

            fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);

            StringBuilder stringBuilder = new StringBuilder();
            int i = 0;
            for (Float fitness : log) {
                stringBuilder.append(i).append(SEPARATOR).append(fitness.toString()).append(System.getProperty("line.separator"));
                i++;
            }

            bw.write(stringBuilder.toString());

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the current directory
     */
    private String getCurrentDirectory() {
        return System.getProperty("user.dir");
    }


    /**
     * Return the formated current timestamp
     */
    private static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd-HHmmss");
        Date now = new Date();
        return sdfDate.format(now);
    }
}
