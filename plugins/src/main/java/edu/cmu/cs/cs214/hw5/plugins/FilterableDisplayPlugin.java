package edu.cmu.cs.cs214.hw5.plugins;

import edu.cmu.cs.cs214.hw5.framework.core.DisplayDataStructure;
import edu.cmu.cs.cs214.hw5.framework.core.DisplayPlugin;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class FilterableDisplayPlugin implements DisplayPlugin {
    @Override
    public JPanel visualize(DisplayDataStructure displayDataStructure) {
        JPanel solution = new JPanel();

        JFrame dummyFrame = new JFrame();
        JTextArea textArea = new JTextArea();
        Map<String, List<String>> dip = displayDataStructure.getAvailableKeys();
        for(String state : dip.keySet()) {
            textArea.append(dip.get(state)+"\n");
        }
        textArea.setEditable(false);
        dummyFrame.add(textArea);
        displayDataStructure.getValueDescription();
        displayDataStructure.getTimeRanges();

        dummyFrame.pack();
        dummyFrame.setVisible(true);

        return null;
    }

    @Override
    public String getName() {
        return "Config Charts";
    }
}
