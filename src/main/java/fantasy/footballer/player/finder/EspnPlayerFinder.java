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

    private Map<Position, List<ESPNPlayer>> positions;
    private Map<Position,List<Player>> playerTiers;
    private Optional<Integer> myTeam = Optional.empty();
    private HashMap<Position, List<PlayerIdentifier>> myPlayers;
    private FantasyFootballTiers teirGenerator;

    public EspnPlayerFinder(FantasyFootballTiers teirGenerator){
        this.teirGenerator = teirGenerator;
        myPlayers = new HashMap<>();
        playerTiers = new HashMap<>();
    }

    public void addEspnPlayers(List<ESPNPlayer> leagueESPNPlayerInfo) {
        positions = leagueESPNPlayerInfo.stream()
            .collect( Collectors.groupingBy(ESPNPlayer -> Position.fromEspn(ESPNPlayer.name.positionId)) );
    }

    public void setMyTeam(int myTeam){
        this.myTeam = Optional.of(myTeam);
    }

    public void findAllPossible(){
        Arrays.stream(Position.values()).forEach(this::findPossiblePlayersForPosition);
    }

    public void findPossiblePlayersForPosition(Position position) {
        List<PlayerIdentifier> leaguePlayers = positions.get(position).stream()
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
            .filter(player -> player.getTier().equals(worstTierMyTeamHas ))
            .collect(Collectors.toList());
        possiblePlayersToPickUp = possiblePlayersToPickUp.stream()
            .filter(player -> player.getTier().equals(bestTierAvailable))
            .collect(Collectors.toList());
        if (worstTierMyTeamHas > bestTierAvailable) {
            System.out.println(position.getName() + " : " + "Trade " + possiblePlayersToDrop + "( Teir " + Integer.toString(worstTierMyTeamHas)
                + " ) for " + possiblePlayersToPickUp + "( Teir " + Integer.toString(bestTierAvailable) + " )");
        } else {
            System.out.println(position.getName() + " : Your Players: " + possiblePlayersToDrop + "( Teir " + Integer.toString(worstTierMyTeamHas)
                + " ) Best Upgrade: " + possiblePlayersToPickUp + "( Teir " + Integer.toString(bestTierAvailable) + " )");
        }
    }

    private List<PlayerIdentifier> getMyPlayers(Position position) {
         return myPlayers.computeIfAbsent(position, myPlayers -> populateMyPlayers(position));
    }

    private List<PlayerIdentifier> populateMyPlayers(Position position) {
        return positions.get(position).stream()
            .filter(ESPNPlayer -> myTeam.isPresent() && ESPNPlayer.teamId == myTeam.get().intValue())
            .map(PlayerIdentifier::createForEspn)
            .collect(Collectors.toList());
    }

    private List<Player> getPlayerTiers(Position position) {
        return playerTiers.computeIfAbsent(position, teir -> populatePlayerTiers(position));
    }

    private List<Player> populatePlayerTiers(Position position) {
        return teirGenerator.getTiers(position);
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
