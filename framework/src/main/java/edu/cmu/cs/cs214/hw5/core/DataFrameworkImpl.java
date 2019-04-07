package edu.cmu.cs.cs214.hw5.core;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The framework core implementation.
 */
public class DataFrameworkImpl {
    private DataPlugin dataPlugin;
    private DisplayPlugin displayPlugin;
    private DisplayDataStructure displayDataStructure;

    /**
     * Registers a new {@link DataPlugin} with the framework.
     */
    public void registerDataPlugin(DataPlugin plugin) {
        this.dataPlugin = plugin;
    }

    /**
     * Registers a new {@link DisplayPlugin} with the framework.
     */
    public void registerDisplayPlugin(DisplayPlugin plugin) {
        this.displayPlugin = plugin;
    }

    public void getData() {
        displayDataStructure = new DisplayDataStructure(dataPlugin.extract());
    }

    public void displayData() {
        displayPlugin.visualize(displayDataStructure);
    }

    public Map<String, Map<String, Map<Integer, BigDecimal>>> runDataPlugin(List<DataPoint> dataPointList) {
        Map<String, Map<String, Map<Integer, BigDecimal>>> tree = new TreeMap<>();
        for (DataPoint dataPoint : dataPointList) {
            tree.computeIfAbsent(dataPoint.getState(), k -> new TreeMap<>());
            tree.get(dataPoint.getState()).computeIfAbsent(dataPoint.getCounty(), k -> new TreeMap<>());
            tree.get(dataPoint.getState()).get(dataPoint.getCounty()).put(dataPoint.getStartDate(), dataPoint.getValue());
        }
        return tree;
    }
}

