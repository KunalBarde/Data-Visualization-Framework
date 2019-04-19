package edu.cmu.cs.cs214.plugins.data;

import com.google.gdata.data.DateTime;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.GameData;
import edu.cmu.cs.cs214.hw5.core.Match;
import edu.cmu.cs.cs214.hw5.core.MatchPlayerInfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

/**
 * PUBG API Plugin is a data plugin that extracts user specific data with PUBG API
 * and provides access to the framework for further use.
 * Workflow:
 * - extract player
 * - extract list of player's recent matches
 * - extract each recent match's detailed data
 * - create MatchPlayerInfo, Match, and GameData class instance
 * - return GameData instance
 */
public class PUBGAPIPlugin implements DataPlugin {

    /**
     * Class used to parse player info JSON.
     */
    private class JSONPlayerData {
        private Player[] data;
        private transient Class links;
        private transient Class meta;

        /**
         * Class used to parse player data JSON.
         */
        private class Player {
            private transient String type;
            private String id;
            private transient Class attributes;
            private MatchData relationships;
            private transient Class links;

            /**
             * Class used to parse player match data JSON.
             */
            private class MatchData {
                private transient Class assets;
                private MatchCodes matches;
                private transient Class links;
            }
        }
    }

    /**
     * Class used to parse id info of each match by player.
     */
    private class MatchCodes {
        private Code[] data;

        /**
         * Class used to parse match codes JSON.
         */
        private class Code {
            private transient String type;
            private String id;
        }
    }

    /**
     * Class used to parse match info JSON.
     */
    private class JSONMatch {
        private DataPt data;
        private transient Class relationships;
        private IncPt[] included;
        private transient Class links;
        private transient Class meta;

        /**
         * Class used to parse match data JSON.
         */
        private class DataPt {
            private transient String type;
            private transient String id;
            private AttrPt attributes;

            /**
             * Class used to parse match data attributes JSON.
             */
            private class AttrPt {
                private String createdAt;
                private int duration;
                private String gameMode;
                private String mapName;
                private transient boolean isCustomMatch;
                private transient String patchVersion;
                private transient String seasonState;
                private transient String shardId;
                private transient String[] stats;
                private transient String[] tags;
                private transient String titleId;
            }
        }

        /**
         * Class used to parse match inclusions JSON.
         */
        private class IncPt {
            private String type;
            private transient String id;
            private Attributes attributes;

            /**
             * Class used to parse match inclusion attributes JSON.
             */
            private class Attributes {
                private Map<String, String> stats;
            }
        }
    }

    private static final String NAME = "PUBG (Web API)";
    private static final String INPUT_TYPE = "PUBG User Tag";

    private static final String BASE_URL = "https://api.pubg.com/shards";
    private static final String PLATFORM = "/steam";
    private static final String API_KEY_NAME = "pubg";
    private String apiKey;

    private static final String PLAYER_FILTER = "/players?filter[playerNames]=";
    private static final String MATCH_FILTER = "/matches/";

    private JSONPlayerData playerData;
    private final Gson gson = new Gson();

    private static final String ID_PARSE_FAIL_MSG = "Error occurred while parsing account ID data";
    private static final String MATCH_PARSE_FAIL_MSG = "Error occurred while parsing match data";

    private static final int SEC_TO_MILLI_SEC = 1000;

    private int kills = 0, deaths = 0, assists = 0, wins = 0, losses = 0;
    private List<Match> matches = new ArrayList<>();

    /**
     * Method that returns the name of the data plugin.
     * @return name of the PUBG Data Plugin.
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Method that returns the name of the type of data expected from the user.
     * @return String asking for PUBG player username.
     */
    @Override
    public String getInputType() {
        return INPUT_TYPE;
    }

    /**
     * Method that extracts the Game Data of the specified user from the API,
     * and passes it on to the framework.
     * @param username String value representing the player whose data we want.
     * @return Game Data of the given user.
     * @throws IllegalArgumentException if API key is invalid, or if user data
     * doesn't exist or is invalid.
     */
    @Override
    public GameData onExtractData(String username) throws IllegalArgumentException {
        apiKey = getAPIKey();

        extractData(username);
        populate(username);

        return new GameData(username, 0, kills, deaths, assists, wins, losses, matches);
    }

    /**
     * Method that parses Player JSON to the respective classes.
     * @param username String that represents the username of the player whose
     * data we are looking for.
     * @throws IllegalArgumentException is JSON is parsed incorrectly, or if
     * user does not exist.
     */
    private void extractData(String username) throws IllegalArgumentException {
        String urlString = BASE_URL + PLATFORM + PLAYER_FILTER + username;
        String jsonString = extractFromURL(urlString, ID_PARSE_FAIL_MSG);
        try {
            playerData = gson.fromJson(jsonString, JSONPlayerData.class);
            if (playerData.data == null || playerData.data[0].id == null) {
                throw new IllegalArgumentException(ID_PARSE_FAIL_MSG);
            }
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException(ID_PARSE_FAIL_MSG);
        }
    }

    /**
     * Method that gets the ids of the recent matches played by the player,
     * then parses the data of each match to determine the player's performance.
     * @param username String that represents the username of the player whose
     * data we are looking for.
     * @throws IllegalArgumentException if JSON parsing fails.
     */
    private void populate(String username) throws IllegalArgumentException {
        MatchCodes codes = playerData.data[0].relationships.matches;
        String urlString;
        String jsonString;

        JSONMatch jsonMatch;
        int i;
        for (i = 0; i < codes.data.length; i++) {
            urlString = BASE_URL + PLATFORM + MATCH_FILTER + codes.data[i].id;
            jsonString = extractFromURL(urlString, MATCH_PARSE_FAIL_MSG);
            try {
                jsonMatch = gson.fromJson(jsonString, JSONMatch.class);
            } catch (JsonSyntaxException e) {
                throw new IllegalArgumentException(MATCH_PARSE_FAIL_MSG);
            }

            boolean didWin = false;
            List<MatchPlayerInfo> participants = new ArrayList<>();
            for (int j = 0; j < jsonMatch.included.length; j++) {
                if (jsonMatch.included[j].type.equals("participant")) {
                    Map<String, String> data = jsonMatch.included[j].attributes.stats;
                    if (data.containsKey("name") && data.get("name").equals(username)) {
                        kills += Integer.parseInt(data.get("kills"));
                        assists += Integer.parseInt(data.get("assists"));

                        if (data.get("winPlace").equals("1")) {
                            didWin = true;
                            wins++;
                        } else {
                            losses++;
                            deaths++;
                        }
                    }

                    int died = 1;
                    if (data.get("winPlace").equals("1")) {
                        died = 0;
                    }
                    MatchPlayerInfo playerInfo = new MatchPlayerInfo(data.get("name"), "PUBG Player", Integer.parseInt(data.get("kills")),
                            died, Integer.parseInt(data.get("assists")), data.get("winPlace").equals("1"));
                    participants.add(playerInfo);
                }
            }

            String mode = jsonMatch.data.attributes.mapName + " " + jsonMatch.data.attributes.gameMode;
            DateTime creation = DateTime.parseDateTime(jsonMatch.data.attributes.createdAt);
            DateTime duration = new DateTime(jsonMatch.data.attributes.duration * SEC_TO_MILLI_SEC);
            Match match = new Match(mode, participants, creation, duration, didWin);
            matches.add(match);
        }
        if (i != 0) {
            kills /= i;
            deaths /= i;
            assists /= i;
        }
    }

    /**
     * Method used to obtain JSON data from API URLs.
     * @param urlString The API's URL address to be connected to.
     * @param failMsg String representing the message to be printed if data
     * access fails.
     * @return String containing data in JSON format.
     * @throws IllegalArgumentException if connection fails or if data is invalid.
     */
    private String extractFromURL(String urlString, String failMsg) throws IllegalArgumentException {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setRequestProperty("Accept", "application/vnd.api+json");

            InputStream inputStream = conn.getInputStream();
            Scanner in = new Scanner(inputStream);
            String line;
            if (in.hasNext()) {
                line = in.nextLine();
                return line;
            } else {
                throw new IllegalArgumentException(failMsg);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(failMsg);
        }
    }

    /**
     * Reads the appropriate API key from the api_key.properties file.
     * @return String containing API key.
     * @throws IllegalArgumentException if Config file is missing, or API key
     * is missing in the file.
     */
    private String getAPIKey() throws IllegalArgumentException {
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(API_KEY_CONFIG)) {
            properties.load(is);
        } catch (IOException e) {
            throw new IllegalArgumentException("Config file was not found. Please have " + API_KEY_CONFIG);
        }

        String key = properties.getProperty(API_KEY_NAME);
        if (key == null) {
            throw new IllegalArgumentException("API Key was not found in " + API_KEY_CONFIG);
        } else {
            return key;
        }
    }
}
