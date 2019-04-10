package edu.cmu.cs.cs214.hw5.framework.core;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CoreTester {
    Framework fm;
    int dataIdx;
    int dispIdx;
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

        List<String> dispPlugins = fm.getDisplayPlugins();
        //After initialization, plugins list isn't null
        assertNotEquals(null, dispPlugins);
        //Contains at least 'TestDataPlugin'
        System.out.println(dispPlugins);
        assertTrue(dispPlugins.size() > 0);
        assertTrue(dispPlugins.contains("STUB Display Plugin"));
        dispIdx = dispPlugins.indexOf("STUB Display Plugin");
    }

    @Test
    public void testDataPluginRuns() {
        assertTrue(fm.runDataPlugin(dataIdx, "source"));
        assertFalse(fm.runDataPlugin(-1, "source"));
    }
}
