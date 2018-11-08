package fantasy.footballer.player.finder;

import fantasy.footballer.borischen.BorischenPlayer;
import fantasy.footballer.borischen.FantasyFootballTiers;
import fantasy.footballer.borischen.PlayerType;
import fantasy.footballer.espn.api.json.player.ESPNPlayer;
import fantasy.footballer.player.Player;
import fantasy.footballer.player.PlayerIdentifier;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EspnPlayerFinder {

    private Map<PlayerType, List<ESPNPlayer>> positions;
    private Map<PlayerType,List<Player>> playerTiers;
    private Optional<Integer> myTeam = Optional.empty();
    private HashMap<PlayerType, List<PlayerIdentifier>> myPlayers;
    private FantasyFootballTiers teirGenerator;

    public EspnPlayerFinder(FantasyFootballTiers teirGenerator){
        this.teirGenerator = teirGenerator;
        myPlayers = new HashMap<>();
        playerTiers = new HashMap<>();
    }

    public void addEspnPlayers(List<ESPNPlayer> leagueESPNPlayerInfo) {
        positions = leagueESPNPlayerInfo.stream()
            .collect( Collectors.groupingBy(ESPNPlayer -> PlayerType.fromEspn(ESPNPlayer.name.positionId)) );
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

       List<Player> possiblePlayersToPickUp = getPossiblePlayers(playerType,playerIdentifier -> !leaguePlayers.contains(playerIdentifier));

        Integer bestTierAvailable = possiblePlayersToPickUp.stream()
            .min(Comparator.comparing(Player::getTier))
            .map(Player::getTier)
            .orElseThrow(NoSuchElementException::new);

        List<PlayerIdentifier> myPlayers = getMyPlayers(playerType);

        List<Player> possiblePlayersToDrop = getPossiblePlayers(playerType, myPlayers::contains);

        Integer worstTierMyTeamHas = possiblePlayersToDrop.stream()
            .max(Comparator.comparing(Player::getTier))
            .map(Player::getTier)
            .orElse(999);

        possiblePlayersToDrop = possiblePlayersToDrop.stream()
            .filter(player -> player.getTier().equals(worstTierMyTeamHas ))
            .collect(Collectors.toList());
        possiblePlayersToPickUp = possiblePlayersToPickUp.stream()
            .filter(player -> player.getTier().equals(bestTierAvailable))
            .collect(Collectors.toList());
        if (worstTierMyTeamHas > bestTierAvailable) {
            System.out.println(playerType.getName() + " : " + "Trade " + possiblePlayersToDrop + "( Teir " + Integer.toString(worstTierMyTeamHas)
                + " ) for " + possiblePlayersToPickUp + "( Teir " + Integer.toString(bestTierAvailable) + " )");
        } else {
            System.out.println(playerType.getName() + " : Your Players: " + possiblePlayersToDrop + "( Teir " + Integer.toString(worstTierMyTeamHas)
                + " ) Best Upgrade: " + possiblePlayersToPickUp + "( Teir " + Integer.toString(bestTierAvailable) + " )");
        }
    }

    private List<PlayerIdentifier> getMyPlayers(PlayerType playerType) {
         return myPlayers.computeIfAbsent(playerType, myPlayers -> populateMyPlayers(playerType));
    }

    private List<PlayerIdentifier> populateMyPlayers(PlayerType playerType) {
        return positions.get(playerType).stream()
            .filter(ESPNPlayer -> myTeam.isPresent() && ESPNPlayer.teamId == myTeam.get().intValue())
            .map(PlayerIdentifier::createForEspn)
            .collect(Collectors.toList());
    }

    private List<Player> getPlayerTiers(PlayerType playerType) {
        return playerTiers.computeIfAbsent(playerType, teir -> populatePlayerTiers(playerType));
    }

    private List<Player> populatePlayerTiers(PlayerType playerType) {
        return teirGenerator.getTiers(playerType);
    }

    private List<Player> getPossiblePlayers(PlayerType playerType, Predicate<PlayerIdentifier> predicate) {
        List<Player> tierList = getPlayerTiers(playerType);
        return tierList.stream()
            .filter ( borischenPlayer -> predicate.test(borischenPlayer.getPlayerIdentifier()))
            .collect(Collectors.toList());
    }

    public void findTeamBestLineup() {
    }
}
