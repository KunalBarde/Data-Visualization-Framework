package edu.cmu.cs.cs214.plugins.data;

import com.google.gson.JsonSyntaxException;
import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.GameData;

import com.google.gson.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class PUBGAPIPlugin implements DataPlugin {

    private class JSONPlayerData {
        private Player[] data;
        private transient Class links;
        private transient Class meta;

        private class Player {
            private transient String type;
            private String id;
            private transient Class attributes;
            private MatchData relationships;
            private transient Class links;

            private class MatchData {
                private transient Class assets;
                private MatchCodes matches;
                private transient Class links;
            }
        }
    }

    private class MatchCodes {
        private Code[] data;

        private class Code {
            private transient String type;
            private String id;
        }
    }

    private class JSONMatch {
        private transient Class data;
        private transient Class relationships;
        private DataPt[] included;
        private transient Class links;
        private transient Class meta;

        private class DataPt {
            private String type;
            private transient String id;
            private Attributes attributes;

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
    private static final String MATCH_FILTER = "/archives/";

    private String playerId;

    private JSONPlayerData playerData;
    private final Gson gson;

    private static final String ID_PARSE_FAIL_MSG = "Error occurred while parsing account ID data";
    private static final String MATCH_PARSE_FAIL_MSG = "Error occurred while parsing match data";

    private int kills = 0, deaths = 0, assists = 0, wins = 0, losses = 0;

    public PUBGAPIPlugin() {
        gson = new Gson();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getInputType() {
        return INPUT_TYPE;
    }

    @Override
    public GameData onExtractData(String username) throws IllegalArgumentException {
        apiKey = getAPIKey();

        extractID(username);
        extractMatches(username);

        return null;
    }

    private void extractID(String username) throws IllegalArgumentException {
        String urlString = BASE_URL + PLATFORM + PLAYER_FILTER + username;
        String jsonString = extractFromURL(urlString, ID_PARSE_FAIL_MSG);
        try {
            playerData = gson.fromJson(jsonString, JSONPlayerData.class);
            if (playerData.data == null || playerData.data[0].id == null) {
                throw new IllegalArgumentException(ID_PARSE_FAIL_MSG);
            } else {
                playerId = playerData.data[0].id;
            }
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException(ID_PARSE_FAIL_MSG);
        }
    }

    private void extractMatches(String username) {
        MatchCodes codes = playerData.data[0].relationships.matches;
        String urlString;
        String jsonString;

        JSONMatch jsonMatch;
        for (int i = 0; i < codes.data.length; i++) {
            urlString = BASE_URL + PLATFORM + MATCH_FILTER + codes.data[i].id;
            jsonString = extractFromURL(urlString, MATCH_PARSE_FAIL_MSG);
            System.out.println(jsonString);
            try {
                jsonMatch = gson.fromJson(jsonString, JSONMatch.class);
            } catch (JsonSyntaxException e) {
                throw new IllegalArgumentException(MATCH_PARSE_FAIL_MSG);
            }

            for (int j = 0; j < jsonMatch.included.length; j++) {
                if (jsonMatch.included[j].type.equals("participant")) {
                    Map<String, String> data = jsonMatch.included[j].attributes.stats;
                    if (data.containsKey("name") && data.get("name").equals(username)) {
                        kills += Integer.parseInt(data.get("kills"));
                        assists += Integer.parseInt(data.get("assists"));

                        if (data.get("winPlace").equals("1")) {
                            wins++;
                        } else {
                            losses++;
                            deaths++;
                        }
                    }
                }
            }
        }
    }

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

    private String getAPIKey() {
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(API_KEY_CONFIG)) {
            properties.load(is);
        } catch(IOException e) {
            throw new IllegalArgumentException("Config file was not found. Please have " + API_KEY_CONFIG);
        }

        String key = properties.getProperty(API_KEY_NAME);
        if (key == null) throw new IllegalArgumentException("API Key was not found in " + API_KEY_CONFIG);
        else return key;
    }
}
