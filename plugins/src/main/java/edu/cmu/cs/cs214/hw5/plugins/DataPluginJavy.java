package edu.cmu.cs.cs214.hw5.plugins;

import edu.cmu.cs.cs214.hw5.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.DataPoint;

import java.util.List;

public class DataPluginJavy implements DataPlugin {
    public List<DataPoint> extract(String source) {
        return null;
    }

    public String getName() {

        return "Javy Data 1";
    }

    @Override
    public String valueDescription() {
        return null;
    }
}
