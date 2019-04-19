package edu.cmu.cs.cs214.plugins.data;

import com.google.gdata.data.DateTime;
import com.google.gson.*;
import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.GameData;
import edu.cmu.cs.cs214.hw5.core.Match;
import edu.cmu.cs.cs214.hw5.core.MatchPlayerInfo;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Chess API Plugin is a data plugin that extracts user specific data with ChessAPI
 * and provides access to the framework for further use
 * Workflow:
 * - extract player
 * - extract list of player's recent archives(20)
 * - extract each recent match's detailed data
 * - create MatchPlayerInfo, Match, and GameData class instance
 * - return GameData instance
 */
public class CHESScomDplugin implements DataPlugin {
    // CHECKSTYLE:OFF
    // CHECKSTYLE:ON
    private static final String NAME = "Chess.com API";

    private static final String INPUT_TYPE = "Chess.com Username";
    // Chess API

    private static final String BASE_URL = "https://api.chess.com/pub/player/";

    // Chess API Endpoints
    private static final String GAMES = "/games";
    private static final String ARCHIVES = "/archives";

    // Messages
    private static final String NO_PLAYER_MSG = "Player doesn't exist";
    private static final String ID_PARSE_FAIL_MSG = "Error occurred while parsing account ID data";
    private static final String RECENT_PARSE_FAIL_MSG = "Error occurred while parsing recent match data";
    // JSON Parsing

    private final Gson gson;
    private JSONPlayer player;
    private JASONGames recent;
    // Mapping id to names for heroes and game modes

    private String playerId;
    private static final int SECTOMS = 1000;

    private static final int HRTOMIN = 60;
    private static final int TZSHIFT = 9;
    /**
     * Chess API Plugin's constructor that initializes GSON in a way that
     * it will skip any unspecified fields when deserializing json.
     * The constructor also initializes HashMap that maps
     * id's to names of heroes and game modes in Chess.
     */
    public CHESScomDplugin() {
        gson = new GsonBuilder().addDeserializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getName().toLowerCase().contains("fieldName");
            }
            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        }).create();

    }

    @Override
    public String getName() { return this.NAME; }

    @Override
    public String getInputType() { return this.INPUT_TYPE; }

    @Override
    public GameData onExtractData(String username) throws IllegalArgumentException {
        extractId(username);
        extractRecentMatches(playerId);

        double kills = 0.0, deaths = 0.0, assists = 0.0;
        int wins = 0, losses = 0;

        int rating = 0;

        List<Match> matches = new ArrayList<>();
        for (JASONGames.JSONGame recentMatch : recent.games) {
            // Get all necessary match data for current recentMatch
            //JSONMatch jsonMatch = extractMatch(recentMatch.match_id);

            // Get all participants' data
            List<MatchPlayerInfo> participants = new ArrayList<>();
            //white player
            JASONGames.JSONGame.JSONPlayer whiteplayer = recentMatch.white;
            JASONGames.JSONGame.JSONPlayer blackplayer = recentMatch.black;

            boolean isWhite = whiteplayer.username == playerId;

            if(isWhite) {
                if(whiteplayer.rating > rating) {
                    rating = whiteplayer.rating;
                }
            }
            else {
                if(blackplayer.rating > rating) {
                    rating = whiteplayer.rating;
                }
            }

            participants.add(new MatchPlayerInfo(whiteplayer.username, "white", whiteplayer.rating, 0, 0, isWhite));
            participants.add(new MatchPlayerInfo(blackplayer.username, "black", blackplayer.rating, 0, 0, !isWhite));

            boolean wini = false;
            if((whiteplayer.result.equals("win") && whiteplayer.username.equals(playerId))
                || (blackplayer.result.equals("win") && blackplayer.username.equals(playerId)))
            {
                wini = true;
                System.out.println("hooray");
            }

            // Create Match class instance
            String mode = recentMatch.time_class;
            DateTime creation = new DateTime(recentMatch.move_by*SECTOMS, TZSHIFT*HRTOMIN);
            DateTime duration = new DateTime(recentMatch.end_time*SECTOMS, TZSHIFT*HRTOMIN);
            Match match = new Match(mode, participants, creation, duration, wini);
            matches.add(match);

            // Accumulate kills, deaths, assists, wins, losses to later calculate player average
            if(wini) {
                kills += 1;
                wins += 1;
            }
            else {
                deaths += 1;
                losses += 1;
            }
        }

        // Get player's average KDA
        int numMatches = recent.games.length;
        kills /= numMatches;
        deaths /= numMatches;
        assists /= numMatches;

        int level = rating;

        // Create GameData class instance
        System.out.println(wins + " loss; " + losses);
        GameData gameData = new GameData(playerId, level,
                kills, deaths, assists, wins, losses, matches);

        return gameData;
    }

    private void extractId(String username) throws IllegalArgumentException {
        String urlString = BASE_URL + username;
        String jsonString = "{\"username\":" + extractFromUrl(urlString, ID_PARSE_FAIL_MSG) + "}";
        try {
            System.out.println(jsonString);
            JsonObject accounts = gson.fromJson(jsonString, JsonObject.class);
            System.out.println(accounts.get("username").getAsJsonObject().get("username"));
            if (accounts.get("username").getAsJsonObject().get("username").getAsString() == null) {
                System.out.println("Null Username");
                throw new IllegalArgumentException(NO_PLAYER_MSG);
            } else {
                playerId = accounts.get("username").getAsJsonObject().get("username").getAsString();
                System.out.println("Extract_ID: SUCCESS");
            }
        }
        catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("JSON PARSE ERROR " + NO_PLAYER_MSG);
        }
    }

    private void extractRecentMatches(String playerId) throws IllegalArgumentException {
        String urlString = BASE_URL + playerId + GAMES + ARCHIVES;// + RECENT_MATCHES_URL + KEY_URL + apiKey;
        String jsonString = "{\"archives\":" + extractFromUrl(urlString, RECENT_PARSE_FAIL_MSG) + "}";
        try {
            JsonObject archives = gson.fromJson(jsonString, JsonObject.class);
            if(archives.get("archives").getAsJsonObject().size() == 0) {
                throw new IllegalArgumentException("No Player games");
            }
            System.out.println(archives.get("archives").getAsJsonObject().get("archives").getAsJsonArray().get(0).getAsString());
            String urlthing = archives.get("archives").getAsJsonObject().get("archives").getAsJsonArray().get(0).getAsString();
            System.out.println(urlthing);
            recent = gson.fromJson(extractFromUrl(urlthing, "Fail Archive Game Extract"),JASONGames.class);
        }
        catch (JsonSyntaxException e) {
            throw new IllegalArgumentException(RECENT_PARSE_FAIL_MSG);
        }
    }

    private String extractFromUrl(String urlString, String failMsg) throws IllegalArgumentException {
        try {
            Scanner in = new Scanner(new URL(urlString).openStream(), "UTF-8");
            String line;
            if (in.hasNext()) {
                line = in.nextLine();
                System.out.println(line);
                return line;
            } else {
                throw new IllegalArgumentException("File reading failed" + failMsg);
            }
        } catch(IOException e) {
            throw new IllegalArgumentException("File reading from URL failed" + failMsg);
        }
    }

    class JSONPlayer {
        String username;
    }

    class JSONArchive {
        JSONRecentMatch[] archives;

        class JSONRecentMatch {
            String url;
        }
    }

    class JASONGames {
        JSONGame[] games;

        class JSONGame {
            JSONPlayer white;
            JSONPlayer black;
            int move_by;
            int end_time;
            String time_class;

            class JSONPlayer {
                int rating;
                String result;
                String username;
            }
        }
    }

    class JSONGameModes {
        JSONGameMode[] modes;

        class JSONGameMode {
            int id;
            String name = "";
        }
    }
}
