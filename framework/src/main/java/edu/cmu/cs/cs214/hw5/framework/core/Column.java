package edu.cmu.cs.cs214.hw5.framework.core;

import java.util.List;

class Column {
    private String label;
    private List<String> data;

    Column(String label, List<String> data) {
        this.label = label;
        this.data = data;
    }

    String getLabel() {
        return label;
    }

    List<String> getData() {
        return data;
    }
}
