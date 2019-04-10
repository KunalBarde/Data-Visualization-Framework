package edu.cmu.cs.cs214.hw5.plugins;

import edu.cmu.cs.cs214.hw5.framework.core.DisplayDataStructure;
import edu.cmu.cs.cs214.hw5.framework.core.DisplayPlugin;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.io.*;


import edu.cmu.cs.cs214.hw5.framework.core.PData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


/**
 * Plugin to visualize data in the form of a line graph
 * @author kunalbarde
 */
public class LineGraphPlugin implements DisplayPlugin {
    /**
     * Visualize function that returns a JPanel with the graph
     * @param displayDataStructure
     * @return JPanel with the line graph
     */
    @Override
    public JPanel visualize(DisplayDataStructure displayDataStructure) {
        Map<String, List<String>> map = displayDataStructure.getAvailableKeys();
        /*
        Pop-up window
         */
        List<String> stateCountyData = new ArrayList<String>();


        final JFrame parent = new JFrame();

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Serif", Font.PLAIN, 12));


        for (Map.Entry<String, List<String>> entry : map.entrySet())
        {
            String key = entry.getKey();
            for(String s: entry.getValue())
            {
                stateCountyData.add(key + " " + s);
            }
        }

        JList li = new JList(stateCountyData.toArray());
        li.setModel(new DefaultListModel());

        li.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        li.setLayoutOrientation(JList.VERTICAL);

        JScrollPane jp = new JScrollPane(li);

        parent.add(jp);
        parent.pack();
        parent.setVisible(true);


        String str = JOptionPane.showInputDialog(parent,
                "What data do you want to show, Enter: state, county, or both", null);

        boolean st = false, cty = false, both = false;

        if(str.equalsIgnoreCase("state"))
            st = true;
        else if(str.equalsIgnoreCase("county"))
            cty = true;
        else if(str.equalsIgnoreCase("both"))
            both = true;
        else {
            try {
                throw new IOException("Invalid Input");
            } catch (IOException e) {
                System.err.println("Didn't enter state, county, or both");
                e.printStackTrace();
            }
        }

        DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
        List<Map<Integer, BigDecimal>> stateData;
        Map<Integer, BigDecimal> countyData;

        String desc = displayDataStructure.getValueDescription();
        PData dataStructure = displayDataStructure.processFilterData(null);

        for(Map.Entry<String, List<String>> entry: map.entrySet())
        {
            String state = entry.getKey();

            stateData = dataStructure.getStateData(entry.getKey());

            if(st)
            {
                for(Map<Integer, BigDecimal> mp: stateData)
                {
                    for(Map.Entry<Integer, BigDecimal> ent: mp.entrySet())
                    {
                        line_chart_dataset.addValue(ent.getValue(), desc,
                                Integer.toString(ent.getKey()));
                    }
                }
            }else if(cty) {
                List<String> counties = entry.getValue();
                for(String ct: counties)
                {
                    countyData = dataStructure.getCountyData(state, ct);
                    for(Map.Entry<Integer, BigDecimal> ent: countyData.entrySet())
                    {
                        line_chart_dataset.addValue(ent.getValue(), desc, Integer.toString(ent.getKey()));
                    }
                }


            }
        }



        JFreeChart lineChartObject = ChartFactory.createLineChart(desc + " vs. " + "time", "time",
                desc, line_chart_dataset, PlotOrientation.VERTICAL, true, true, false);

        int width = 640, height = 480;

        String filename = desc+"LineChart"+".jpg";

        File lineChartFile = new File(filename);

        JPanel jpanel = null;

        try {
            jpanel = new JPanel();
            ChartUtils.saveChartAsJPEG(lineChartFile ,lineChartObject, width ,height);
            BufferedImage image = ImageIO.read(lineChartFile);
            JLabel pic = new JLabel(new ImageIcon(image));
            jpanel.add(pic);
            jpanel.repaint();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return jpanel;
    }


    public String getName() {
        return "Line Graph Tool";
    }
}
