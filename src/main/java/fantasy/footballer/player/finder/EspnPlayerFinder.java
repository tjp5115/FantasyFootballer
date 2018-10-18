package fantasy.footballer.player.finder;

import fantasy.footballer.borischen.FantasyFootballTiers;
import fantasy.footballer.borischen.LeagueType;
import fantasy.footballer.borischen.PlayerType;
import fantasy.footballer.espn.api.json.player.Player;
import fantasy.footballer.player.PlayerIdentifier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class EspnPlayerFinder {

    private Map<PlayerType, List<Player>> positions;

    private int myTeam = -1;

    public void addEspnPlayers(List<Player> leaguePlayerInfo) {
        positions = leaguePlayerInfo.stream()
            .collect( Collectors.groupingBy(player -> PlayerType.fromEspn(player.name.positionId)) );

    }

    public void setMyTeam(int myTeam){
        this.myTeam = myTeam;
    }

    public void findAllPossible(){
        Arrays.stream(PlayerType.values()).forEach(this::findPossiblePlayersForPosition);
    }

    public void findPossiblePlayersForPosition(PlayerType playerType){
        Map<Integer,List<PlayerIdentifier>> tierList = new FantasyFootballTiers(playerType, LeagueType.PPR)
            .getTiers()
            .entrySet().stream()
            .collect( Collectors.toMap (
                Map.Entry::getKey,
                entry -> entry.getValue().stream()
                    .map(player -> PlayerIdentifier.createForBorichen(player, playerType))
                    .collect(Collectors.toList())
                )
            );

        List<PlayerIdentifier> leaguePlayers = positions.get(playerType).stream()
            .map(PlayerIdentifier::createForEspn)
            .collect(Collectors.toList());

        Map<Integer,List<PlayerIdentifier>> possiblePlayersToPickUp = tierList.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().stream()
                    .filter(playerIdentifier -> ! leaguePlayers.contains(playerIdentifier))
                    .collect(Collectors.toList())
            )).entrySet().stream()
            .filter(entry -> !entry.getValue().isEmpty())
            .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));

        Integer bestTeirAvailable = possiblePlayersToPickUp.keySet().stream()
            .mapToInt(i -> i)
            .min()
            .orElseThrow(NoSuchElementException::new);



        List<PlayerIdentifier> myPlayers = positions.get(playerType).stream()
            .filter(player -> player.teamId == myTeam)
            .map(PlayerIdentifier::createForEspn)
            .collect(Collectors.toList());

        Map<Integer,List<PlayerIdentifier>> possiblePlayersToDrop = tierList.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().stream()
                    .filter(myPlayers::contains)
                    .collect(Collectors.toList())
            )).entrySet().stream()
            .filter(entry -> !entry.getValue().isEmpty() )
            .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));

        Integer worstTierMyTeamHas = possiblePlayersToDrop.keySet().stream()
            .mapToInt(i -> i)
            .max()
            .orElse(999);

        if( worstTierMyTeamHas > bestTeirAvailable ){
            System.out.println(playerType.getName() + " : " + "Trade "+ possiblePlayersToDrop.get(worstTierMyTeamHas) + "( Teir " + Integer.toString(worstTierMyTeamHas)
                + " ) for " + possiblePlayersToPickUp.get(bestTeirAvailable) + "( Teir " + Integer.toString(bestTeirAvailable) +" )");
        }else{
            System.out.println(playerType.getName() + " : Your Players: " + possiblePlayersToDrop.get(worstTierMyTeamHas) + "( Teir " + Integer.toString(worstTierMyTeamHas)
                + " ) Best Upgrade: " + possiblePlayersToPickUp.get(bestTeirAvailable) + "( Teir " + Integer.toString(bestTeirAvailable) +" )");
        }
    }

}
