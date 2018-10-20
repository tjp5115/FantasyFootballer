package fantasy.footballer.player.finder;

import fantasy.footballer.borischen.FantasyFootballTiers;
import fantasy.footballer.borischen.LeagueType;
import fantasy.footballer.borischen.PlayerType;
import fantasy.footballer.espn.api.json.player.Player;
import fantasy.footballer.player.PlayerIdentifier;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EspnPlayerFinder {

    private Map<PlayerType, List<Player>> positions;
    private Map<PlayerType,Map<Integer,List<PlayerIdentifier>>> playerTeirs;
    private Optional<Integer> myTeam = Optional.empty();
    private HashMap<PlayerType, List<PlayerIdentifier>> myPlayers;
    private FantasyFootballTiers teirGenerator;

    public EspnPlayerFinder(FantasyFootballTiers teirGenerator){
        this.teirGenerator = teirGenerator;
        myPlayers = new HashMap<>();
        playerTeirs = new HashMap<>();
    }

    public void addEspnPlayers(List<Player> leaguePlayerInfo) {
        positions = leaguePlayerInfo.stream()
            .collect( Collectors.groupingBy(player -> PlayerType.fromEspn(player.name.positionId)) );
    }

    public void setMyTeam(int myTeam){
        this.myTeam = Optional.of(myTeam);
    }

    public void findAllPossible(){
        Arrays.stream(PlayerType.values()).forEach(this::findPossiblePlayersForPosition);
    }

    public void findPossiblePlayersForPosition(PlayerType playerType) {
        List<PlayerIdentifier> leaguePlayers = positions.get(playerType).stream()
            .map(PlayerIdentifier::createForEspn)
            .collect(Collectors.toList());

        Map<Integer, List<PlayerIdentifier>> possiblePlayersToPickUp = getPossiblePlayers(playerType,playerIdentifier -> !leaguePlayers.contains(playerIdentifier));

        Integer bestTeirAvailable = possiblePlayersToPickUp.keySet().stream()
            .mapToInt(i -> i)
            .min()
            .orElseThrow(NoSuchElementException::new);

        List<PlayerIdentifier> myPlayers = getMyPlayers(playerType);

        Map<Integer, List<PlayerIdentifier>> possiblePlayersToDrop = getPossiblePlayers(playerType, myPlayers::contains);

        Integer worstTierMyTeamHas = possiblePlayersToDrop.keySet().stream()
            .mapToInt(i -> i)
            .max()
            .orElse(999);

        if (worstTierMyTeamHas > bestTeirAvailable) {
            System.out.println(playerType.getName() + " : " + "Trade " + possiblePlayersToDrop.get(worstTierMyTeamHas) + "( Teir " + Integer.toString(worstTierMyTeamHas)
                + " ) for " + possiblePlayersToPickUp.get(bestTeirAvailable) + "( Teir " + Integer.toString(bestTeirAvailable) + " )");
        } else {
            System.out.println(playerType.getName() + " : Your Players: " + possiblePlayersToDrop.get(worstTierMyTeamHas) + "( Teir " + Integer.toString(worstTierMyTeamHas)
                + " ) Best Upgrade: " + possiblePlayersToPickUp.get(bestTeirAvailable) + "( Teir " + Integer.toString(bestTeirAvailable) + " )");
        }
    }

    private List<PlayerIdentifier> getMyPlayers(PlayerType playerType) {
         return myPlayers.computeIfAbsent(playerType, myPlayers -> populateMyPlayers(playerType));
    }

    private List<PlayerIdentifier> populateMyPlayers(PlayerType playerType) {
        return positions.get(playerType).stream()
            .filter(player -> myTeam.isPresent() && player.teamId == myTeam.get().intValue())
            .map(PlayerIdentifier::createForEspn)
            .collect(Collectors.toList());
    }

    private Map<Integer,List<PlayerIdentifier>> getPlayerTiers(PlayerType playerType) {
        return playerTeirs.computeIfAbsent(playerType, teir -> populatePlayerTiers(playerType, LeagueType.PPR));
    }

    private Map<Integer, List<PlayerIdentifier>> populatePlayerTiers(PlayerType playerType, LeagueType leagueType) {
        return teirGenerator.getTiers(playerType,leagueType)
            .entrySet().stream()
            .collect( Collectors.toMap (
                Map.Entry::getKey,
                entry -> entry.getValue().stream()
                    .map(player -> PlayerIdentifier.createForBorichen(player, playerType))
                    .collect(Collectors.toList())
                )
            );
    }

    private Map<Integer,List<PlayerIdentifier>> getPossiblePlayers(PlayerType playerType, Predicate<PlayerIdentifier> predicate) {
        Map<Integer,List<PlayerIdentifier>> tierList = getPlayerTiers(playerType);
        return tierList.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().stream()
                    .filter(predicate::test)
                    .collect(Collectors.toList())
            )).entrySet().stream()
            .filter(entry -> !entry.getValue().isEmpty())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void findTeamBestLineup() {
    }
}
