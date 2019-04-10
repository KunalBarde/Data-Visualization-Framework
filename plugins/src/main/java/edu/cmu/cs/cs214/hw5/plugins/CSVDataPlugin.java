package edu.cmu.cs.cs214.hw5.plugins;

import edu.cmu.cs.cs214.hw5.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.DataPoint;

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

    private static final String COMMA_DELIMITER = ",";
    private static String str = null;
    private static final int TWO = 2;
    private static final int THREE = 3;


<<<<<<< HEAD

    private List<DataPoint> data;

    /**
     * Empty Contructor for the ServiceLoader
     */
    public CSVDataPlugin()
    {

    }


    /**
     * Function to extract data from the file
     * @return List<DataPoint> representing extracted Data Points
     */

    @Override
    public List<DataPoint> extract() {
=======
    @Override

    public List<DataPoint> extract(String source) {
>>>>>>> bf7a16a9f6004848395ee25bfb6f0d92f0c1879d

        /*
        Pop-up window
         */

<<<<<<< HEAD
        final JFrame parent = new JFrame();
=======
        /*final JFrame parent = new JFrame();
        JButton button = new JButton();
>>>>>>> bf7a16a9f6004848395ee25bfb6f0d92f0c1879d

        parent.pack();
        parent.setVisible(false);

<<<<<<< HEAD
        str = JOptionPane.showInputDialog(parent,
                "Enter csv file path as a string", null);
=======
        button.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                str = JOptionPane.showInputDialog(parent,
                        "Enter csv file path as a string", null);
            }
        });*/
>>>>>>> bf7a16a9f6004848395ee25bfb6f0d92f0c1879d

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
<<<<<<< HEAD
                String[] values = line.split(",");
                DataPoint dataPoint = new DataPoint(values[0], values[1], Integer.parseInt(values[TWO]), new BigDecimal(values[THREE]));
=======
                String[] values = line.split(COMMA_DELIMITER);
                DataPoint dataPoint = new DataPoint(values[0], values[1], Integer.parseInt(values[2]), new BigDecimal(values[3]));
>>>>>>> bf7a16a9f6004848395ee25bfb6f0d92f0c1879d
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
}
