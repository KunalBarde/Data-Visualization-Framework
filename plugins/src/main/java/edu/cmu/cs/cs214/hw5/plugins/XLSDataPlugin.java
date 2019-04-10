package edu.cmu.cs.cs214.hw5.plugins;

import edu.cmu.cs.cs214.hw5.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.DataPoint;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XLSDataPlugin implements DataPlugin {
    private List<DataPoint> dataPoints;

    public List<DataPoint> extract(String source) {
        try {
            Workbook workbook = WorkbookFactory.create(new File(source));
            dataPoints = new ArrayList<>();
            Sheet sheet = workbook.getSheetAt(0);

            DataFormatter dataFormatter = new DataFormatter();
            for (Row row : sheet) {
                Iterator<Cell> cellIterator = row.cellIterator();

                String state = dataFormatter.formatCellValue(cellIterator.next());
                String county = dataFormatter.formatCellValue(cellIterator.next());
                String year = dataFormatter.formatCellValue(cellIterator.next());
                String value = dataFormatter.formatCellValue(cellIterator.next());

                dataPoints.add(new DataPoint(state, county, Integer.parseInt(year), new BigDecimal(value)));
            }
            workbook.close();
        } catch (Exception e) {
            return dataPoints;
        }
        return dataPoints;
    }

    public String getName() {
        return "XLS Data Plugin";
    }
}