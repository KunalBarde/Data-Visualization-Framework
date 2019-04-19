package edu.cmu.cs.cs214.plugins.display;

import com.google.gdata.data.DateTime;
import edu.cmu.cs.cs214.hw5.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.core.GameData;
import edu.cmu.cs.cs214.hw5.core.Match;
import edu.cmu.cs.cs214.hw5.core.MatchPlayerInfo;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Bar Chart Plugin that displays when in the day the player plays the game,
 * and how their performance (kills / game) varies during different times of
 * the day.
 */
public class BarChartPlugin implements DisplayPlugin {
    private static final String NAME = "Bar Chart Plugin";
    private static final String IMAGE_FILE = "barchart.jpg";

    private static final int HOURS_IN_DAY = 24;

    private static final int GRAPH_ONE_MAX = 100;
    private static final int GRAPH_ONE_PRIMARY = 20;
    private static final int GRAPH_ONE_SECONDARY = 10;
    private static final int GRAPH_ONE_TERTIARY = 5;
    private static final int GRAPH_TWO_MAX = 10;
    private static final int GRAPH_TWO_PRIMARY = 5;
    private static final int GRAPH_TWO_SECONDARY = 5;
    private static final int GRAPH_TWO_TERTIARY = 1;
    private static final int GRAPH_OFFSET = 100;
    private static final int LABEL_OFFSET = 40;

    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 400;

    private static final int TURN = 270;
    private static final int FONT_SIZE = 12;

    private static final int MAJOR_TICK_WIDTH = 10;
    private static final int SEC_TICK_WIDTH = 5;
    private static final int MINOR_TICK_WIDTH = 2;
    private static final int BAR_WIDTH = 10;

    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

    /**
     * Method that returns the name of the plugin.
     * @return String representing the name of the plugin.
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Get the plugin's display image.
     * @return the plugin's display image.
     */
    @Override
    public BufferedImage getDefaultImage() {
        try {
            return ImageIO.read(new File(IMAGE_DIR + IMAGE_FILE));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Method that creates the two required bar charts, and adds them to a JFrame.
     * @param data is the user specific game data that was passed from the framework.
     */
    @Override
    public void display(GameData data) {
        String[] labels = {"00:00", "01:00", "02:00", "03:00", "04:00", "05:00",
                           "06:00", "07:00", "08:00", "09:00", "10:00", "11:00",
                           "12:00", "13:00", "14:00", "15:00", "16:00", "17:00",
                           "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
        int[] sizes = new int[HOURS_IN_DAY];
        int[] kills = new int[HOURS_IN_DAY];

        String player = data.getUserName();

        List<Match> matchHistory = data.getMatchHistory();
        for (Match match : matchHistory) {
            DateTime startTime = match.getStartTime();
            Calendar dateTime = new GregorianCalendar(GMT);
            dateTime.setTimeInMillis(startTime.getValue());
            sizes[dateTime.get(Calendar.HOUR_OF_DAY)]++;

            List<MatchPlayerInfo> playerInfos = match.getParticipants();
            for (MatchPlayerInfo playerInfo : playerInfos) {
                if (playerInfo.getUsername().equals(player)) {
                    kills[dateTime.get(Calendar.HOUR_OF_DAY)] += playerInfo.getKill();
                }
            }
        }

        for (int i = 0; i < kills.length; i++) {
            if (sizes[i] != 0) {
                kills[i] /= sizes[i];
            }
        }

        ArrayList<Bar> values1 = new ArrayList<>();
        for (int i = 0; i < labels.length; i++) {
            values1.add(new Bar(sizes[i], Color.RED, labels[i]));
        }

        Axis yAxis1 = new Axis(GRAPH_ONE_MAX, GRAPH_ONE_PRIMARY, GRAPH_ONE_SECONDARY, GRAPH_ONE_TERTIARY, "Games Played");
        BarChart barChart1 = new BarChart(values1, yAxis1);
        barChart1.title = "Game Plays v/s Time of Day";
        barChart1.xAxis = "Time of Day";

        barChart1.width = WINDOW_WIDTH;
        barChart1.height = WINDOW_HEIGHT;
        barChart1.leftOffset = GRAPH_OFFSET;
        barChart1.rightOffset = GRAPH_OFFSET;
        barChart1.topOffset = GRAPH_OFFSET;
        barChart1.bottomOffset = GRAPH_OFFSET;
        barChart1.xLabelOffset = LABEL_OFFSET;
        barChart1.yLabelOffset = LABEL_OFFSET;

        ArrayList<Bar> values2 = new ArrayList<>();
        for (int i = 0; i < labels.length; i++) {
            values2.add(new Bar(kills[i], Color.BLUE, labels[i]));
        }

        Axis yAxis2 = new Axis(GRAPH_TWO_MAX, GRAPH_TWO_PRIMARY, GRAPH_TWO_SECONDARY, GRAPH_TWO_TERTIARY, "Kills per Game");
        BarChart barChart2 = new BarChart(values2, yAxis2);
        barChart2.title = "Game Plays v/s Time of Day";
        barChart2.xAxis = "Time of Day";

        barChart2.width = WINDOW_WIDTH;
        barChart2.height = WINDOW_HEIGHT;
        barChart2.leftOffset = GRAPH_OFFSET;
        barChart2.rightOffset = GRAPH_OFFSET;
        barChart2.topOffset = GRAPH_OFFSET;
        barChart2.bottomOffset = GRAPH_OFFSET;
        barChart2.xLabelOffset = LABEL_OFFSET;
        barChart2.yLabelOffset = LABEL_OFFSET;

        JFrame frame = new JFrame("Bar Chart Plugin");
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Game Plays", barChart1);
        tabbedPane.addTab("Kills / Game", barChart2);

        frame.add(tabbedPane);
        frame.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT + GRAPH_OFFSET));
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * copyright.
     * @author Oliver Watkins.
     * (www.blue-walrus.com) All Rights Reserved.
     */
    private class BarChart extends JPanel {
        // offsets (padding of actual chart to its border)
        private int leftOffset;
        private int topOffset;
        private int bottomOffset;
        private int rightOffset;

        // height of X labels (must be significantly smaller than bottomOffset)
        private int xLabelOffset;
        // width of Y labels (must be significantly smaller than leftOffset)
        private int yLabelOffset;

        private String xAxis = "X Axis";
        private String yAxisStr;
        private String title = "My Fruits";

        private int width; //total width of the component
        private int height; //total height of the component

        private Color backgroundColor = Color.WHITE;

        private Font yFont = new Font("Arial", Font.PLAIN, FONT_SIZE);
        private Font xFont = new Font("Arial", Font.BOLD, FONT_SIZE);
        private Font titleFont = new Font("Arial", Font.BOLD, FONT_SIZE);

        private Font yCatFont = new Font("Arial", Font.BOLD, FONT_SIZE);
        private Font xCatFont = new Font("Arial", Font.BOLD, FONT_SIZE);

        private ArrayList<Bar> bars;
        private Axis yAxis;

        /**
         * Construct BarChart.
         * @param bars a number of bars to display.
         * @param yAxis Axis object describes how to display y Axis.
         */
        BarChart(ArrayList<Bar> bars, Axis yAxis) {
            this.bars = bars;
            this.yAxis = yAxis;
            this.yAxisStr = yAxis.yLabel;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g.drawRect(0, 0, width, height);
            g2d.setColor(backgroundColor);
            g.fillRect(0, 0, width, height);
            g2d.setColor(Color.BLACK);

            int heightChart = height - (topOffset + bottomOffset);
            int widthChart = width - (leftOffset + rightOffset);

            // left
            g.drawLine(leftOffset, topOffset, leftOffset, heightChart + topOffset);

            // bottom
            g.drawLine(leftOffset, heightChart + topOffset, leftOffset + widthChart, heightChart + topOffset);

            if (this.yAxis.primaryIncrements != 0) {
                drawTick(heightChart, this.yAxis.primaryIncrements, g, MAJOR_TICK_WIDTH);
            }
            if (this.yAxis.secondaryIncrements != 0) {
                drawTick(heightChart, this.yAxis.secondaryIncrements, g, SEC_TICK_WIDTH);
            }
            if (this.yAxis.tertiaryIncrements != 0) {
                drawTick(heightChart, this.yAxis.tertiaryIncrements, g, MINOR_TICK_WIDTH);
            }

            drawYLabels(heightChart, this.yAxis.primaryIncrements, g);
            drawBars(heightChart, widthChart, g);
            drawLabels(heightChart, widthChart, g);
        }

        private void drawTick(int heightChart, int increment, Graphics g, int tickWidth) {
            int incrementNo = yAxis.maxValue / increment;
            double factor = ((double) heightChart / (double) yAxis.maxValue);
            double incrementInPixel = increment * factor;

            g.setColor(Color.BLACK);

            for (int i = 0; i < incrementNo; i++) {
                int fromTop = heightChart + topOffset - (int) (i * incrementInPixel);
                g.drawLine(leftOffset, fromTop, leftOffset + tickWidth, fromTop);
            }
        }

        /**
         * Method that draws the labels on the Y axis.
         * @param heightChart height of the chart.
         * @param increment separation between successive labels.
         * @param g graphics instance of the chart panel.
         */
        private void drawYLabels(int heightChart, int increment, Graphics g) {
            int incrementNo = yAxis.maxValue / increment;
            double factor = ((double) heightChart / (double) yAxis.maxValue);
            int incrementInPixel = (int) (increment * factor);

            g.setColor(Color.BLACK);
            FontMetrics fm = getFontMetrics(yCatFont);

            for (int i = 0; i < incrementNo; i++) {
                int fromTop = heightChart + topOffset - (i * incrementInPixel);

                String yLabel = "" + (i * increment);

                int widthStr = fm.stringWidth(yLabel);
                int heightStr = fm.getHeight();

                g.setFont(yCatFont);
                g.drawString(yLabel, (leftOffset - yLabelOffset) + (yLabelOffset / 2 - widthStr / 2), fromTop + (heightStr / 2));
            }
        }

        /**
         * Method that draws the bars on the bar chart.
         * @param heightChart height of the chart.
         * @param widthChart width of the chart.
         * @param g graphics instance of the chart panel.
         */
        private void drawBars(int heightChart, int widthChart, Graphics g) {
            int i = 0;
            int barNumber = bars.size();
            int pointDistance = widthChart / (barNumber + 1);

            for (Bar bar : bars) {
                i++;

                double factor = ((double) heightChart / (double) yAxis.maxValue);
                int scaledBarHeight = (int) (bar.value * factor);
                int j = topOffset + heightChart - scaledBarHeight;

                g.setColor(bar.color);
                g.fillRect(leftOffset + (i * pointDistance) - (BAR_WIDTH / 2), j, BAR_WIDTH, scaledBarHeight);

                // draw tick
                g.drawLine(leftOffset + (i * pointDistance),
                        topOffset + heightChart,
                        leftOffset + (i * pointDistance),
                        topOffset + heightChart + 2);

                FontMetrics fm = getFontMetrics(xCatFont);
                int widthStr = fm.stringWidth(bar.name);
                int heightStr = fm.getHeight();

                g.setFont(xCatFont);
                g.setColor(Color.BLACK);

                int xPosition = leftOffset + (i * pointDistance) - (widthStr / 2);
                int yPosition = topOffset + heightChart + xLabelOffset - heightStr / 2;

                // draw tick
                g.drawString(bar.name, xPosition, yPosition);
            }
        }

        /**
         * Method draws labels under the X axis.
         * @param heightChart height of the chart.
         * @param widthChart width of the chart.
         * @param g graphics instance of the chart panel.
         */
        private void drawLabels(int heightChart, int widthChart, Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform oldTransform = g2d.getTransform();

            FontMetrics fmY = getFontMetrics(yFont);
            int yAxisStringWidth = fmY.stringWidth(yAxisStr);
            int yAxisStringHeight = fmY.getHeight();

            FontMetrics fmX = getFontMetrics(xFont);
            int xAxisStringWidth = fmX.stringWidth(yAxisStr);

            FontMetrics fmT = getFontMetrics(titleFont);
            int titleStringWidth = fmT.stringWidth(title);
            int titleStringHeight = fmT.getHeight();

            g2d.setColor(Color.BLACK);
            // draw tick
            g2d.rotate(Math.toRadians(TURN)); //rotates to above out of screen.

            int translateX = -leftOffset - (topOffset + heightChart / 2 + yAxisStringWidth / 2);

            // starts off being "topOffset" off, so subtract that first
            int translateY = -topOffset + (leftOffset - yLabelOffset) / 2 + yAxisStringHeight / 2;

            // pull down, which is basically the left offset, topOffset, then middle it by
            // using chart height and using text height.
            g2d.translate(translateX, translateY);

            g2d.setFont(yFont);
            g2d.drawString(yAxisStr, leftOffset, topOffset);

            // reset
            g2d.setTransform(oldTransform);

            int xAxesLabelHeight = bottomOffset - xLabelOffset;

            // x label
            g2d.setFont(xFont);
            g2d.drawString(xAxis, widthChart / 2 + leftOffset - xAxisStringWidth / 2, topOffset + heightChart + xLabelOffset + xAxesLabelHeight / 2);

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
                    RenderingHints.VALUE_ANTIALIAS_ON);
            // title
            g2d.setFont(titleFont);
            int titleX = (leftOffset + rightOffset + widthChart) / 2 - titleStringWidth / 2;
            int titleY = topOffset / 2 + titleStringHeight / 2;

            g2d.drawString(title, titleX, titleY);
        }
    }

    /**
     * copyright 2014.
     * @author Oliver Watkins (www.blue-walrus.com).
     * All Rights Reserved.
     */
    public class Bar {
        private int value;
        private Color color;
        private String name;

        /**
         * Constructor that initializes an object of Bar type that is used to
         * represent a bar in the bar chart.
         * @param value size of the bar.
         * @param color color of the bar.
         * @param name labels placed below the bar in the chart.
         */
        Bar(int value, Color color, String name) {
            this.value = value;
            this.color = color;
            this.name = name;
        }
    }

    /**
     * copyright 2014.
     * @author Oliver Watkins (www.blue-walrus.com).
     * All Rights Reserved.
     */
    private class Axis {
        private int primaryIncrements;
        private int secondaryIncrements;
        private int tertiaryIncrements;
        private int maxValue;
        private String yLabel;

        /**
         * Initializes the parameters of an axis of the bar graph.
         * @param maxValue the numerical size of the axis.
         * @param primaryIncrements the major, labelled numerical increments of the axis.
         * @param secondaryIncrements the secondary numerical increments of the axis.
         * @param tertiaryIncrements the tertiary numerical increments of the axis.
         * @param name the name label of the axis.
         */
        Axis(int maxValue, int primaryIncrements, int secondaryIncrements, int tertiaryIncrements, String name) {
            this.maxValue = maxValue;
            this.yLabel = name;

            if (primaryIncrements != 0) {
                this.primaryIncrements = primaryIncrements;
            }
            if (secondaryIncrements != 0) {
                this.secondaryIncrements = secondaryIncrements;
            }
            if (tertiaryIncrements != 0) {
                this.tertiaryIncrements = tertiaryIncrements;
            }
        }
    }
}
