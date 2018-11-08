package fantasy.footballer.player.finder;

import fantasy.footballer.borischen.FantasyFootballTiers;
import fantasy.footballer.borischen.PlayerType;
import fantasy.footballer.fanduel.player.FanDuelPlayer;
import fantasy.footballer.player.Player;

import java.util.*;
import java.util.stream.Collectors;

public class FanduelPlayerFinder {


    private Map<PlayerType,List<FanDuelPlayer>> fanduelPlayers;
    private FantasyFootballTiers fantasyFootballTiers;

    public FanduelPlayerFinder(List<FanDuelPlayer> fanduelPlayerList, FantasyFootballTiers fantasyFootballTiers){
       this.fantasyFootballTiers = fantasyFootballTiers;

       fanduelPlayerList.forEach(player -> player.setTier( getTierForPlayer(player) ) );

       this.fanduelPlayers = fanduelPlayerList.stream()
           .collect(Collectors.groupingBy(Player::getPosition));
    }

    private Integer getTierForPlayer( Player fanDuelPlayer ) {
        Optional<Player> tierPlayer = fantasyFootballTiers.getTiers(fanDuelPlayer.getPosition()).stream()
            .filter( player -> fanDuelPlayer.getPlayerIdentifier().equals(player.getPlayerIdentifier()))
            .findFirst();
        if ( tierPlayer.isPresent() ){
            return tierPlayer.get().getTier();
        }else{
            return 999;
        }
    }

    public List<Player> findCheapestPlayersForTier(int tier){
        return Arrays.stream(PlayerType.values())
            .map(playerType -> findCheapestPlayerForTier(playerType,tier))
            .collect(Collectors.toList());
    }
    public Player findCheapestPlayerForTier(PlayerType playerType, Integer tier){
        List<FanDuelPlayer> playersForPosition = getPlayersForPosition(playerType);
        List<FanDuelPlayer> playersForTeir = playersForPosition.stream()
            .filter(fanduelPlayer -> fanduelPlayer.getTier().equals(tier))
            .collect(Collectors.toList());
        return playersForTeir.stream().min(Comparator.comparing(FanDuelPlayer::getSalary)).orElse(null);
    }

    private List<FanDuelPlayer> getPlayersForPosition(PlayerType playerType) {
        return fanduelPlayers.getOrDefault(playerType, Collections.EMPTY_LIST);
    }

}
