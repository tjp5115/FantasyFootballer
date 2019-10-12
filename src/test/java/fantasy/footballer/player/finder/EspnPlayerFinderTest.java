package fantasy.footballer.player.finder;

import fantasy.footballer.borischen.BorischenPlayer;
import fantasy.footballer.borischen.FantasyFootballTiers;
import fantasy.footballer.player.Position;
import fantasy.footballer.espn.api.json.player.EspnPlayerAPI;
import fantasy.footballer.espn.api.json.player.PlayerInfo;
import fantasy.footballer.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class EspnPlayerFinderTest {
    @Mock
    FantasyFootballTiers tierGenerator;

    private static int myTeamId = 1;

    @BeforeEach
    void before(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void find_player_with_that_should_be_traded() {
        EspnPlayerFinder espnPlayerFinder = new EspnPlayerFinder(tierGenerator);
        espnPlayerFinder.setTeamId(myTeamId);

        List<EspnPlayerAPI> espnPlayerAPIS = new ArrayList<>();
        espnPlayerAPIS.add(createESPNPlayer("FIRST","TRADE", myTeamId));
        espnPlayerAPIS.add(createESPNPlayer("FIRST","POPULAR",2));

        espnPlayerFinder.addEspnPlayers(espnPlayerAPIS);


        List<Player> players = new ArrayList<>();
        players.add(createPlayer(6, espnPlayerAPIS.get(0)));
        players.add(createPlayer(1, espnPlayerAPIS.get(1)));
        players.add(createPlayer(9,"FIRST","BAD_PLAYER"));
        players.add(createPlayer(5,"FIRST","PICKUP"));
        when(tierGenerator.getTiers(Position.QUARTER_BACK)).thenReturn(players);

        PlayerTrade playerTrade = espnPlayerFinder.findPossibleTradesForPosition(Position.QUARTER_BACK);

        assertTrue(playerTrade.isTrade());
        assertEquals(createPlayer(5,"FIRST","PICKUP"), playerTrade.getPossiblePlayersToPickUp().get(0));
        assertEquals(1,  playerTrade.getPossiblePlayersToPickUp().size());
        assertEquals(createPlayer(6,"FIRST","TRADE"), playerTrade.getPossiblePlayersToDrop().get(0));
        assertEquals(1, playerTrade.getPossiblePlayersToDrop().size());
    }

    @Test
    void find_player_with_that_should_not_be_traded() {
        EspnPlayerFinder espnPlayerFinder = new EspnPlayerFinder(tierGenerator);
        espnPlayerFinder.setTeamId(myTeamId);

        List<EspnPlayerAPI> espnPlayerAPIS = new ArrayList<>();
        espnPlayerAPIS.add(createESPNPlayer("FIRST","NO_TRADE", myTeamId));
        espnPlayerAPIS.add(createESPNPlayer("FIRST","POPULAR",2));

        espnPlayerFinder.addEspnPlayers(espnPlayerAPIS);


        List<Player> players = new ArrayList<>();
        players.add(createPlayer(5, espnPlayerAPIS.get(0)));
        players.add(createPlayer(1, espnPlayerAPIS.get(1)));
        players.add(createPlayer(9,"FIRST","BAD_PLAYER"));
        players.add(createPlayer(5,"FIRST","PICKUP"));
        when(tierGenerator.getTiers(Position.QUARTER_BACK)).thenReturn(players);

        PlayerTrade playerTrade = espnPlayerFinder.findPossibleTradesForPosition(Position.QUARTER_BACK);

        assertFalse(playerTrade.isTrade());
        assertEquals(createPlayer(5,"FIRST","PICKUP"), playerTrade.getPossiblePlayersToPickUp().get(0));
        assertEquals(1,  playerTrade.getPossiblePlayersToPickUp().size());
        assertEquals(createPlayer(5,"FIRST","NO_TRADE"), playerTrade.getPossiblePlayersToDrop().get(0));
        assertEquals(1, playerTrade.getPossiblePlayersToDrop().size());
    }

    @Test
    void find_multiple_players_that_should_be_traded() {
        EspnPlayerFinder espnPlayerFinder = new EspnPlayerFinder(tierGenerator);
        espnPlayerFinder.setTeamId(myTeamId);

        List<EspnPlayerAPI> espnPlayerAPIS = new ArrayList<>();
        espnPlayerAPIS.add(createESPNPlayer("FIRST","TRADE_A", myTeamId));
        espnPlayerAPIS.add(createESPNPlayer("FIRST","TRADE_B", myTeamId));
        espnPlayerAPIS.add(createESPNPlayer("FIRST","POPULAR",2));

        espnPlayerFinder.addEspnPlayers(espnPlayerAPIS);


        List<Player> players = new ArrayList<>();
        players.add(createPlayer(6, espnPlayerAPIS.get(0)));
        players.add(createPlayer(7, espnPlayerAPIS.get(1)));
        players.add(createPlayer(1, espnPlayerAPIS.get(2)));
        players.add(createPlayer(9,"FIRST","BAD_PLAYER"));
        players.add(createPlayer(5,"FIRST","PICKUP"));
        when(tierGenerator.getTiers(Position.QUARTER_BACK)).thenReturn(players);

        PlayerTrade playerTrade = espnPlayerFinder.findPossibleTradesForPosition(Position.QUARTER_BACK);

        assertTrue(playerTrade.isTrade());
        assertEquals(createPlayer(5,"FIRST","PICKUP"), playerTrade.getPossiblePlayersToPickUp().get(0));
        assertEquals(1,  playerTrade.getPossiblePlayersToPickUp().size());
        assertTrue(playerTrade.getPossiblePlayersToDrop().contains(createPlayer(6,"FIRST","TRADE_A")));
        assertTrue(playerTrade.getPossiblePlayersToDrop().contains(createPlayer(7,"FIRST","TRADE_B")));
        assertEquals(2, playerTrade.getPossiblePlayersToDrop().size());
    }

    private Player createPlayer(int i, EspnPlayerAPI espnPlayerAPI) {
        return createPlayer(i, espnPlayerAPI.info.firstName, espnPlayerAPI.info.lastName);
    }

    private EspnPlayerAPI createESPNPlayer(String first, String last, int teamId) {
        PlayerInfo playerName = new PlayerInfo(first,last);
        playerName.positionId = 1; //QB
        EspnPlayerAPI espnPlayerAPI = new EspnPlayerAPI(playerName);
        espnPlayerAPI.teamId = teamId;
        return espnPlayerAPI;
    }

    private Player createPlayer(int tier, String firstName, String lastName) {
        return new BorischenPlayer(Position.QUARTER_BACK, firstName + " " + lastName,tier);
    }


}