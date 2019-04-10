package edu.cmu.cs.cs214.hw5.plugins;

import edu.cmu.cs.cs214.hw5.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.DataPoint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Plugin for data describing violent crime for a state
 */

public class CSVDataPlugin implements DataPlugin {

    private static final String COMMA_DELIMITER = ",";
    private static String str = null;

    @Override

    public List<DataPoint> extract(String source) {

        /*
        Pop-up window
         */

        /*final JFrame parent = new JFrame();
        JButton button = new JButton();

        button.setText("CSV Data Plugin");
        parent.add(button);
        parent.pack();
        parent.setVisible(true);

        button.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                str = JOptionPane.showInputDialog(parent,
                        "Enter csv file path as a string", null);
            }
        });*/

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
                String[] values = line.split(COMMA_DELIMITER);
                DataPoint dataPoint = new DataPoint(values[0], values[1], Integer.parseInt(values[2]), new BigDecimal(values[3]));
                dataPoints.add(dataPoint);
            }
        } catch (Exception e) {
            return dataPoints;
        }
        return dataPoints;
    }

    @Override
    public String getName() {
        return "CSV Reader";
    }
}
