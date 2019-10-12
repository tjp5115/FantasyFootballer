package fantasy.footballer.espn.api.json.player;

import com.google.api.client.util.Key;

public class EspnPlayerAPI {
    @Key("player")
    public PlayerInfo info;
    @Key("onTeamId")
    public Integer teamId;
    @Key("status")
    public String status;

    public EspnPlayerAPI(){}

    public EspnPlayerAPI(PlayerInfo info) {
        this.info = info;
    }
}
