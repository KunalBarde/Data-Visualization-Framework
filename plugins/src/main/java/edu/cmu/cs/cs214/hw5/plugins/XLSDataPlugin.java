package edu.cmu.cs.cs214.hw5.plugins;

import edu.cmu.cs.cs214.hw5.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.DataPoint;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class that reads data from EXCEL files .xls or .xlsx. Data should be in
 * the format of state, county, year, value, columns.
 */
public class XLSDataPlugin implements DataPlugin {
    private List<DataPoint> dataPoints;
    private String valueDesc;

    /**
     * Method that returns a List of DataPoint objects from corresponding to
     * the XLS data source.
     *
     * @param source String filepath to the excel file.
     * @return List of DataPoint objects
     */
    public List<DataPoint> extract(String source) {
        JFrame inputDialog = new JFrame();
        valueDesc = JOptionPane.showInputDialog(inputDialog, "Enter Data Value Description:", null);
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

    /**
     * Return the value description of the date type.
     *
     * @return String corresponding to the value description.
     */
    @Override
    public String valueDescription() {
        return valueDesc;
    }

    /**
     * Return the data plugin name.
     *
     * @return String corresponding to the data plugin name.
     */
    public String getName() {
        return "XLS Data Plugin";
    }
}
