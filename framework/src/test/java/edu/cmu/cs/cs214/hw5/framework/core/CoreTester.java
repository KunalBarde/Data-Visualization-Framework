package edu.cmu.cs.cs214.hw5.framework.core;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CoreTester {

    @Before
    public void stubSetUp() {
        DataPlugin dataStub = new DataPlugin (){
            @Override
            public List<DataPoint> extract() {
                //Test Data
                return null;
            }

            @Override
            public String getName() {
                return "Data STUB";
            }
        };

        DisplayPlugin dispStub = new DisplayPlugin (){
            @Override
            public JPanel visualize(DisplayDataStructure displayDataStructure) {
                return null;
            }

            @Override
            public String getName() {
                return "Display STUB";
            }
        };

        Framework fm = new Framework();
    }

    @Test
    public void Testy1() {
        System.out.println("HEYYYY>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}
