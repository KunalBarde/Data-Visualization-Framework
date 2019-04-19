package edu.cmu.cs.cs214.plugins.display;

import com.google.gdata.data.DateTime;
import edu.cmu.cs.cs214.hw5.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.core.GameData;
import edu.cmu.cs.cs214.hw5.core.Match;
import edu.cmu.cs.cs214.hw5.core.MatchPlayerInfo;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Plugin to show kill/assist/death data over time
 * Visualizes player performance based on kda
 * @author kunalbarde
 */

public class LineGraphPerformancePlugin extends JFrame implements DisplayPlugin {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private static final int TWO = 2;
    private static final float THICCNESS = 2.0f;
    private static final float THICCNESS1 = 3.0f;
    private static final float THICCNESS2 = 4.0f;
    private static final double FLOAT1 = 2.0;
    private static final double FLOAT2 = 3.0;
    private static final double FLOAT3 = 4.0;
    private static final double FLOAT4 = 5.0;
    private static final double FLOAT5 = 6.0;
    private static final double FLOAT6 = 7.0;


    /**
     * Default constructor
     */
    public LineGraphPerformancePlugin()
    {

    }

    /**
     * Private constructor to be called within Swing.invokeLater
     * @param chart representing the JFreeChart that is constructed
     */
    private LineGraphPerformancePlugin(JFreeChart chart)
    {
        super("Kill/Death/Assist/Win Over Time For a Player");
        JPanel chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);

        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    /**
     * Function to return the name of the plugin
     * @return String representing the name of the plugin
     */
    @Override
    public String getName() {
        return "Line Graph Performance Plugin";
    }

    /**
     * Function to convert unix epoch time in milliseconds to days
     * @param milliseconds the value we want to convert
     * @return double representing the value in days
     */
    private double convertToDays(long milliseconds)
    {
        return (double)TimeUnit.MILLISECONDS.toDays(milliseconds);
    }

    /**
     * Function to display data through the framework
     * @param data is the user specific game data that was passed from the framework
     */
    @Override
    public void display(GameData data) {

        String username = data.getUserName();


        XYSeriesCollection dataSet = new XYSeriesCollection();
        XYSeries killData = new XYSeries("kills");
        XYSeries deathData = new XYSeries("deaths");
        XYSeries assistData = new XYSeries("assists");





        List<Match> matchesPlayed = data.getMatchHistory();
        int count = 0;

        Match firstMatch = matchesPlayed.get(0);
        DateTime startTime = firstMatch.getStartTime();
        double start = convertToDays(startTime.getValue());

        List<Double> timeValues = new ArrayList<Double>();
        List<Double> killValues = new ArrayList<Double>();
        List<Double> assistValues = new ArrayList<Double>();
        List<Double> deathValues = new ArrayList<Double>();
        List<Boolean> winValues = new ArrayList<Boolean>();

        for(Match m: matchesPlayed)
        {
            double time = convertToDays(m.getStartTime().getValue());
            double dif = time - start;

            timeValues.add(dif);
            winValues.add(m.getGameResult());

            List<MatchPlayerInfo> infoList = m.getParticipants();

            for(MatchPlayerInfo mi : infoList)
            {
                String uname = mi.getUsername();
                if(uname.equalsIgnoreCase(username))
                {
                    killValues.add((double)mi.getKill());
                    assistValues.add((double)mi.getAssist());
                    deathValues.add((double)mi.getDeath());
                }

            }
        }

       /*
       Add data points to series then to dataSet
        */

        for(Double tValue: timeValues)
        {
            for(Double kill: killValues)
            {
                killData.add(tValue, kill);
            }
            for(Double death: deathValues)
            {
                deathData.add(tValue, death);
            }
            for(Double assist: assistValues)
            {
                assistData.add(tValue, assist);
            }
        }

        dataSet.addSeries(killData);
        dataSet.addSeries(deathData);
        dataSet.addSeries(assistData);

        String chartTitle = "Kill/Death/Assist Over Time";
        String xAxisLabel = "Time";
        String yAxisLabel = "Number of kills/deaths/assists";

        JFreeChart lineGraph = ChartFactory.createXYLineChart(chartTitle, xAxisLabel, yAxisLabel, dataSet);

        XYPlot p = lineGraph.getXYPlot();

       /*
       Modeled after code here:
       https://steemit.com/visualization/@datatreemap/visualize-a-multiple-lines-graph-with-jfreechart-in-java
        */
        XYLineAndShapeRenderer rend = new XYLineAndShapeRenderer();

        //sets paint color for each series

        rend.setSeriesPaint(0, Color.GREEN);
        rend.setSeriesPaint(1, Color.RED);
        rend.setSeriesPaint(TWO, Color.YELLOW);

        //sets thickness for the series

        rend.setSeriesStroke(0, new BasicStroke(THICCNESS2));
        rend.setSeriesStroke(1, new BasicStroke(THICCNESS1));
        rend.setSeriesStroke(TWO, new BasicStroke(THICCNESS));

        p.setOutlinePaint(Color.BLUE);
        p.setOutlineStroke(new BasicStroke(THICCNESS));

        p.setRenderer(rend);
        p.setBackgroundPaint(Color.DARK_GRAY);
        p.setRangeGridlinesVisible(true);

        p.setRangeGridlinePaint(Color.BLACK);

        p.setDomainGridlinesVisible(true);

        p.setDomainGridlinePaint(Color.BLACK);

        SwingUtilities.invokeLater(new Runnable() {
            /**
             * FUNCTION
             */
            @Override
            public void run() {
                new LineGraphPerformancePlugin(lineGraph).setVisible(true);
            }
        });

    }

    /**
     * Function to get a BufferedImage from the graph, for the default image of the plugin
     * @return BufferedImage representing the image representation of the graph
     */

    @Override
    public BufferedImage getDefaultImage() {
        String filename = "LineGraphPerformance.jpg";
        File f = new File(filename);
        BufferedImage result = null;


        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("dots");
        XYSeries series2 = new XYSeries("bananas");

        series1.add(FLOAT1, FLOAT2);
        series1.add(FLOAT3,FLOAT1);
        series1.add(FLOAT2,FLOAT4);

        series2.add(FLOAT1,FLOAT6);
        series2.add(FLOAT2,FLOAT5);
        series2.add(FLOAT3, FLOAT4);

        dataset.addSeries(series1);
        dataset.addSeries(series2);

        JFreeChart test = ChartFactory.createXYLineChart("Demo", "X", "Y", dataset);
        XYPlot plot = test.getXYPlot();

        XYLineAndShapeRenderer testRend = new XYLineAndShapeRenderer();
        testRend.setSeriesPaint(0, Color.BLUE);
        testRend.setSeriesPaint(1, Color.green);

        testRend.setSeriesStroke(0, new BasicStroke(THICCNESS2));
        testRend.setSeriesStroke(1, new BasicStroke(THICCNESS1));

        plot.setOutlinePaint(Color.BLUE);
        plot.setOutlineStroke(new BasicStroke(THICCNESS));

        plot.setRenderer(testRend);
        plot.setBackgroundPaint(Color.DARK_GRAY);
        plot.setRangeGridlinesVisible(true);

        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);

        plot.setDomainGridlinePaint(Color.BLACK);

        try{
            ChartUtils.saveChartAsJPEG(f, test, WIDTH, HEIGHT);
            result = ImageIO.read(f);
        }catch(IOException e){
            e.printStackTrace();

        }

        return result;
    }
}

