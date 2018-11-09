package fantasy.footballer.player.finder;

import fantasy.footballer.borischen.BorischenPlayer;
import fantasy.footballer.borischen.FantasyFootballTiers;
import fantasy.footballer.borischen.Position;
import fantasy.footballer.espn.api.json.player.ESPNPlayer;
import fantasy.footballer.espn.api.json.player.PlayerName;
import fantasy.footballer.player.Player;
import fantasy.footballer.player.PlayerIdentifier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EspnPlayerFinderTest {
    @Mock
    FantasyFootballTiers tierGenerator;

    @BeforeEach
    void before(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findPossiblePlayersForPosition() {
        EspnPlayerFinder espnPlayerFinder = new EspnPlayerFinder(tierGenerator);
        espnPlayerFinder.setMyTeam(1);

        List<ESPNPlayer> espnPlayers = new ArrayList<>();
        espnPlayers.add(createESPNPlayer("FIRST","TRADE",1));
        espnPlayers.add(createESPNPlayer("FIRST","POPULAR",2));

        espnPlayerFinder.addEspnPlayers(espnPlayers);


        List<Player> players = new ArrayList<>();
        players.add(createPlayer(1,"FIRST","POPULAR"));
        players.add(createPlayer(9,"FIRST","BAD_PLAYER"));
        players.add(createPlayer(6,"FIRST","TRADE"));
        players.add(createPlayer(5,"FIRST","PICKUP"));
        when(tierGenerator.getTiers(Position.QUARTER_BACK)).thenReturn(players);

        PlayerPosition playerPosition = espnPlayerFinder.findPossiblePlayersForPosition(Position.QUARTER_BACK);

        assertTrue(playerPosition.shouldTrade());
        assertEquals(5, playerPosition.getBestTierAvailableToPickUp());
        assertEquals(6, playerPosition.getWorstTierOnTeam());
        assertEquals(createPlayer(5,"FIRST","PICKUP"), playerPosition.getPossiblePlayersToPickUp().get(0));
        assertEquals(1,  playerPosition.getPossiblePlayersToPickUp().size());
        assertEquals(createPlayer(6,"FIRST","TRADE"), playerPosition.getPossiblePlayersToDrop().get(0));
        assertEquals(1, playerPosition.getPossiblePlayersToDrop().size());
    }

    private ESPNPlayer createESPNPlayer(String first, String last, int teamId) {
        PlayerName playerName = new PlayerName(first,last);
        playerName.positionId = 1; //QB
        ESPNPlayer espnPlayer = new ESPNPlayer(playerName);
        espnPlayer.teamId = teamId;
        return espnPlayer;
    }

    private Player createPlayer(int tier, String firstName, String lastName) {
        return new BorischenPlayer(Position.QUARTER_BACK, firstName + " " + lastName,tier);
    }


}