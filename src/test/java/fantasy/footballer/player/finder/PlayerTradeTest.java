package fantasy.footballer.player.finder;

import fantasy.footballer.player.Position;
import fantasy.footballer.player.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PlayerTradeTest {
    @Test
    void with_empty_players(){
        // no players
        List<Player> possiblePlayersToDrop = new ArrayList<>();
        List<Player> possiblePlayersToPickUp = new ArrayList<>();
        PlayerTrade playerTrade = new PlayerTrade(Position.DEFENCE,possiblePlayersToDrop,possiblePlayersToPickUp);
        assertFalse(playerTrade.isTrade());
        assertEquals("NO PLAYERS", playerTrade.toString());

        //with one player to drop
        possiblePlayersToDrop.add( mock(Player.class) );
        playerTrade = new PlayerTrade(Position.DEFENCE,possiblePlayersToDrop,possiblePlayersToPickUp);
        assertFalse(playerTrade.isTrade());

        //with one player to pick up
        possiblePlayersToDrop.clear();
        possiblePlayersToPickUp.add( mock(Player.class) );
        playerTrade = new PlayerTrade(Position.DEFENCE,possiblePlayersToDrop,possiblePlayersToPickUp);
        assertFalse(playerTrade.isTrade());
    }
}