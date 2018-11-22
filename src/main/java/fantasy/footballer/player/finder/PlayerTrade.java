package fantasy.footballer.player.finder;

import com.google.api.client.util.Key;
import fantasy.footballer.player.Position;
import fantasy.footballer.player.Player;

import java.util.List;

public class PlayerTrade {
    @Key
    private Position position;
    @Key
    private boolean trade;
    @Key
    private List<Player> possiblePlayersToDrop;
    @Key
    private List<Player> possiblePlayersToPickUp;

    public PlayerTrade(Position position, List<Player> possiblePlayersToDrop, List<Player> possiblePlayersToPickUp) {
        this.position = position;
        this.possiblePlayersToDrop = possiblePlayersToDrop;
        this.possiblePlayersToPickUp = possiblePlayersToPickUp;
        if( possiblePlayersToDrop.isEmpty() || possiblePlayersToPickUp.isEmpty() ){
            this.trade = false;
        }else{
            this.trade = possiblePlayersToDrop.get(0).getTier() > possiblePlayersToPickUp.get(0).getTier();
        }
    }

    public List<Player> getPossiblePlayersToPickUp() {
        return possiblePlayersToPickUp;
    }

    public List<Player> getPossiblePlayersToDrop() {
        return possiblePlayersToDrop;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isTrade() {
        return trade;
    }
}

