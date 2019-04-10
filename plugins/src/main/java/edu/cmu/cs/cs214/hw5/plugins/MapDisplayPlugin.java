package edu.cmu.cs.cs214.hw5.plugins;

import edu.cmu.cs.cs214.hw5.framework.core.DisplayDataStructure;
import edu.cmu.cs.cs214.hw5.framework.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.PData;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Plugin that is used to display the data in a map format for the counties
 * of Connecticut.
 */
class MapDisplayPlugin implements DisplayPlugin {
    private static final int[] IMAGE_SIZE = {800, 583};
    private static final int YEAR_GAP = 5;

    private static final int[] MIDDLESEX_COORDINATES = {460, 330};
    private static final int[] FAIRFIELD_COORDINATES = {110, 420};
    private static final int[] NEWLONDON_COORDINATES = {655, 315};
    private static final int[] NEWHAVEN_COORDINATES = {275, 355};
    private static final int[] TOLLAND_COORDINATES = {555, 110};
    private static final int[] HARTFORD_COORDINATES = {380, 125};
    private static final int[] WINDHAM_COORDINATES = {685, 125};
    private static final int[] LITCHFIELD_COORDINATES = {140, 120};

    private PData pData;
    private JSlider slider;
    private ImagePanel imagePanel;

    /**
     * Method that initializes the map display panel with the data value
     * description label, the map image, and the year selection slider.
     * @param displayDataStructure reference to the data structure received
     * from the framework.
     * @return JPanel with the display visualization.
     */
    public JPanel visualize(DisplayDataStructure displayDataStructure) {
        pData = displayDataStructure.processFilterData(null);

        List<Integer> timeRange = displayDataStructure.getTimeRanges();
        slider = new JSlider(JSlider.HORIZONTAL, timeRange.get(0), timeRange.get(timeRange.size() - 1), timeRange.get(0));
        slider.setMajorTickSpacing(YEAR_GAP);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener((event) -> imagePanel.updatePanel());

        imagePanel = new ImagePanel();
        imagePanel.setPreferredSize(new Dimension(IMAGE_SIZE[0], IMAGE_SIZE[1]));

        JLabel valueDesc = new JLabel(displayDataStructure.getValueDescription());

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(valueDesc, BorderLayout.NORTH);
        panel.add(imagePanel, BorderLayout.CENTER);
        panel.add(slider, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Method that returns the symbolic name of the display plugin.
     * @return String with name of plugin.
     */
    public String getName() {
        return "Connecticut Map";
    }

    /**
     * Class that represents the image panel that contains the map of
     * Connecticut.
     */
    private class ImagePanel extends JPanel {
        private BufferedImage image;

        private String middlesex;
        private String fairfield;
        private String windham;
        private String newHaven;
        private String tolland;
        private String newLondon;
        private String litchfield;
        private String hartford;

        /**
         * Constructor that initialises the Map image with the initial data.
         */
        ImagePanel() {
            try {
                image = ImageIO.read(new File("src/main/resources/Connecticut.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            updatePanel();
        }

        /**
         * Method that updates the Image panel with the data of the year
         * selected by the slider.
         */
        void updatePanel() {
            middlesex = pData.getCountyData("CT", "Middlesex").get(slider.getValue()).toString() + "%";
            fairfield = pData.getCountyData("CT", "Fairfield").get(slider.getValue()).toString() + "%";
            windham = pData.getCountyData("CT", "Windham").get(slider.getValue()).toString() + "%";
            newHaven = pData.getCountyData("CT", "New Haven").get(slider.getValue()).toString() + "%";
            tolland = pData.getCountyData("CT", "Tolland").get(slider.getValue()).toString() + "%";
            newLondon = pData.getCountyData("CT", "New London").get(slider.getValue()).toString() + "%";
            litchfield = pData.getCountyData("CT", "Litchfield").get(slider.getValue()).toString() + "%";
            hartford = pData.getCountyData("CT", "Hartford").get(slider.getValue()).toString() + "%";

            this.repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, null);
            g.drawString(middlesex, MIDDLESEX_COORDINATES[0], MIDDLESEX_COORDINATES[1]);
            g.drawString(fairfield, FAIRFIELD_COORDINATES[0], FAIRFIELD_COORDINATES[1]);
            g.drawString(newLondon, NEWLONDON_COORDINATES[0], NEWLONDON_COORDINATES[1]);
            g.drawString(newHaven, NEWHAVEN_COORDINATES[0], NEWHAVEN_COORDINATES[1]);
            g.drawString(tolland, TOLLAND_COORDINATES[0], TOLLAND_COORDINATES[1]);
            g.drawString(hartford, HARTFORD_COORDINATES[0], HARTFORD_COORDINATES[1]);
            g.drawString(windham, WINDHAM_COORDINATES[0], WINDHAM_COORDINATES[1]);
            g.drawString(litchfield, LITCHFIELD_COORDINATES[0], LITCHFIELD_COORDINATES[1]);
        }
    }
}
