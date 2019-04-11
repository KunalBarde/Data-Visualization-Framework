package edu.cmu.cs.cs214.hw5.framework.core;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.jidesoft.utils.BigDecimalMathUtils;

public class PData {
    private Map<String, Map<String, Map<Integer, BigDecimal>>> internals;
    final private BigDecimal zero = new BigDecimal("0");

    /**
     * Given possibly filtered data from source allows for operations upon it
     * @param data possibly filtered data from source
     */
    PData(Map<String, Map<String, Map<Integer, BigDecimal>>> data) {
        this.internals = data;
    }

    /**
     * returns list of year-value pairs for desired state
     * @param state string of state
     * @return list of data entries representted by direct map year->value
     */
    public List<Map<Integer, BigDecimal>> getStateData(String state){
        Map<String, Map<Integer, BigDecimal>> nip = this.internals.get(state);
        List<Map<Integer, BigDecimal>> answer = new ArrayList<>();
        if(nip == null) {
            return answer;
        }
        for(String county : nip.keySet()) {
            System.out.println(county);
            answer.add(getCountyData(state,county));
        }
        return answer;
    }

    /**
     * returns list of state_county data in form year-value
     * @param state state
     * @param county county
     * @return data entries representted by direct map year->value
     */
    public Map<Integer, BigDecimal> getCountyData(String state, String county){
        Map<Integer, BigDecimal> answer = new HashMap<>();
        if(this.internals.get(state) != null && this.internals.get(state).get(county) != null){
            return new HashMap<>(this.internals.get(state).get(county));
        }
        return answer;
    }

    /**
     * Returns all data associated with a year across all available datum
     * @param targetYear year
     * @return all data associated with a year across all available datum
     */
    public List<BigDecimal> getYearData(Integer targetYear){
        return getYearFlattened(targetYear);
    }

    //________AVG_________
    /**
     * returns avg for given state
     * @param state state
     * @return returns avg for given state
     */
    public BigDecimal getStateAvg(String state){
        Map<String, Map<Integer, BigDecimal>> nip = this.internals.get(state);
        if(nip == null) {
            return zero;
        }
        List<BigDecimal> pasta = getStateFlattened(state);
        if(pasta.size() == 0) {
            return zero;
        }
        return BigDecimalMathUtils.mean(pasta, MathContext.DECIMAL64);
    }

    /**
     * returns avg for given state-county pair
     * @param state state
     * @param county county
     * @return returns avg for given state-county pair
     */
    public BigDecimal getCountyAvg(String state, String county) {
        Map<String, Map<Integer, BigDecimal>> nip = this.internals.get(state);
        if(nip == null || nip.get(county) == null) {
            return zero;
        }
        List<BigDecimal> pasta = getCountyFlattened(state, county);
        if(pasta.size() == 0) {
            return zero;
        }
        return BigDecimalMathUtils.mean(pasta,MathContext.DECIMAL64);
    }

    /**
     * returns avg for a year across all data available
     * @param targetYear year we want as an Integer
     * @return returns avg for a year across all data available
     */
    public BigDecimal getYearAvg(Integer targetYear){
        try {
            return BigDecimalMathUtils.mean(getYearFlattened(targetYear), MathContext.DECIMAL64);
        } catch(Exception e) {return zero;}

    }

    //________STD__________
    /**
     * returns standard dev for given state
     * @param state state
     * @return returns avg for given state
     */
    public BigDecimal getStateStd(String state) {
        Map<String, Map<Integer, BigDecimal>> nip = this.internals.get(state);
        if(nip == null) {
            return zero;
        }
        return BigDecimalMathUtils.stddev(getStateFlattened(state), true, MathContext.DECIMAL64);
    }

    /**
     * returns stddev for given state-county pair
     * @param state state
     * @param county county
     * @return returns avg for given state-county pair
     */
    public BigDecimal getCountyStd(String state, String county) {
        Map<String, Map<Integer, BigDecimal>> nip = this.internals.get(state);
        if(nip == null || nip.get(county) == null) {
            return zero;
        }
        List<BigDecimal> pasta = getCountyFlattened(state, county);
        return BigDecimalMathUtils.stddev(pasta,true,MathContext.DECIMAL64);
    }

    /**
     * returns sttdev for a year across all data available
     * @param targetYear year we want as an Integer
     * @return returns avg for a year across all data available
     */
    public BigDecimal getYearStd(Integer targetYear){
        return BigDecimalMathUtils.stddev(getYearFlattened(targetYear), true, MathContext.DECIMAL64);
    }

    //________SUM___________
    /**
     * returns sum dev for given state
     * @param state state
     * @return returns avg for given state
     */
    public BigDecimal getStateSum(String state) {
        Map<String, Map<Integer, BigDecimal>> nip = this.internals.get(state);
        if(nip == null) {
            return zero;
        }
        return BigDecimalMathUtils.sum(getStateFlattened(state));
    }

    /**
     * returns sum for given state-county pair
     * @param state state
     * @param county county
     * @return returns avg for given state-county pair
     */
    public BigDecimal getCountySum(String state, String county) {
        Map<String, Map<Integer, BigDecimal>> nip = this.internals.get(state);
        if(nip == null || nip.get(county) == null) {
            return zero;
        }
        List<BigDecimal> pasta = getCountyFlattened(state, county);
        return BigDecimalMathUtils.sum(pasta);
    }

    /**
     * returns sum for a year across all data available
     * @param targetYear year we want as an Integer
     * @return returns avg for a year across all data available
     */
    public BigDecimal getYearSum(Integer targetYear){
        return BigDecimalMathUtils.sum(getYearFlattened(targetYear));
    }

    //_____________________________UTILS_____________________________
    private List<BigDecimal> getStateFlattened(String state) {
        Map<String, Map<Integer, BigDecimal>> nip = this.internals.get(state);
        List<BigDecimal> pasta = new ArrayList<>();
        for(String county : nip.keySet()) {
            for(Integer year : nip.get(county).keySet()) {
                pasta.add(nip.get(county).get(year));
            }
        }
        return pasta;
    }

    private List<BigDecimal> getCountyFlattened(String state, String county) {
        Map<String, Map<Integer, BigDecimal>> nip = this.internals.get(state);
        List<BigDecimal> pasta = new ArrayList<>();
        for(Integer year : nip.get(county).keySet()) {
            pasta.add(nip.get(county).get(year));
        }
        return pasta;
    }

    private List<BigDecimal> getYearFlattened(Integer targetYear) {
        List<BigDecimal> pasta = new ArrayList<>();
        for(String state : this.internals.keySet()){
            for(String county : this.internals.get(state).keySet()) {
                for(Integer year : this.internals.get(state).get(county).keySet()) {
                    if(year == targetYear.intValue()){
                        pasta.add(this.internals.get(state).get(county).get(year));
                    }
                }
            }
        }
        return pasta;
    }
}


