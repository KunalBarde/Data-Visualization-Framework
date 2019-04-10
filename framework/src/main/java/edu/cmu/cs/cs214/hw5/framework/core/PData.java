package edu.cmu.cs.cs214.hw5.framework.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PData {
    private Map<String, Map<String, Map<Integer, BigDecimal>>> internals;

    PData(Map<String, Map<String, Map<Integer, BigDecimal>>> data) {
        this.internals = data;
    }

    /**
     * returns list of year-value pairs for desired state
     * @param state
     * @return
     */
    public List<STtuple> getStateData(String state){
        Map<String, Map<Integer, BigDecimal>> nip = this.internals.get(state);
        List<STtuple> answer = new ArrayList<>();
        for(String county : nip.keySet()) {
            for(Integer year : nip.get(county).keySet()) {
                STtuple tmpObj = new STtuple(year, nip.get(county).get(year));
                answer.add(tmpObj);
            }
        }
        return answer;
    }

    /**
     * returns list of state_county data in form year-value
     * @param county
     * @return
     */
    public Map<Integer, BigDecimal> getCountyData(String state, String county){
        Map<String, Map<Integer, BigDecimal>> nip = this.internals.get(state);
        Map<Integer, BigDecimal> answer = new HashMap<>(this.internals.get(state).get(county));
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

    /**
     * returns avg for given state
     * @param state state
     * @return returns avg for given state
     */
    public BigDecimal getStateAvg(String state){
        Map<String, Map<Integer, BigDecimal>> nip = this.internals.get(state);
        BigDecimal sum = new BigDecimal("0");
        BigDecimal count = new BigDecimal("0");
        for(String county : nip.keySet()) {
            for(Integer year : nip.get(county).keySet()) {
                sum = sum.add(nip.get(county).get(year));
                count = count.add(new BigDecimal("1"));
            }
        }
        BigDecimal ans = sum.divide(count);
        return ans;
    }

    /**
     * returns avg for given state-county pair
     * @param state state
     * @param county county
     * @return returns avg for given state-county pair
     */
    public BigDecimal getCountyAvg(String state, String county) {
        Map<String, Map<Integer, BigDecimal>> nip = this.internals.get(state);
        BigDecimal sum = new BigDecimal("0");
        BigDecimal count = new BigDecimal("0");
        for(Integer year : nip.get(county).keySet()) {
            sum = sum.add(nip.get(county).get(year));
            count = count.add(new BigDecimal("1"));
        }
        BigDecimal ans = sum.divide(count);
        return ans;
    }

    /**
     * returns avg for a year across all data available
     * @param targetYear year we want as an Integer
     * @return returns avg for a year across all data available
     */
    public BigDecimal getYearAvg(Integer targetYear){
        BigDecimal sum = new BigDecimal("0");
        BigDecimal count = new BigDecimal("0");
        for(String state : this.internals.keySet()){
            for(String county : this.internals.get(state).keySet()) {
                for(Integer year : this.internals.get(state).get(county).keySet()) {
                    if(year == targetYear){
                        sum.add(this.internals.get(state).get(county).get(year));
                        count = count.add(new BigDecimal("1"));
                    }
                }
            }
        }
        BigDecimal ans = sum.divide(count);
        return ans;
    }

    /**
     * Given our heirarchy datastructure (possibly filtered)
     * it calculates (Average) (Standard deviation) (Median) (Mode)
     * (Sum) (Min) (Max)
     * @param ftree heirarchy datastructure (possibly filtered)
     * @return labled columns with described operations, where rows correspond to
     * operations and objects
     */
}


