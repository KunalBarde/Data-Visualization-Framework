package edu.cmu.cs.cs214.hw5.framework.core;

import java.util.List;

public interface DataPlugin {
    /**
     * Should parse the source and return a list of DataPoints
     * @param source source in the form of source path or UML, etc.
     * @return a list of DataPoints to be used by framework
     *          null if errored
     */
    List<DataPoint> extract(String source);

    /**
     * Returns the name of the Plugin to be displayed in framework
     * @return name of the Plugin to be displayed in framework
     */
    String getName();

    /**
     * Returns the description of the numerical data to be analyzed
     * Possibly change on each run of extract
     * @return description of numerical data
     */
    String valueDescription();
}
