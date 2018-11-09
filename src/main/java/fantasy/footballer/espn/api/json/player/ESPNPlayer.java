package fantasy.footballer.espn.api.json.player;

import com.google.api.client.util.Key;

public class ESPNPlayer {
    @Key("player")
    public PlayerName name;
    @Key("teamId")
    public Integer teamId;

    public ESPNPlayer(PlayerName playerName) {
        this.name = playerName;
    }
}
