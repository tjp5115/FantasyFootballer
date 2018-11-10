package fantasy.footballer.espn.api.json.player;

import com.google.api.client.util.Key;

public class EspnPlayerAPI {
    @Key("player")
    public PlayerName name;
    @Key("teamId")
    public Integer teamId;

    public EspnPlayerAPI(){}

    public EspnPlayerAPI(PlayerName playerName) {
        this.name = playerName;
    }
}
