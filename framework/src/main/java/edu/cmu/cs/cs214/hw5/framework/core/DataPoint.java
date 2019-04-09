package edu.cmu.cs.cs214.hw5.framework.core;
import java.math.BigDecimal;

public class DataPoint {
    private String state;
    private String county;
    private Integer startDate;
    private BigDecimal value;

    public DataPoint(String state, String county, Integer startDate, BigDecimal value) {
        this.state = state;
        this.county = county;
        this.startDate = startDate;
        this.value = value;
    }

    String getState() {
        return state;
    }

    String getCounty() {
        return county;
    }

    Integer getStartDate() {
        return startDate;
    }

    BigDecimal getValue() {
        return value;
    }
}
