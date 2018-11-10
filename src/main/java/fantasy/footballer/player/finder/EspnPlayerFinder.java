package fantasy.footballer.player.finder;

import fantasy.footballer.borischen.FantasyFootballTiers;
import fantasy.footballer.borischen.Position;
import fantasy.footballer.espn.api.json.player.EspnPlayerAPI;
import fantasy.footballer.espn.player.EspnPlayer;
import fantasy.footballer.player.Player;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EspnPlayerFinder {

    private Map<Position, List<EspnPlayer>> leaguePlayers;
    private Map<Position,List<Player>> playerTiers;
    private Integer teamId;
    private HashMap<Position, List<Player>> myPlayers;
    private FantasyFootballTiers tierGenerator;

    public EspnPlayerFinder(FantasyFootballTiers tierGenerator){
        this.tierGenerator = tierGenerator;
        myPlayers = new HashMap<>();
        playerTiers = new HashMap<>();
        leaguePlayers = new HashMap<>();
    }

    public void addEspnPlayers(List<EspnPlayerAPI> leaguePlayers) {
        this.leaguePlayers = leaguePlayers.stream()
            .map(EspnPlayer::new)
            .collect( Collectors.groupingBy(Player::getPosition) );
    }

    public void setTeamId(int teamId){
        this.teamId = teamId;
        myPlayers.clear();
    }

    public List<Player> rankPlayersForPosition(Position position) {
        if( teamId == null ){
            //todo make an "empty" player position.
            return null;
        }

        List<Player> teamPlayers = getPlayerTiers(position).stream()
            .filter( player -> getMyPlayers(position).contains(player))
            .collect(Collectors.toList());

        List<Player> playersNotRanked = getMyPlayers(position).stream()
            .filter(player -> ! teamPlayers.contains(player))
            .collect(Collectors.toList());

        playersNotRanked.forEach(player -> player.setTier(999));

        teamPlayers.addAll(playersNotRanked);
        teamPlayers.sort(Comparator.comparing(Player::getTier));
        return teamPlayers;
    }

    public PlayerPosition findPossibleTradesForPosition(Position position) {
        List<EspnPlayer> leaguePlayers = this.leaguePlayers.get(position);

       List<Player> possiblePlayersToPickUp = getPossiblePlayers(position, player -> !leaguePlayers.contains(player));

        Integer bestTierAvailable = possiblePlayersToPickUp.stream()
            .min(Comparator.comparing(Player::getTier))
            .map(Player::getTier)
            .orElseThrow(NoSuchElementException::new);

        List<Player> myPlayers = getMyPlayers(position);

        List<Player> possiblePlayersToDrop = getPossiblePlayers(position, myPlayers::contains);

        Integer worstTierMyTeamHas = possiblePlayersToDrop.stream()
            .max(Comparator.comparing(Player::getTier))
            .map(Player::getTier)
            .orElse(999);

        possiblePlayersToDrop = possiblePlayersToDrop.stream()
            .filter(player -> player.getTier() > bestTierAvailable || player.getTier().equals(worstTierMyTeamHas))
            .collect(Collectors.toList());

        possiblePlayersToPickUp = possiblePlayersToPickUp.stream()
            .filter(player -> player.getTier().equals(bestTierAvailable))
            .collect(Collectors.toList());

        return new PlayerPosition(position, possiblePlayersToDrop, possiblePlayersToPickUp);
    }

    private List<Player> getMyPlayers(Position position) {
         return myPlayers.computeIfAbsent(position, myPlayers -> populateTeamsPlayers(position));
    }

    private List<Player> populateTeamsPlayers(Position position) {
        return leaguePlayers.get(position).stream()
            .filter(player -> teamId != null && player.getTeamId() == teamId)
            .collect(Collectors.toList());
    }

    /**
     * Gets the possible tiers for a given position. Populates it with the generator if the Cache does not contain it.
     * @param position
     * @return
     */
    private List<Player> getPlayerTiers(Position position) {
        return playerTiers.computeIfAbsent(position, tier -> tierGenerator.getTiers(position));
    }

    /**
     * get the Players for a given position and a given predicate.
     * @param position
     * @param predicate
     * @return
     */
    private List<Player> getPossiblePlayers(Position position, Predicate<Player> predicate) {
        List<Player> tierList = getPlayerTiers(position);
        return tierList.stream()
            .filter (predicate::test)
            .collect(Collectors.toList());
    }

    public Map<Position,List<Player>> rankPlayersForTeam() {
        if( teamId == null ) return new HashMap<>();
        Map<Position,List<Player>> results = Arrays.stream(Position.values())
            .collect(Collectors.toMap(position -> position, this::rankPlayersForPosition));
        return results;
    }

    public List<PlayerPosition> findAllPossibleTrades(){
        return forEachPosition(this::findPossibleTradesForPosition);
    }

    /**
     * Any routine that needs to iterator over all positions and return a PlayerPosition should use this.
     * @param function
     * @return
     */
    private List<PlayerPosition> forEachPosition(Function<Position,PlayerPosition> function){
        List<PlayerPosition> results = new ArrayList<>();
        Arrays.stream(Position.values())
            .forEach(position -> results.add(function.apply(position)));
        return results;
    }
}
