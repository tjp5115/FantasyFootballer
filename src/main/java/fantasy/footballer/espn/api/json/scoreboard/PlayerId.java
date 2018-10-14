package fantasy.footballer.espn.api.json.scoreboard;

import com.google.api.client.util.Key;

import java.util.ArrayList;

public class PlayerId {
    @Key("playerIDs")
    public ArrayList<Integer> ids;
}
