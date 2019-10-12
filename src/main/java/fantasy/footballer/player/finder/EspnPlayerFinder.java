package fantasy.footballer.player.finder;

import fantasy.footballer.borischen.FantasyFootballTiers;
import fantasy.footballer.player.Position;
import fantasy.footballer.espn.api.json.player.EspnPlayerAPI;
import fantasy.footballer.espn.player.EspnPlayer;
import fantasy.footballer.player.Player;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EspnPlayerFinder {

    Logger logger = Logger.getLogger(EspnPlayerFinder.class);

    private Map<Position, Set<EspnPlayer>> leaguePlayers;
    private Map<Position, Set<Player>> playerTiers;
    private Integer teamId;
    private HashMap<Position, Set<Player>> myPlayers;
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
            .collect(Collectors.groupingBy(Player::getPosition, Collectors.toSet()));
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

        playersNotRanked.forEach(player -> player.setTier(Integer.MAX_VALUE));

        teamPlayers.addAll(playersNotRanked);
        teamPlayers.sort(Comparator.comparing(Player::getTier));
        return teamPlayers;
    }

    public PlayerTrade findPossibleTradesForPosition(Position position) {
        Set<Player> freeAgents = leaguePlayers.get(position).stream()
                .filter(player -> "FREEAGENT".equals(player.getStatus()))
                .collect(Collectors.toSet());

        List<Player> availableLeaguePlayers = getPossiblePlayers(position, freeAgents::contains);

        Integer bestTierAvailable = Integer.MAX_VALUE;
        List<Player> bestAvailablePlayers = new ArrayList<>();
        for (Player player : availableLeaguePlayers) {
            int compareTo = player.getTier().compareTo(bestTierAvailable);

            if (compareTo < 0) {
                bestAvailablePlayers.clear();
                bestTierAvailable = player.getTier();
            }

            if (compareTo < 1) {
                bestAvailablePlayers.add(player);
            }

            if (player.getTier().equals(Integer.MAX_VALUE)) {
                logger.error(String.format("Did not find player [%s]",player.getPlayerIdentifier()));
            }

        }

        List<Player> myPlayers = getPossiblePlayers(position, getMyPlayers(position)::contains);
        Integer finalBestTierAvailable = bestTierAvailable;
        List<Player> possiblePlayersToDrop = myPlayers.stream()
            .filter(player -> player.getTier().compareTo(finalBestTierAvailable) > -1)
            .collect(Collectors.toList());

        return new PlayerTrade(position, possiblePlayersToDrop, bestAvailablePlayers);
    }

    private Set<Player> getMyPlayers(Position position) {
         return myPlayers.computeIfAbsent(position, myPlayers -> populateTeamsPlayers(position));
    }

    private Set<Player> populateTeamsPlayers(Position position) {
        return leaguePlayers.get(position).stream()
            .filter(player -> teamId != null && player.getTeamId() == teamId)
            .collect(Collectors.toSet());
    }

    /**
     * Gets the possible tiers for a given position. Populates it with the generator if the Cache does not contain it.
     * @param position
     * @return
     */
    private Set<Player> getPlayerTiers(Position position) {
        // todo make the hashset kind of nice.
        return playerTiers.computeIfAbsent(position, tier -> new HashSet(tierGenerator.getTiers(position)));
    }

    /**
     * get the Players for a given position and a given predicate.
     * @param position
     * @param predicate
     * @return
     */
    private List<Player> getPossiblePlayers(Position position, Predicate<Player> predicate) {
        List<Player> possiblePlayers = new ArrayList<>();
        for(Player player :  getPlayerTiers(position)){
            if(predicate.test(player)){
                possiblePlayers.add(player);
            }
        }

        return possiblePlayers;
    }

    public Map<Position,List<Player>> rankPlayersForTeam() {
        if( teamId == null ) return new HashMap<>();
        Map<Position,List<Player>> results = Arrays.stream(Position.values())
            .collect(Collectors.toMap(position -> position, this::rankPlayersForPosition));
        return results;
    }

    public List<PlayerTrade> findAllPossibleTrades(){
        return forEachPosition(this::findPossibleTradesForPosition);
    }

    /**
     * Any routine that needs to iterator over all positions and return a PlayerTrade should use this.
     * @param function
     * @return
     */
    private List<PlayerTrade> forEachPosition(Function<Position,PlayerTrade> function){
        List<PlayerTrade> results = new ArrayList<>();
        Arrays.stream(Position.values())
            .forEach(position -> results.add(function.apply(position)));
        return results;
    }
}
