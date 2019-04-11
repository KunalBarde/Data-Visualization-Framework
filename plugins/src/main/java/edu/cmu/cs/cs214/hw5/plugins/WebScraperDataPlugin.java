package edu.cmu.cs.cs214.hw5.plugins;

import com.jidesoft.utils.BigDecimalMathUtils;
import edu.cmu.cs.cs214.hw5.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.DataPoint;

import javax.swing.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.poi.ss.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScraperDataPlugin implements DataPlugin {
    private String vDescription;

    public List<DataPoint> extract(String source) {
        List<DataPoint> dataPoints = new ArrayList<>();

        JFrame inputDialog = new JFrame();
        try {
            vDescription = JOptionPane.showInputDialog(inputDialog, "Enter Keywords Matching Value Description:\nE.g.: Veteran", null);
        }catch(Exception e) {
            System.out.println("Option pane failed");
        }
        Document doc = null;
        try{
            doc = Jsoup.parse(new URL(source), 2000);
        }
        catch(Exception e) {return null;}


        Elements resultLinks = doc.select("a[href*=.xl]");
        for (Element link : resultLinks) {
            if(link.text() == "plotly") {continue;}
            try (BufferedInputStream inputStream = new BufferedInputStream(new URL(link.attr("href")).openStream());
                 FileOutputStream fileOS = new FileOutputStream("downloaded_xls.xls"))
            {
                byte data[] = new byte[1024];
                int byteContent;
                while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                    fileOS.write(data, 0, byteContent);
                }
            } catch (IOException e) {
                System.out.println("FAIL: can't download");
            }
        }
        try {
            Workbook workbook = WorkbookFactory.create(new File("downloaded_xls.xls"));
            dataPoints = new ArrayList<>();
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            String comp = ".*(?i)(" + vDescription + ").*";
            for (Row row : sheet) {
                Iterator<Cell> cellIterator = row.cellIterator();
                Iterator<Cell> frowIt = sheet.getRow(sheet.getFirstRowNum()).cellIterator();

                String state = "";
                String county = "";
                String year = "0"; boolean yearT = false;
                String value = "0"; boolean valueT = false;


                int celItCont = 0;
                int frowCont = 0;
                while(cellIterator.hasNext() && frowIt.hasNext()) {
                    String tmpC = dataFormatter.formatCellValue(cellIterator.next());
                    String tmpR = dataFormatter.formatCellValue(frowIt.next());
                    if (tmpR.matches(".*(?i)(state).*")) {
                        if (state == "") {
                            state = tmpC;
                        }
                    } else if (tmpR.matches(".*(?i)(county).*")) {
                        if (county == "") {
                            county = tmpC;
                        }
                    } else if (tmpR.matches(".*(?i)(year).*")) {
                        if (!yearT) {
                            try {
                                int i = Integer.parseInt(tmpC);
                                year = tmpC;
                                yearT = true;
                            } catch (Exception e) {
                            }
                        }
                    }
                    else if (tmpR.matches(comp)) {
                        if (!valueT) {
                            try {
                                int i = Integer.parseInt(tmpC);
                                value = tmpC;
                                valueT = true;
                            } catch (Exception e) {
                            }
                        }
                    }
                }
                if(!yearT) {
                    year =  Integer.toString((new Random().nextInt(100)));
                }
                dataPoints.add(new DataPoint(state, county, Integer.parseInt(year), new BigDecimal(value)));
            }
            workbook.close();
        } catch (Exception e) {
            System.out.println("FAIL: Can't read data");
            return null;
        }
        return dataPoints;
    }

    public String getName() {
        return "Web Scraper XLS";
    }

    @Override
    public String valueDescription() {
        return vDescription;
    }
}
