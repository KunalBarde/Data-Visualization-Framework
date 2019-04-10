package edu.cmu.cs.cs214.hw5.framework.core;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * The edu.cmu.cs.cs214.hw5.framework.core.DataPlugin core implementation.
 */
public class Framework {
    private List<DataPlugin> dataPlugins;
    private List<DisplayPlugin> displayPlugins;
    private DisplayDataStructure displayDataStructure;

    public Framework() {
        ServiceLoader<DataPlugin> dataPins = ServiceLoader.load(DataPlugin.class);
        this.dataPlugins = new ArrayList<>();
        for(DataPlugin in : dataPins){
            System.out.println(in);
            this.dataPlugins.add(in);
        }

        //Loading in current display plugins
        ServiceLoader<DisplayPlugin> dispPins = ServiceLoader.load(DisplayPlugin.class);
        this.displayPlugins = new ArrayList<>();
        for(DisplayPlugin in : dispPins) {
            System.out.println(in);
            this.displayPlugins.add(in);
        }
    }

    /**
     * Returns ordered list of registered data plugins
     */
    public List<String> getDataPlugins() {
        return dataPlugins.stream().map(s -> s.getName()).collect(Collectors.toList());
    }

    /**
     * Returns ordered list of registered display plugins
     */
    public List<String> getDisplayPlugins() {
        return displayPlugins.stream().map(s -> s.getName()).collect(Collectors.toList());
    }

    /**
     * Sets the current extracted data from the plugin selected through the index
     * @param index index of plugin selected by user which maps to our plugin list
     */
    public boolean runDataPlugin(int index, String source) {
        DataPlugin thePlugin = this.dataPlugins.get(index);
        System.out.println(thePlugin.getName());

        List<DataPoint> dataPointList = thePlugin.extract(source);
        if (dataPointList == null) {
            return false;
        }
        Map<String, Map<String, Map<Integer, BigDecimal>>> tree = new TreeMap<>();
        for (DataPoint dataPoint : dataPointList) {
            tree.computeIfAbsent(dataPoint.getState(), k -> new TreeMap<>());
            tree.get(dataPoint.getState()).computeIfAbsent(dataPoint.getCounty(), k -> new TreeMap<>());
            tree.get(dataPoint.getState()).get(dataPoint.getCounty()).put(dataPoint.getStartDate(), dataPoint.getValue());
        }
        DisplayDataStructure struct = new DisplayDataStructureImpl(tree);
        this.displayDataStructure = struct;
        return true;
    }

    /**
     * Returns JPanel that display plugin returns
     * @param index index of plugin selected by user which maps to our plugin list
     */
    public JPanel runDisplayPlugin(int index) {
        JPanel panel = this.displayPlugins.get(index).visualize(this.displayDataStructure);
        return panel;
    }
}

