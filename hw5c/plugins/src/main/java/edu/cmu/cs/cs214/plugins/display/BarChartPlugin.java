package edu.cmu.cs.cs214.plugins.display;

import edu.cmu.cs.cs214.hw5.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.core.GameData;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BarChartPlugin implements DisplayPlugin {
    private static final String NAME = "Bar Chart Plugin";
    private static final String IMAGE_FILE = "barchart.png";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public BufferedImage getDefaultImage() {
        try {
            return ImageIO.read(new File(IMAGE_DIR + IMAGE_FILE));
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void display(GameData data) {
        String[] labels = {"00:00", "01:00", "02:00", "03:00", "04:00", "05:00",
                           "06:00", "07:00", "08:00", "09:00", "10:00", "11:00",
                           "12:00", "13:00", "14:00", "15:00", "16:00", "17:00",
                           "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};



    }

    private class BarChart extends JPanel {
        private double[] values;
        private String[] labels;
        private Color[] colors;
        private String title;

        public BarChart(double[] values, String[] labels, Color[] colors, String title) {
            this.labels = labels;
            this.values = values;
            this.colors = colors;
            this.title = title;
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (values == null || values.length == 0) {
                return;
            }

            double minValue = 0;
            double maxValue = 0;
            for (int i = 0; i < values.length; i++) {
                if (minValue > values[i]) {
                    minValue = values[i];
                }
                if (maxValue < values[i]) {
                    maxValue = values[i];
                }
            }

            Dimension dim = getSize();
            int panelWidth = dim.width;
            int panelHeight = dim.height;
            int barWidth = panelWidth / values.length;

            Font titleFont = new Font("Book Antiqua", Font.BOLD, 15);
            FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);

            Font labelFont = new Font("Book Antiqua", Font.PLAIN, 14);
            FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);

            int titleWidth = titleFontMetrics.stringWidth(title);
            int stringHeight = titleFontMetrics.getAscent();
            int stringWidth = (panelWidth - titleWidth) / 2;
            g.setFont(titleFont);
            g.drawString(title, stringWidth, stringHeight);

            int top = titleFontMetrics.getHeight();
            int bottom = labelFontMetrics.getHeight();
            if (maxValue == minValue) {
                return;
            }
            double scale = (panelHeight - top - bottom) / (maxValue - minValue);
            stringHeight = panelHeight - labelFontMetrics.getDescent();
            g.setFont(labelFont);
            for (int j = 0; j < values.length; j++) {
                int valueP = j * barWidth + 1;
                int valueQ = top;
                int height = (int) (values[j] * scale);
                if (values[j] >= 0) {
                    valueQ += (int) ((maxValue - values[j]) * scale);
                } else {
                    valueQ += (int) (maxValue * scale);
                    height = -height;
                }

                g.setColor(colors[j]);
                g.fillRect(valueP, valueQ, barWidth - 2, height);
                g.setColor(Color.black);
                g.drawRect(valueP, valueQ, barWidth - 2, height);

                int labelWidth = labelFontMetrics.stringWidth(labels[j]);
                stringWidth = j * barWidth + (barWidth - labelWidth) / 2;
                g.drawString(labels[j], stringWidth, stringHeight);
            }
        }
    }
}
