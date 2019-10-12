package fantasy.footballer.espn.api.json.player;

import com.google.api.client.util.Key;

public class PlayerInfo {
    @Key("lastName")
    public String lastName;
    @Key("firstName")
    public String firstName;
    @Key("id")
    public Integer playerId;
    @Key("defaultPositionId")
    public Integer positionId;

    public PlayerInfo(){}

    public PlayerInfo(String first, String last) {
        firstName = first;
        lastName = last;
    }
}
