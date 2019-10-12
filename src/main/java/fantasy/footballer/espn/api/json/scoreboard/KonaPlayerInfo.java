package fantasy.footballer.espn.api.json.scoreboard;

import com.google.api.client.util.Key;
import fantasy.footballer.espn.api.json.player.EspnPlayerAPI;

import java.util.List;


public class KonaPlayerInfo {
    @Key("players")
    public List<EspnPlayerAPI> players;
    @Key("positionAgainstOpponent")
    public Metadata positionAgainstOpponent;
}
