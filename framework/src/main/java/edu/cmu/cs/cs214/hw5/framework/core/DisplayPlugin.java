package edu.cmu.cs.cs214.hw5.framework.core;

import javax.swing.*;

public interface DisplayPlugin {
    JPanel visualize(DisplayDataStructure displayDataStructure);

    String getName();
}
