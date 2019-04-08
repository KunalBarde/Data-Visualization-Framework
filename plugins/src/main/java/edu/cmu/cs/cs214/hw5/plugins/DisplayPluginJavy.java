package edu.cmu.cs.cs214.hw5.plugins;


import edu.cmu.cs.cs214.hw5.framework.core.DisplayDataStructure;
import edu.cmu.cs.cs214.hw5.framework.core.DisplayPlugin;

import javax.swing.*;

public class DisplayPluginJavy implements DisplayPlugin {
    public JPanel visualize(DisplayDataStructure displayDataStructure) {
        JPanel panel = new JPanel();
        JButton but = new JButton("KILL ME");
        panel.add(but);
        return panel;
    }

    public String getName() {
        return "Javy Display";
    }
}
