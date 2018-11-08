package fantasy.footballer.espn.api.json.player;

import com.google.api.client.util.Key;

import java.util.ArrayList;

public class Team {
    @Key("players")
    public ArrayList<ESPNPlayer> ESPNPlayers;
}
