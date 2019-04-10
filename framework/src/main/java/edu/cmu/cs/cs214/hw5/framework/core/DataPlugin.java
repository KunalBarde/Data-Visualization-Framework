package edu.cmu.cs.cs214.hw5.framework.core;

import java.util.List;

public interface DataPlugin {
    List<DataPoint> extract(String source);

    String getName();

    String valueDescription();
}
