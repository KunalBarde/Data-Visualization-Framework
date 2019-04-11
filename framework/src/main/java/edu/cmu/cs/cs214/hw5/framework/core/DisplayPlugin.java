package edu.cmu.cs.cs214.hw5.framework.core;

import javax.swing.*;

public interface DisplayPlugin {
    /**
     * Creates a visualization based on the display datastructure
     * Framework relies on the return of a JPanel to be displayed
     * as a different frame on the framework window.
     * @param displayDataStructure API useful for filtering and allows use of
     *                             PData's analysis API
     * @return JPanel representing the visualization. As complex or simple as desired
     */
    JPanel visualize(DisplayDataStructure displayDataStructure);

    /**
     * Returns name the plugin will show in Framework
     * @return string representing name of framework
     */
    String getName();
}
