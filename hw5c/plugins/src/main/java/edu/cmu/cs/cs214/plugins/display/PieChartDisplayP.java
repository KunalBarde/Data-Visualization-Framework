package edu.cmu.cs.cs214.plugins.display;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.cs.cs214.hw5.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.core.GameData;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.*;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PieChartDisplayP implements DisplayPlugin {
    private static final String NAME = "Wins PieChartDisplayP";
    private static final String IMAGE_FILE = "pie_chart.jpg";
    private static final int W_WIDTH = 800;
    private static final int W_HEIGHT = 600;

    // Start from 'src' when 'gradle run'
    // Start from 'plugins' when run on IDE
    String IMAGE_DIR = System.getProperty("user.dir").contains("plugins") ?
            "src/main/resources/images/" : "plugins/src/main/resources/images/";

    /**
     * Used to access the DisplayPlugin - specific name
     * Usually used to set text of gui buttons.
     * @return the specific name of the plugin
     */
    public String getName(){
        return NAME;
    }

    /**
     * generates a new display to show the data in a concise matter to the user
     * @param data is the user specific game data that was passed from the framework
     */
    public void display(GameData data){
        JFrame f = new JFrame();

        double win_rate = data.getWinRate();
        String u_name = data.getUserName();

        System.out.println(data.getTotalWin());
        System.out.println(data.getTotalLose());
        System.out.println(win_rate);

        PieChart chart = new PieChartBuilder().width(W_WIDTH).height(W_HEIGHT).title(getClass().getSimpleName()).build();
        chart.setTitle(u_name + " Win-Rate");
        chart.addSeries("Wins", win_rate);
        chart.addSeries("Losses", 1.0-win_rate);
        f.setContentPane(new XChartPanel<PieChart>(chart));
        f.pack();
        f.setVisible(true);
    }

    /**
     * getDefaultImage provides a default image to the gui such that
     * the user could have an idea of what the result of a display plugin
     * would look like
     * @return the default image for the display
     */
    public BufferedImage getDefaultImage(){
        try {
            BufferedImage image = ImageIO.read(new File(IMAGE_DIR + IMAGE_FILE));
            return image;
        } catch (IOException e) {
            return null;
        }
    }
}