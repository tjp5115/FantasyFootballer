package fantasy.footballer.player.finder;

import fantasy.footballer.borischen.Position;
import fantasy.footballer.player.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PlayerPositionTest {
    @Test
    void with_empty_players(){
        // no players
        List<Player> possiblePlayersToDrop = new ArrayList<>();
        List<Player> possiblePlayersToPickUp = new ArrayList<>();
        PlayerPosition playerPosition = new PlayerPosition(Position.DEFENCE,possiblePlayersToDrop,possiblePlayersToPickUp);
        assertFalse(playerPosition.shouldTrade());
        assertEquals("NO PLAYERS",playerPosition.toString());

        //with one player to drop
        possiblePlayersToDrop.add( mock(Player.class) );
        playerPosition = new PlayerPosition(Position.DEFENCE,possiblePlayersToDrop,possiblePlayersToPickUp);
        assertFalse(playerPosition.shouldTrade());

        //with one player to pick up
        possiblePlayersToDrop.clear();
        possiblePlayersToPickUp.add( mock(Player.class) );
        playerPosition = new PlayerPosition(Position.DEFENCE,possiblePlayersToDrop,possiblePlayersToPickUp);
        assertFalse(playerPosition.shouldTrade());
    }
}