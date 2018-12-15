package fantasy.footballer.player.finder;

import fantasy.footballer.borischen.FantasyFootballTiers;
import fantasy.footballer.player.Position;
import fantasy.footballer.fanduel.player.FanDuelPlayer;
import fantasy.footballer.player.Player;

import java.util.*;
import java.util.stream.Collectors;

public class FanduelPlayerFinder {


    private Map<Position,List<FanDuelPlayer>> fanduelPlayers;
    private FantasyFootballTiers fantasyFootballTiers;

    public FanduelPlayerFinder(List<FanDuelPlayer> fanduelPlayerList, FantasyFootballTiers fantasyFootballTiers){
       this.fantasyFootballTiers = fantasyFootballTiers;

       fanduelPlayerList.forEach(player -> player.setTier( getTierForPlayer(player) ) );

       this.fanduelPlayers = fanduelPlayerList.stream()
           .collect(Collectors.groupingBy(Player::getPosition));
    }

    private Integer getTierForPlayer(Player fanDuelPlayer ) {
        Optional<Player> tierPlayer = fantasyFootballTiers.getTiers(fanDuelPlayer.getPosition()).stream()
            .filter( player -> fanDuelPlayer.getPlayerIdentifier().equals(player.getPlayerIdentifier()))
            .findFirst();
        if ( tierPlayer.isPresent() ){
            return tierPlayer.get().getTier();
        }else{
            return Integer.MAX_VALUE;
        }
    }

    public Map<Position, List<FanDuelPlayer> > findCheapestPlayersForTier(int tier){
        return findCheapestPlayersForTier(tier,3);
    }

    public Map<Position, List<FanDuelPlayer> > findCheapestPlayersForTier(int tier, int numberOfPlayersPerTier){
       Map<Position,List<FanDuelPlayer>> players = new HashMap<>();
        for ( Position position : Position.values()){
            players.put(position,findCheapestPlayerForTier(position,tier, numberOfPlayersPerTier));
        }
        return players;
    }

    private List<FanDuelPlayer> findCheapestPlayerForTier(Position position, Integer tier, int numberOfPlayersPerTier){
        return getPlayersForPosition(position).stream()
            .filter(fanduelPlayer -> fanduelPlayer.getTier().equals(tier))
            .sorted((Comparator.comparing(FanDuelPlayer::getSalary)))
            .limit(numberOfPlayersPerTier)
            .collect(Collectors.toList());
    }

    private List<FanDuelPlayer> getPlayersForPosition(Position position) {
        return fanduelPlayers.getOrDefault(position, new ArrayList<>());
    }

    public List<FanDuelPlayer> findFlexPlayer(int targetSalary){
        return findFlexPlayer(targetSalary, targetSalary - 500);
    }

    public List<FanDuelPlayer> findFlexPlayer(int targetSalary, int minSalary) {
        List<FanDuelPlayer> flexPlayers = new ArrayList<>();
        flexPlayers.addAll(getPlayersForPosition(Position.WIDE_RECEIVER));
        flexPlayers.addAll(getPlayersForPosition(Position.TIGHT_END));
        flexPlayers.addAll(getPlayersForPosition(Position.RUNNING_BACK));

        return flexPlayers.stream()
            .filter(fanDuelPlayer -> fanDuelPlayer.getSalary() <= targetSalary && fanDuelPlayer.getSalary() >= minSalary)
            .sorted(Comparator.comparing(FanDuelPlayer::getTier))
            .collect(Collectors.toList());
    }
}
