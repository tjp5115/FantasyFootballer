package fantasy.footballer.espn.api.json.scoreboard;

import com.google.api.client.util.Key;

import java.util.ArrayList;

public class Team {

    @Key("teams")
    public ArrayList<PlayerId> playerIds;
}
