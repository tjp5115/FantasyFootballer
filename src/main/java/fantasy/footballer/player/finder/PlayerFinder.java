package fantasy.footballer.player.finder;

import fantasy.footballer.borischen.FantasyFootballTiers;
import fantasy.footballer.borischen.LeagueType;
import fantasy.footballer.borischen.PlayerType;
import fantasy.footballer.espn.api.json.player.Player;
import fantasy.footballer.espn.api.json.player.PlayerName;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerFinder {

    private Map<PlayerType, List<Player>> positions;

    private int myTeam = -1;

    public void addEspnPlayers(List<Player> leaguePlayerInfo) {
        positions = leaguePlayerInfo.stream()
            .collect( Collectors.groupingBy(player -> (PlayerType.fromEspn(player.name.positionId))) );
    }

    public void setMyTeam(int myTeam){
        this.myTeam = myTeam;
    }

    public void findPossibleWideReceivers(){
        Map<Integer,List<String>> teirList = new FantasyFootballTiers(PlayerType.WIDE_RECEIVER, LeagueType.PPR)
            .getTiers();
    }

}
