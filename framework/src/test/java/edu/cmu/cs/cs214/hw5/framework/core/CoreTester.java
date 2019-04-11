package edu.cmu.cs.cs214.hw5.framework.core;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.Array;
import java.util.*;

import static org.junit.Assert.*;

public class CoreTester {
    Framework fm;
    int dataIdx;
    int numDataPlugins;
    int dispIdx;
    int numDispPlugins;
    DisplayDataStructure dds;
    Map<String, List<String>> keys;
    List<Integer> times;
    final BigDecimal zero = new BigDecimal("0");
    @Before
    public void stubSetUp() {
        fm = new Framework();
    }

    @Before
    public void ddsSetUp() {
        DataPoint sampleA = new DataPoint("FL", "county", 2001, new BigDecimal("1"));
        DataPoint sampleB = new DataPoint("FL", "county", 2002, new BigDecimal("2"));
        DataPoint sampleC = new DataPoint("CT", "midsex", 2001, new BigDecimal("10"));
        DataPoint sampleD = new DataPoint("CT", "midsex", 2002, new BigDecimal("20"));
        DataPoint sampleE = new DataPoint("MA", "corn", 2001, new BigDecimal("100"));
        DataPoint sampleF = new DataPoint("MA", "herd", 2001, new BigDecimal("200"));

        List<DataPoint> dataPointList = new ArrayList<DataPoint>(Arrays.asList(sampleA, sampleB, sampleC,sampleD,sampleE
        ,sampleF));

        Map<String, Map<String, Map<Integer, BigDecimal>>> tree = new TreeMap<>();
        for (DataPoint dataPoint : dataPointList) {
            tree.computeIfAbsent(dataPoint.getState(), k -> new TreeMap<>());
            tree.get(dataPoint.getState()).computeIfAbsent(dataPoint.getCounty(), k -> new TreeMap<>());
            tree.get(dataPoint.getState()).get(dataPoint.getCounty()).put(dataPoint.getStartDate(), dataPoint.getValue());
        }

        dds = new DisplayDataStructureImpl(tree, "label");
        keys = dds.getAvailableKeys();
        times = dds.getTimeRanges();
    }

    @Test
    public void testSourceLoader() {
        List<String> dataPlugins = fm.getDataPlugins();
        //After initialization, plugins list isn't null
        assertNotEquals(null, dataPlugins);
        //Contains at least 'TestDataPlugin'
        assertTrue(dataPlugins.size() > 0);
        assertTrue(dataPlugins.contains("Data STUB"));
        dataIdx = dataPlugins.indexOf("Data Stub");
        numDataPlugins = dataPlugins.size();

        List<String> dispPlugins = fm.getDisplayPlugins();
        //After initialization, plugins list isn't null
        assertNotEquals(null, dispPlugins);
        //Contains at least 'TestDataPlugin'
        assertTrue(dispPlugins.size() > 0);
        assertTrue(dispPlugins.contains("Display STUB"));
        dispIdx = dispPlugins.indexOf("Display STUB");
        numDispPlugins = dispPlugins.size();
    }

    @Test
    public void testDataPluginRunner() {
        assertTrue(fm.runDataPlugin(dataIdx, "source"));
        assertFalse(fm.runDataPlugin(dataIdx, "badsource"));
        assertFalse(fm.runDataPlugin(-1, "source"));
        assertFalse(fm.runDataPlugin(numDataPlugins+1, "source"));
    }

    @Test
    public void testDispPluginRunner() {
        assertNotNull(fm.runDisplayPlugin(dataIdx));
        assertNull(fm.runDisplayPlugin(-1));
        assertNull(fm.runDisplayPlugin(numDispPlugins+1));
    }

    @Test
    public void testDisplayStructure() {
        //Getter tests
        assertTrue(arrayEquals(new ArrayList<>(keys.keySet()), new ArrayList<String>(Arrays.asList("FL", "CT", "MA"))));

        assertEquals(2, times.size());
        assertTrue(arrayEquals(times, new ArrayList<Integer>(Arrays.asList(2001, 2002))));


        String descrp = dds.getValueDescription();
        assertTrue(descrp.equals("label"));

        //Config testing
        Config cfg1 = new Config(times, keys); //remove everything
        PData set1 = dds.processFilterData(cfg1); //should be empty
        assertTrue(set1.getStateData("FL").size() == 1);
        assertEquals(2,set1.getCountyData("FL", "county").size());
        assertEquals(new BigDecimal("1.5"),set1.getCountyAvg("FL", "county"));
        double std = set1.getCountyStd("FL", "county").doubleValue();
        assertTrue(0.7 < std && std < 0.71);
        assertEquals(new BigDecimal("3"),set1.getCountySum("FL", "county"));
        assertEquals(4, set1.getYearData(2001).size());
        assertEquals(2, set1.getYearData(2002).size());
        assertEquals(0, set1.getYearData(2000).size());
    }

    @Test
    /**
     * Tests if configuration that removes county works
     */
    public void testCountyConfig() {
        List<Integer> times1 = new ArrayList<Integer>(Arrays.asList(2001));
        Map<String, List<String>> keys1 = new TreeMap<>();
        for(String state : new ArrayList<String>(Arrays.asList("MA"))){
            keys1.put(state, keys.get(state));
            keys1.get(state).remove("corn");
        }
        Config cfg1 = new Config(times1,keys1);
        PData set1 = dds.processFilterData(cfg1);
        assertTrue(set1.getStateData("MA").size() == 1);
        assertEquals(new BigDecimal("200"),set1.getCountyAvg("MA", "herd"));
        assertEquals(new BigDecimal("0"),set1.getCountyStd("MA", "herd"));
        assertEquals(new BigDecimal("200"),set1.getCountySum("MA", "herd"));
        assertEquals(new BigDecimal("200"), set1.getStateAvg("MA"));

        assertTrue(set1.getStateData("FL").size() == 0);
        assertTrue(set1.getCountyData("FL", "county").size() == 0);
        assertTrue(set1.getStateData("CT").size() == 0);
        assertTrue(set1.getStateData("Fake").size() == 0);

        assertEquals(1, set1.getYearData(2001).size());
        assertEquals(0, set1.getYearData(2002).size());
    }

    @Test
     /**
     * Tests if configuration that removes years works
     */
    public void testYearConfig() {
        //No years allowed
        List<Integer> times1 = new ArrayList<Integer>(Arrays.asList());
        Map<String, List<String>> keys1 = keys;
        Config cfg1 = new Config(times1,keys1);
        PData set1 = dds.processFilterData(cfg1);
        assertEquals(0, set1.getStateData("FL").size());
        assertEquals(0,set1.getStateData("CT").size());
        assertEquals(0,set1.getStateData("MA").size());

        assertEquals(0, set1.getYearData(2001).size());
        assertEquals(0, set1.getYearData(2002).size());
        assertEquals(new BigDecimal("0"), set1.getYearAvg(2001));
        assertEquals(new BigDecimal("0"), set1.getYearAvg(2002));

        times1 = new ArrayList<Integer>(Arrays.asList(2001));
        cfg1 = new Config(times1,keys1);
        set1 = dds.processFilterData(cfg1);

        assertEquals(1,set1.getStateData("FL").size());
        assertEquals(1,set1.getStateData("CT").size());
        assertEquals(2,set1.getStateData("MA").size());

        assertEquals(4, set1.getYearData(2001).size());
        assertEquals(0, set1.getYearData(2002).size());
        double std = set1.getYearAvg(2001).doubleValue();
        assertTrue(77.70 < std && std < 77.80);
    }


    //__UTILS__
    private <E> boolean  arrayEquals(List<E> A, List<E> B) {
        if(A.size() != B.size()){
            return false;
        }
        for(E elem : A) {
            if(!B.contains(elem)) {
                return false;
            }
        }
        return true;
    }
}
