package edu.cmu.cs.cs214.hw5.plugins;

import edu.cmu.cs.cs214.hw5.framework.core.DisplayDataStructure;
import edu.cmu.cs.cs214.hw5.framework.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.PData;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MapDisplayPlugin implements DisplayPlugin {
    private PData pData;
    private JSlider slider;
    private ImagePanel imagePanel;

    public JPanel visualize(DisplayDataStructure displayDataStructure) {
        pData = displayDataStructure.processFilterData(null);

        JPanel panel = new JPanel();
        imagePanel = new ImagePanel();

        imagePanel.setPreferredSize(new Dimension(800, 583));
        panel.setLayout(new BorderLayout());
        panel.add(imagePanel, BorderLayout.NORTH);

        List<Integer> timeRange = displayDataStructure.getTimeRanges();
        slider = new JSlider(JSlider.HORIZONTAL, timeRange.get(0), timeRange.get(timeRange.size() - 1), timeRange.get(0));
        slider.setMajorTickSpacing(5);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener((event) -> imagePanel.updatePanel());

        panel.add(slider, BorderLayout.SOUTH);

        return panel;
    }

    public String getName() {
        return "Connecticut Map";
    }

    private class ImagePanel extends JPanel {
        BufferedImage image;

        String middlesex;
        String fairfield;
        String windham;
        String newHaven;
        String tolland;
        String newEngland;
        String litchfield;
        String hartford;

        ImagePanel() {
            try {
                image = ImageIO.read(new File("src/main/resources/Connecticut.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            updatePanel();
        }

        void updatePanel() {
            middlesex = pData.getCountyData("CT", "Middlesex").get(slider.getValue()).toString();
            fairfield = pData.getCountyData("CT", "Fairfield").get(slider.getValue()).toString();
            windham = pData.getCountyData("CT", "Windham").get(slider.getValue()).toString();
            newHaven = pData.getCountyData("CT", "New Haven").get(slider.getValue()).toString();
            tolland = pData.getCountyData("CT", "Tolland").get(slider.getValue()).toString();
            newEngland = pData.getCountyData("CT", "New England").get(slider.getValue()).toString();
            litchfield = pData.getCountyData("CT", "Litchfield").get(slider.getValue()).toString();
            hartford = pData.getCountyData("CT", "Hartford").get(slider.getValue()).toString();

            this.repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, null);
            g.drawString(middlesex, 300, 400);
            //g.drawString(fairfield, 300, 400);
            //g.drawString(newEngland, 300, 400);
            //g.drawString(newHaven, 300, 400);
            //g.drawString(tolland, 300, 400);
            //g.drawString(hartford, 300, 400);
            //g.drawString(windham, 300, 400);
            //g.drawString(litchfield, 300, 400);
        }
    }
}
