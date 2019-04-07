package edu.cmu.cs.cs214.hw5.framework.core;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
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
    public void runDataPlugin(int index) {
        List<DataPoint> datapts = this.dataPlugins.get(index).extract();
        //part where we modify data from list of data entries to complicated map
        this.displayDataStructure = null;
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

