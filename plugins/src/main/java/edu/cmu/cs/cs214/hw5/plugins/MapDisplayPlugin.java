package edu.cmu.cs.cs214.hw5.plugins;

import edu.cmu.cs.cs214.hw5.framework.core.DisplayDataStructure;
import edu.cmu.cs.cs214.hw5.framework.core.DisplayPlugin;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MapDisplayPlugin implements DisplayPlugin {
    public JPanel visualize(DisplayDataStructure displayDataStructure) {
        final BufferedImage image;
        try {
             image = ImageIO.read(new File("src/main/resources/Connecticut.png"));
             JPanel panel = new JPanel() {
                 @Override
                 protected void paintComponent(Graphics g) {
                     super.paintComponent(g);
                     g.drawImage(image, 0, 0, null);
                 }
             };
             panel.setPreferredSize(new Dimension(800, 583));
             return panel;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getName() {
        return "Connecticut Map";
    }
}
