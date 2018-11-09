package fantasy.footballer.player.finder;

import fantasy.footballer.borischen.FantasyFootballTiers;
import fantasy.footballer.borischen.Position;
import fantasy.footballer.espn.api.json.player.ESPNPlayer;
import fantasy.footballer.player.Player;
import fantasy.footballer.player.PlayerIdentifier;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EspnPlayerFinder {

    private Map<Position, List<ESPNPlayer>> leaguePlayers;
    private Map<Position,List<Player>> playerTiers;
    private Integer myTeam;
    private HashMap<Position, List<PlayerIdentifier>> myPlayers;
    private FantasyFootballTiers tierGenerator;

    public EspnPlayerFinder(FantasyFootballTiers tierGenerator){
        this.tierGenerator = tierGenerator;
        myPlayers = new HashMap<>();
        playerTiers = new HashMap<>();
    }

    public void addEspnPlayers(List<ESPNPlayer> leaguePlayers) {
        this.leaguePlayers = leaguePlayers.stream()
            .collect( Collectors.groupingBy(ESPNPlayer -> Position.fromEspn(ESPNPlayer.name.positionId)) );
    }

    public void setMyTeam(int myTeam){
        this.myTeam = myTeam;
    }

    public List<PlayerPosition> findAllPossible(){
        List<PlayerPosition> results = new ArrayList<>();
        Arrays.stream(Position.values())
            .forEach(position -> results.add(this.findPossiblePlayersForPosition(position)));
        return results;
    }

    public PlayerPosition findPossiblePlayersForPosition(Position position) {
        List<PlayerIdentifier> leaguePlayers = this.leaguePlayers.get(position).stream()
            .map(PlayerIdentifier::createForEspn)
            .collect(Collectors.toList());

       List<Player> possiblePlayersToPickUp = getPossiblePlayers(position, playerIdentifier -> !leaguePlayers.contains(playerIdentifier));

        Integer bestTierAvailable = possiblePlayersToPickUp.stream()
            .min(Comparator.comparing(Player::getTier))
            .map(Player::getTier)
            .orElseThrow(NoSuchElementException::new);

        List<PlayerIdentifier> myPlayers = getMyPlayers(position);

        List<Player> possiblePlayersToDrop = getPossiblePlayers(position, myPlayers::contains);

        Integer worstTierMyTeamHas = possiblePlayersToDrop.stream()
            .max(Comparator.comparing(Player::getTier))
            .map(Player::getTier)
            .orElse(999);

        possiblePlayersToDrop = possiblePlayersToDrop.stream()
            .filter(player -> player.getTier() >= worstTierMyTeamHas)
            .collect(Collectors.toList());

        possiblePlayersToPickUp = possiblePlayersToPickUp.stream()
            .filter(player -> player.getTier().equals(bestTierAvailable))
            .collect(Collectors.toList());

        return new PlayerPosition(position, possiblePlayersToDrop, possiblePlayersToPickUp);
    }

    private List<PlayerIdentifier> getMyPlayers(Position position) {
         return myPlayers.computeIfAbsent(position, myPlayers -> populateMyPlayers(position));
    }

    private List<PlayerIdentifier> populateMyPlayers(Position position) {
        return leaguePlayers.get(position).stream()
            .filter(ESPNPlayer -> myTeam != null && ESPNPlayer.teamId.intValue() == myTeam.intValue())
            .map(PlayerIdentifier::createForEspn)
            .collect(Collectors.toList());
    }

    private List<Player> getPlayerTiers(Position position) {
        return playerTiers.computeIfAbsent(position, teir -> populatePlayerTiers(position));
    }

    private List<Player> populatePlayerTiers(Position position) {
        return tierGenerator.getTiers(position);
    }

    private List<Player> getPossiblePlayers(Position position, Predicate<PlayerIdentifier> predicate) {
        List<Player> tierList = getPlayerTiers(position);
        return tierList.stream()
            .filter ( borischenPlayer -> predicate.test(borischenPlayer.getPlayerIdentifier()))
            .collect(Collectors.toList());
    }

    public void findTeamBestLineup() {
    }
}
