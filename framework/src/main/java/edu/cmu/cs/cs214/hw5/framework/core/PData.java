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

    PData(Map<String, Map<String, Map<Integer, BigDecimal>>> data) {
        this.internals = data;
    }

    /**
     * returns list of year-value pairs for desired state
     * @param state
     * @return
     */
    public List<Map<Integer, BigDecimal>> getStateData(String state){
        Map<String, Map<Integer, BigDecimal>> nip = this.internals.get(state);
        List<Map<Integer, BigDecimal>> answer = new ArrayList<>();
        for(String county : nip.keySet()) {
            answer.add(getCountyData(state,county));
        }
        return answer;
    }

    /**
     * returns list of state_county data in form year-value
     * @param county
     * @return
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
     * @param targetYear
     * @return
     */
    public List<BigDecimal> getYearData(Integer targetYear){
        List<BigDecimal> answer = new ArrayList<>();
        for(String state : this.internals.keySet()){
            for(String county : this.internals.get(state).keySet()) {
                for(Integer year : this.internals.get(state).get(county).keySet()) {
                    if(year == targetYear){
                        answer.add(this.internals.get(state).get(county).get(year));
                    }
                }
            }
        }
        return answer;
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
        return BigDecimalMathUtils.mean(pasta, MathContext.UNLIMITED);
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
        return BigDecimalMathUtils.mean(pasta,MathContext.UNLIMITED);
    }

    /**
     * returns avg for a year across all data available
     * @param targetYear year we want as an Integer
     * @return returns avg for a year across all data available
     */
    public BigDecimal getYearAvg(Integer targetYear){
        return BigDecimalMathUtils.mean(getYearFlattened(targetYear), MathContext.UNLIMITED);
    }

    //________STD__________
    public BigDecimal getStateStd(String state) {
        Map<String, Map<Integer, BigDecimal>> nip = this.internals.get(state);
        if(nip == null) {
            return zero;
        }
        return BigDecimalMathUtils.stddev(getStateFlattened(state), true, MathContext.UNLIMITED);
    }

    public BigDecimal getCountyStd(String state, String county) {
        Map<String, Map<Integer, BigDecimal>> nip = this.internals.get(state);
        if(nip == null || nip.get(county) == null) {
            return zero;
        }
        List<BigDecimal> pasta = getCountyFlattened(state, county);
        return BigDecimalMathUtils.stddev(pasta,true,MathContext.UNLIMITED);
    }

    public BigDecimal getYearStd(Integer targetYear){
        return BigDecimalMathUtils.stddev(getYearFlattened(targetYear), true, MathContext.UNLIMITED);
    }

    //________SUM___________
    public BigDecimal getStateSum(String state) {
        Map<String, Map<Integer, BigDecimal>> nip = this.internals.get(state);
        if(nip == null) {
            return zero;
        }
        return BigDecimalMathUtils.sum(getStateFlattened(state));
    }

    public BigDecimal getCountySum(String state, String county) {
        Map<String, Map<Integer, BigDecimal>> nip = this.internals.get(state);
        if(nip == null || nip.get(county) == null) {
            return zero;
        }
        List<BigDecimal> pasta = getCountyFlattened(state, county);
        return BigDecimalMathUtils.sum(pasta);
    }

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
                    if(year == targetYear){
                        pasta.add(this.internals.get(state).get(county).get(year));
                    }
                }
            }
        }
        return pasta;
    }
}


