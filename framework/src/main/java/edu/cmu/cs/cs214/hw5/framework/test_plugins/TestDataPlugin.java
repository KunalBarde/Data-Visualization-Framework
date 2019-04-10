package edu.cmu.cs.cs214.hw5.framework.test_plugins;

import edu.cmu.cs.cs214.hw5.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.DataPoint;

import java.util.ArrayList;
import java.util.List;

public class TestDataPlugin implements DataPlugin {
    @Override
    public List<DataPoint> extract(String source) {
        //Test Data
        if(source != "source"){return null;}
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "Data STUB";
    }
}
