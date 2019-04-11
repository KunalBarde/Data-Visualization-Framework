package edu.cmu.cs.cs214.hw5.framework.core;
import java.math.BigDecimal;

public class DataPoint {
    private String state;
    private String county;
    private Integer startDate;
    private BigDecimal value;

    /**
     * Fills all the fields for datapoint
     * @param state string representing state
     * @param county string representing county
     * @param startDate Integer representing year date
     * @param value numerical value
     */
    public DataPoint(String state, String county, Integer startDate, BigDecimal value) {
        this.state = state;
        this.county = county;
        this.startDate = startDate;
        this.value = value;
    }

    /**
     * Simple getter for state
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * Simple getter for county
     * @return county
     */
    public String getCounty() {
        return county;
    }

    /**
     * Simple getter for year date
     * @return year date
     */
    public Integer getStartDate() {
        return startDate;
    }

    /**
     * simple getter for numerical value of entry
     * @return value of entry
     */
    public BigDecimal getValue() {
        return value;
    }


    @Override
    /**
     * Returns printout of all fields
     */
    public String toString(){
        return getState() + " " + getCounty() + " " + getStartDate().toString() + " " + getValue().toString() + "\n";
    }
}
