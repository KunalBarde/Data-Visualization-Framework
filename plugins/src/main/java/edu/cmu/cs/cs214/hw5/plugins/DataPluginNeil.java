package edu.cmu.cs.cs214.hw5.plugins;

import edu.cmu.cs.cs214.hw5.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.DataPoint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DataPluginNeil implements DataPlugin {
    private BufferedReader br;

    DataPluginNeil(String fileName) {
        try {
            br = new BufferedReader(new FileReader(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<DataPoint> extract() {
        List<DataPoint> dataPoints = new ArrayList<>();
        try {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                DataPoint dataPoint = new DataPoint(values[0], values[1], Integer.parseInt(values[2]), new BigDecimal(values[3]));
                dataPoints.add(dataPoint);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataPoints;
    }

    public String getName() {
        return "Neil Data";
    }
}