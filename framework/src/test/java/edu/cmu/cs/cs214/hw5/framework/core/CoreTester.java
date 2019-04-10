package edu.cmu.cs.cs214.hw5.framework.core;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

public class CoreTester {
    Framework fm;
    int dataIdx;
    int numDataPlugins;
    int dispIdx;
    int numDispPlugins;
    @Before
    public void stubSetUp() {
        fm = new Framework();
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
        DataPoint sampleA = new DataPoint("state1", "county", 2001, new BigDecimal("0"));
        DataPoint sampleB = new DataPoint("state", "county", 2001, new BigDecimal("0"));
        List<DataPoint> dataPointList = null;
        Map<String, Map<String, Map<Integer, BigDecimal>>> tree = new TreeMap<>();
        for (DataPoint dataPoint : dataPointList) {
            tree.computeIfAbsent(dataPoint.getState(), k -> new TreeMap<>());
            tree.get(dataPoint.getState()).computeIfAbsent(dataPoint.getCounty(), k -> new TreeMap<>());
            tree.get(dataPoint.getState()).get(dataPoint.getCounty()).put(dataPoint.getStartDate(), dataPoint.getValue());
        }
    }
}
