package fantasy.footballer.espn.api.json.scoreboard;

import com.google.api.client.util.Key;

import java.util.ArrayList;

public class Matchups {
    @Key("matchups")
    public ArrayList<Team> teams;
}
