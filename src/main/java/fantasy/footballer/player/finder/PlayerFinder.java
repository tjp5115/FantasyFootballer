package fantasy.footballer.player.finder;

import fantasy.footballer.espn.api.json.player.Player;
import fantasy.footballer.espn.api.player.Position;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerFinder {

    private Map<Position, List<Player>> positions;

    public void addEspnPlayers(List<Player> leaguePlayerInfo) {
        positions = leaguePlayerInfo.stream()
            .collect( Collectors.groupingBy(player -> (Position.fromInt(player.name.positionId))) );
    }

}
