package edu.cmu.cs.cs214.hw5.plugins;

import edu.cmu.cs.cs214.hw5.framework.core.DisplayDataStructure;
import edu.cmu.cs.cs214.hw5.framework.core.DisplayPlugin;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.io.*;


import edu.cmu.cs.cs214.hw5.framework.core.PData;
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

        String delims = "[,]";
        String[] statesCounties = str.split(delims);

        DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
        List<Map<Integer, BigDecimal>> stateData;
        Map<Integer, BigDecimal> countyData;

        for(int i = 0; i < statesCounties.length-1; i += 2)
        {
            String state = statesCounties[0];
            String county = statesCounties[1];
            PData dataStructure = displayDataStructure.processFilterData(null);
            stateData = dataStructure.getStateData(state);
            countyData = dataStructure.getCountyData(state, county);

            if(st)
            {

            }




        }

        /*
        DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
        line_chart_dataset.addValue( 15 , "schools" , "1970" );
        line_chart_dataset.addValue( 30 , "schools" , "1980" );
        line_chart_dataset.addValue( 60 , "schools" , "1990" );
        line_chart_dataset.addValue( 120 , "schools" , "2000" );
        line_chart_dataset.addValue( 240 , "schools" , "2010" );
        line_chart_dataset.addValue( 300 , "schools" , "2014" );

        JFreeChart lineChartObject = ChartFactory.createLineChart(
                "Schools Vs Years","Year",
                "Schools Count",
                line_chart_dataset, PlotOrientation.VERTICAL,
                true,true,false);

        int width = 640;
        int height = 480;
        File lineChart = new File( "LineChart.jpeg" );
        try {
            ChartUtils.saveChartAsJPEG(lineChart ,lineChartObject, width ,height);
        } catch (IOException e) {
            e.printStackTrace();
        }

         */


        return null;
    }


    public String getName() {
        return "Line Graph Tool";
    }
}
