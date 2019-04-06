package edu.cmu.cs.cs214.hw5.core;

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
}

