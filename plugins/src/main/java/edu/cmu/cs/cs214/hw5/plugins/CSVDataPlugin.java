package edu.cmu.cs.cs214.hw5.plugins;

import edu.cmu.cs.cs214.hw5.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.DataPoint;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Plugin for CSV data files
 * @author kunalbarde
 */

public class CSVDataPlugin implements DataPlugin {
    private static String str = null;
    private static final int TWO = 2;
    private static final int THREE = 3;

    /**
     * Function to extract data from the file
     * @return List<DataPoint> representing extracted Data Points
     */
    @Override
    public List<DataPoint> extract(String source) {

        JFrame jf = new JFrame();
        str = JOptionPane.showInputDialog(jf, "Enter Y-Axis Label", null);


        /*
        Parse CSV file
         */

        List<DataPoint> dataPoints = null;
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(str));
            dataPoints = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                DataPoint dataPoint = new DataPoint(values[0], values[1], Integer.parseInt(values[TWO]), new BigDecimal(values[THREE]));
                dataPoints.add(dataPoint);
            }
        } catch (Exception e) {
            return dataPoints;
        }
        return dataPoints;
    }

    /**
     * Function to return name of plugin
     * @return String representing the type of plugin
     */
    @Override
    public String getName() {
        return "CSV Reader";
    }

    @Override
    public String valueDescription() {
        return str;
    }
}
