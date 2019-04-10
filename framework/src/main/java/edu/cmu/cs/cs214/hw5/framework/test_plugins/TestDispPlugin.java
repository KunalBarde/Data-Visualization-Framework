package edu.cmu.cs.cs214.hw5.framework.test_plugins;


import edu.cmu.cs.cs214.hw5.framework.core.DisplayDataStructure;
import edu.cmu.cs.cs214.hw5.framework.core.DisplayPlugin;

import javax.swing.*;

public class TestDispPlugin implements DisplayPlugin {
    public JPanel visualize(DisplayDataStructure displayDataStructure) {
        JPanel panel = new JPanel();
        JButton but = new JButton("I'm a Button");
        panel.add(but);
        return panel;
    }

    public String getName() {
        return "STUB Display Plugin";
    }
}
