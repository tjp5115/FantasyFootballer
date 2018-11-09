package fantasy.footballer.player.finder;

import fantasy.footballer.borischen.Position;
import fantasy.footballer.player.Player;

import java.util.List;

public class PlayerPosition {
    private final List<Player> possiblePlayersToDrop;
    private final List<Player> possiblePlayersToPickUp;
    private final Position position;
    private final boolean shouldTrade;

    public PlayerPosition(Position position, List<Player> possiblePlayersToDrop, List<Player> possiblePlayersToPickUp) {
        this.position = position;
        this.possiblePlayersToDrop = possiblePlayersToDrop;
        this.possiblePlayersToPickUp = possiblePlayersToPickUp;
        if( possiblePlayersToDrop.isEmpty() || possiblePlayersToPickUp.isEmpty() ){
            this.shouldTrade = false;
        }else{
            this.shouldTrade = possiblePlayersToDrop.get(0).getTier() > possiblePlayersToPickUp.get(0).getTier();
        }
    }

    public List<Player> getPossiblePlayersToDrop() {
        return possiblePlayersToDrop;
    }

    public List<Player> getPossiblePlayersToPickUp() {
        return possiblePlayersToPickUp;
    }

    public boolean shouldTrade() {
        return shouldTrade;
    }

    @Override
    public String toString() {
        String prefix ;

        if( possiblePlayersToDrop.isEmpty() && possiblePlayersToPickUp.isEmpty() ){
            return "NO PLAYERS";
        }

        if (shouldTrade) {
            prefix = position.getName() + " : " + "Trade ";
        } else {
            prefix = position.getName() + " : Your Players: ";
        }
        return prefix + getStringForPlayers(possiblePlayersToDrop) + " for " + getStringForPlayers(possiblePlayersToPickUp);
    }

    private String getStringForPlayers(List<Player> possiblePlayers) {
        if ( possiblePlayers.isEmpty() ){
            return "NO PLAYERS";
        }
        return possiblePlayers + "( Teir " + possiblePlayers.get(0).getTier() + " )";
    }

    public int getBestTierAvailableToPickUp() {
        return possiblePlayersToPickUp.get(0).getTier();
    }

    public int getWorstTierOnTeam() {
        return possiblePlayersToDrop.get(0).getTier();
    }
}
