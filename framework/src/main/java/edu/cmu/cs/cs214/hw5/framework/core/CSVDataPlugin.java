package edu.cmu.cs.cs214.hw5.framework.core;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * Plugin for data describing violent crime for a state
 */

public class CSVDataPlugin implements DataPlugin {

    private static final String COMMA_DELIMETER = ",";
    private static String str = null;


    private List<DataPoint> data;

    public CSVDataPlugin()
    {

        final JFrame parent = new JFrame();
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
        });


    }


    @Override

    public List<DataPoint> extract() {
        List<DataPoint> dataPoints = new ArrayList<DataPoint>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(str));
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

    @Override
    public String getName() {
        return "CSV Reader";
    }
}
