package fantasy.footballer.espn.api.json.scoreboard;

import com.google.api.client.util.Key;


public class ScoreBoard {
    @Key("scoreboard")
    public Matchups matchups;
    @Key("metadata")
    public Metadata metadata;

}
