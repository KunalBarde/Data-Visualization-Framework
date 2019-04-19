package edu.cmu.cs.cs214.plugins.data;

import com.google.gdata.data.DateTime;
import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.GameData;
import edu.cmu.cs.cs214.hw5.core.Match;
import edu.cmu.cs.cs214.hw5.core.MatchPlayerInfo;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Plugin to extract
 * data from .xls or .xlsx file
 * @author kunalbarde
 *
 */

public class PluginXLS implements DataPlugin {
    private List<MatchPlayerInfo> playerInfos;

    private Map<String, MatchPlayerInfo> matchPlayerInfoMap; //Map keeping track of username and match info

    private Map<String, List<Match>> playerMatchesMap; //Map keeping track of username and matches played

    private Map<String, Integer> playerLevelMap; //Map keeping track of username and level

    private List<Match> matches;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private static final int SIX = 6;
    private static final int SEVEN = 7;
    private static final int EIGHT = 8;
    private static final int NINE = 9;
    private static final int TEN = 10;



    /**
     * Function to return name of plugin
     * @return String XLS Plugin
     */
    @Override
    public String getName() {
        return "XLS Plugin";
    }

    /**
     * Function to return the expected input type for the plugin
     * @return String Player Name
     */
    @Override
    public String getInputType() {
        return "Player Name";
    }

    /**
     * Function to extract data from a XLS file, and return data associated with given username
     * Expects xls spreadsheet with match data and individual player data
     * @param username returned data will be of this user
     * @return GameData object with data to be passed onto the framework for use
     * @throws IllegalArgumentException if username not found or other malformed issues
     */
    @Override
    public GameData onExtractData(String username) throws IllegalArgumentException {

       /*
       Pop-up window
        */

        JFrame inputDialog = new JFrame();
        String filePath = JOptionPane.showInputDialog(inputDialog, "Enter Path to XLS File:", null);

        matchPlayerInfoMap = new HashMap<String, MatchPlayerInfo>();
        playerMatchesMap = new HashMap<String, List<Match>>();
        playerLevelMap = new HashMap<String, Integer>();  //Map keeping track of username and level

        try
        {
            FileInputStream fi = new FileInputStream(new File(filePath));
            XSSFWorkbook wrk = new XSSFWorkbook(fi);
            XSSFSheet xssfSheet = wrk.getSheetAt(0);

            Iterator<Row> rowIterator = xssfSheet.iterator();

           /*
           Iterate through rows
            */

            while(rowIterator.hasNext())
            {
                //flag notifies us when the next row is a new match

                boolean flag = false;
                Row currRow = rowIterator.next();
                Iterator<Cell> cellIterator = currRow.cellIterator();
                List<MatchPlayerInfo> temp = new ArrayList<MatchPlayerInfo>();
                boolean result = false;
                String date = "";
                String dur = "";
                String type = "";
                String uname = "";
                String character = "";
                int kill = -1;
                int death = -1;
                int assist = -1;
                int level = -1;
                boolean isTeam = false;

                int col = 0;

               /*
               Iterate through columns
                */

                while(cellIterator.hasNext())
                {
                    Cell c = cellIterator.next();
                    if(c.getCellType() == CellType.BOOLEAN && col == 0)
                    {
                        result = c.getBooleanCellValue();
                    }else if(c.getCellType() == CellType.STRING && col == 1){

                        //If the date is different from the previous row then we know it's a new match

                        if(!c.getStringCellValue().equals(date))
                            flag = true;
                        date = c.getStringCellValue();
                    }else if(c.getCellType() == CellType.STRING && col == TWO){
                        dur = c.getStringCellValue();
                    }else if(c.getCellType() == CellType.STRING && col == THREE){
                        type = c.getStringCellValue();
                    }else if(c.getCellType() == CellType.STRING && col == FOUR){
                        uname = c.getStringCellValue();
                    }else if(c.getCellType() == CellType.STRING && col == FIVE){
                        character = c.getStringCellValue();
                    }else if(c.getCellType() == CellType.NUMERIC && col == SIX){
                        kill = (int)c.getNumericCellValue();
                    }else if(c.getCellType() == CellType.NUMERIC && col == SEVEN){
                        death = (int)c.getNumericCellValue();
                    }else if(c.getCellType() == CellType.NUMERIC && col == EIGHT){
                        assist = (int)c.getNumericCellValue();
                    }else if(c.getCellType() == CellType.BOOLEAN && col == NINE){
                        isTeam = c.getBooleanCellValue();
                    }else if(c.getCellType() == CellType.NUMERIC && col == TEN){
                        level = (int)c.getNumericCellValue();
                        playerLevelMap.put(uname, level);
                    }
                    col++;

                }


                MatchPlayerInfo mp = new MatchPlayerInfo(uname, character, kill, death, assist, isTeam);

                matchPlayerInfoMap.put(uname, mp);

                playerInfos.add(mp);
                temp.add(mp);

                DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:s");
                Date res = df.parse(date);

                Date res1 = df.parse(dur);

                Match m = new Match(type, temp, new DateTime(res), new DateTime(res1), result);

               /*
               If new match then add
                */
                if(flag)
                {
                    matches.add(new Match(type, temp, new DateTime(res), new DateTime(res1), result));
                }
            }

        } catch (IOException e) {
            System.err.println("Error opening file");
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Error parsing the file");
            e.printStackTrace();
        }

        MatchPlayerInfo data = matchPlayerInfoMap.get(username);

        int wins = 0;
        int losses = 0;

        for(Map.Entry<String, List<Match>> entry: playerMatchesMap.entrySet())
        {
            if(entry.getKey().equals(username))
            {
                List<Match> temp = entry.getValue();
                for(Match m: temp)
                {
                    if(m.getGameResult())
                        wins++;
                    else
                        losses++;
                }
            }
        }

        return new GameData(username, playerLevelMap.get(username), data.getKill(), data.getDeath(),
                data.getAssist(), wins, losses, playerMatchesMap.get(username));
    }
}

