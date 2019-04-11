package edu.cmu.cs.cs214.hw5.plugins;

import edu.cmu.cs.cs214.hw5.framework.core.Config;
import edu.cmu.cs.cs214.hw5.framework.core.DisplayDataStructure;
import edu.cmu.cs.cs214.hw5.framework.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.PData;
import org.knowm.xchart.*;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FilterableDisplayPlugin implements DisplayPlugin {
    @Override
    public JPanel visualize(DisplayDataStructure displayDataStructure) {
        String vdesc = displayDataStructure.getValueDescription();
        JPanel solution = new JPanel();
        JFrame dummyFrame = new JFrame();


        Map<String, List<String>> dip = displayDataStructure.getAvailableKeys();
        Object[] states = dip.keySet().toArray();
        JList li = new JList(states);
        li.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        li.setLayoutOrientation(JList.VERTICAL);
        JScrollPane jp = new JScrollPane(li);

        JButton rawStates = new JButton("Raw Data");

        rawStates.addActionListener( (event) -> {
            List<String> selectedStates = new ArrayList<>();
            int[] slc = li.getSelectedIndices();
            for(int i = 0; i < slc.length; i++) {
                selectedStates.add((String)states[slc[i]]);
            }

            Map<String, List<String>> configStates = new TreeMap<>();
            for(String state : selectedStates){
                configStates.put(state, dip.get(state));
            }
            Config config1 = new Config(displayDataStructure.getTimeRanges(), configStates);
            PData processedData = displayDataStructure.processFilterData(config1);

            int numStates = selectedStates.size();
            List<JPanel> charts = new ArrayList<JPanel>();

            System.out.println(selectedStates);
            for(int i = 0; i < numStates; i++) {
                XYChart chart = new XYChartBuilder().xAxisTitle("Year").yAxisTitle(vdesc).width(600).height(400).build();
                String state = selectedStates.get(i);
                List<Map<Integer, BigDecimal >> s = processedData.getStateData(state);
                List<Integer> xVal = new ArrayList<>();
                List<BigDecimal> yVal = new ArrayList<>();
                for(Map<Integer, BigDecimal> map : s) {
                    for(Map.Entry<Integer, BigDecimal> d : map.entrySet()) {
                        xVal.add(d.getKey());
                        yVal.add(d.getValue());
                    }
                }
                double[] xs = new double[xVal.size()];
                for(int idx = 0; idx < xVal.size(); idx++) {
                    xs[idx] = (double) xVal.get(idx).intValue();
                }
                double[] ys = new double[yVal.size()];
                for(int idx = 0; idx < yVal.size(); idx++) {
                    ys[idx] = yVal.get(idx).doubleValue();
                }
                XYSeries series = chart.addSeries(state, xs, ys);
                chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
                JPanel newChart = new XChartPanel<>(chart);
                charts.add(newChart);
            }


            for(JPanel p : charts){
                JFrame popup = new JFrame();
                popup.add(p);
                popup.pack();
                popup.setVisible(true);
                popup.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }
            li.clearSelection();
        });

        JButton avgStates = new JButton("Avg + Stddev");
        avgStates.addActionListener( (event) -> {
            List<String> selectedStates = new ArrayList<>();
            int[] slc = li.getSelectedIndices();
            for(int i = 0; i < slc.length; i++) {
                selectedStates.add((String)states[slc[i]]);
            }

            Map<String, List<String>> configStates = new TreeMap<>();
            for(String state : selectedStates){
                configStates.put(state, dip.get(state));
            }
            Config config1 = new Config(displayDataStructure.getTimeRanges(), configStates);
            PData processedData = displayDataStructure.processFilterData(config1);

            int numStates = selectedStates.size();
            System.out.println(selectedStates);

            XYChart chart = new XYChartBuilder().xAxisTitle("State").yAxisTitle(vdesc).width(600).height(400).build();
            for(int i = 0; i < numStates; i++) {
                String state = selectedStates.get(i);
                Double avg = processedData.getStateAvg(state).doubleValue();
                Double std = processedData.getStateStd(state).doubleValue();
                double[] ys = new double[]{avg};
                double[] xs = new double[]{i};
                double[] stds = new double[]{std};
                XYSeries series = chart.addSeries(state, xs, ys, stds);

                chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
            }
            JFrame popup = new JFrame();
            popup.add(new XChartPanel<>(chart));
            popup.pack();
            popup.setVisible(true);
            popup.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        });


        solution.add(jp);
        solution.add(rawStates);
        solution.add(avgStates);
        displayDataStructure.getValueDescription();
        displayDataStructure.getTimeRanges();


        return solution;
    }

    @Override
    public String getName() {
        return "Config Charts";
    }
}
