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

    public String getState() {
        return state;
    }

    public String getCounty() {
        return county;
    }

    public Integer getStartDate() {
        return startDate;
    }

    public BigDecimal getValue() {
        return value;
    }
}
