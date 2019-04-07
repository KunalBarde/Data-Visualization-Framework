package edu.cmu.cs.cs214.hw5.framework;

import edu.cmu.cs.cs214.hw5.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.framework.gui.FrameworkGui;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () ->
                {
                    FrameworkGui vis = new FrameworkGui();
                    vis.setVisible(true);
                }
        );

    }
}