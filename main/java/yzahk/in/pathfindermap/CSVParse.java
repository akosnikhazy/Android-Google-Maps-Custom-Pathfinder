/**
 *Created by Ákos Nikházy 2019
 *
 * Modified version of this
 * https://javapapers.com/android/android-read-csv-file/
 *
 */

package yzahk.in.pathfindermap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVParse {
    private static InputStream inputStream;
    private static final String TAG = "MapsActivity";

    public CSVParse(InputStream inputStream) {
        CSVParse.inputStream = inputStream;
    }

    public static List read() {
        List resultList = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                resultList.add(row);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
        return resultList;
    }

    /**
     * this reads Edge CVS file. I was lazy to do with
     * one method
     * @return
     */
    public static Edge[] readEdge() {
        Edge[] resultList;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            int i = 0;

            ArrayList list = new ArrayList();

            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                list.add(new Edge(Integer.parseInt(row[0]), Integer.parseInt(row[1]), Integer.parseInt(row[2]), Integer.parseInt(row[3]), row[4]));
                i++;
            }

            resultList = new Edge[i];

            i = 0;
            for (Object edges : list) {

                resultList[i] = (Edge) edges;
                i++;
            }

        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
        return resultList;
    }
}