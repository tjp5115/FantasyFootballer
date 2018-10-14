package fantasy.footballer.espn.api.json.player;

import com.google.api.client.util.Key;

public class PlayerName {
    @Key("lastName")
    public String lastName;
    @Key("firstName")
    public String firstName;
    @Key("playerId")
    public Integer playerId;
}
