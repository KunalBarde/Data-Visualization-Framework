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
    @Before
    public void stubSetUp() {
        fm = new Framework();
    }

    @Before
    public void ddsSetUp() {
        DataPoint sampleA = new DataPoint("FL", "county", 2001, new BigDecimal("1"));
        DataPoint sampleB = new DataPoint("FL", "county", 2002, new BigDecimal("2"));
        DataPoint sampleC = new DataPoint("CT", "midsex", 2001, new BigDecimal("1"));
        DataPoint sampleD = new DataPoint("CT", "midsex", 2002, new BigDecimal("2"));
        DataPoint sampleE = new DataPoint("MA", "corn", 2001, new BigDecimal("1"));
        DataPoint sampleF = new DataPoint("MA", "herd", 2001, new BigDecimal("2"));

        List<DataPoint> dataPointList = new ArrayList<DataPoint>(Arrays.asList(sampleA, sampleB, sampleC,sampleD,sampleE
        ,sampleF));

        Map<String, Map<String, Map<Integer, BigDecimal>>> tree = new TreeMap<>();
        for (DataPoint dataPoint : dataPointList) {
            tree.computeIfAbsent(dataPoint.getState(), k -> new TreeMap<>());
            tree.get(dataPoint.getState()).computeIfAbsent(dataPoint.getCounty(), k -> new TreeMap<>());
            tree.get(dataPoint.getState()).get(dataPoint.getCounty()).put(dataPoint.getStartDate(), dataPoint.getValue());
        }

        dds = new DisplayDataStructureImpl(tree, "label");
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
        System.out.println(dispPlugins);
        assertTrue(dispPlugins.size() > 0);
        assertTrue(dispPlugins.contains("STUB Display Plugin"));
        dispIdx = dispPlugins.indexOf("STUB Display Plugin");
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
        Map<String, List<String>> keys = dds.getAvailableKeys();
        assertTrue(arrayEquals(new ArrayList<>(keys.keySet()), new ArrayList<String>(Arrays.asList("FL", "CT", "MA"))));

        List<Integer> times = dds.getTimeRanges();
        assertEquals(2, times.size());
        assertTrue(arrayEquals(times, new ArrayList<Integer>(Arrays.asList(2001, 2002))));


        String descrp = dds.getValueDescription();
        assertTrue(descrp.equals("label"));


        //Config testing
        Config cfg1 = new Config(times, keys); //remove everything
        PData set1 = dds.processFilterData(cfg1); //should be empty
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
