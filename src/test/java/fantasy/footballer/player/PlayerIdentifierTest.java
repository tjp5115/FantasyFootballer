package fantasy.footballer.player;

import fantasy.footballer.borischen.Position;
import fantasy.footballer.espn.api.json.player.ESPNPlayer;
import fantasy.footballer.espn.api.json.player.PlayerName;
import fantasy.footballer.fanduel.player.FanDuelPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerIdentifierTest {
    @Test
    void create_for_fanduel_defence(){
        FanDuelPlayer player = new FanDuelPlayer(Position.DEFENCE);
        player.setFirstName("NOT_USED");
        player.setLastName("DEFENCE_NAME");
        PlayerIdentifier results = PlayerIdentifier.createForFanDuel(player);
        PlayerIdentifier expected = new PlayerIdentifier("DEFENCE_NAME".toLowerCase());
        assertEquals(expected,results);
    }

    @Test
    void create_for_fanduel_offence(){
        FanDuelPlayer player = new FanDuelPlayer(Position.WIDE_RECEIVER);
        player.setFirstName("FIRST_NAME12");
        player.setLastName("LAST_NAME43");
        PlayerIdentifier results = PlayerIdentifier.createForFanDuel(player);
        PlayerIdentifier expected = new PlayerIdentifier("FIRSTNAMELASTNAME".toLowerCase());
        assertEquals(expected,results);
    }

    @Test
    void create_for_ESPN_offence(){
        ESPNPlayer player = new ESPNPlayer();
        player.name = new PlayerName();
        player.name.firstName = "First Name II";
        player.name.lastName = "Last Name Jr.";
        PlayerIdentifier results = PlayerIdentifier.createForEspn(player);
        PlayerIdentifier expected = new PlayerIdentifier("FIRSTNAMELASTNAME".toLowerCase());
        assertEquals(expected,results);
    }

    @Test
    void create_for_ESPN_defence(){
        ESPNPlayer player = new ESPNPlayer();
        player.name = new PlayerName();
        player.name.firstName = "First Name SR.";
        player.name.lastName = "Last Name d/st";
        PlayerIdentifier results = PlayerIdentifier.createForEspn(player);
        PlayerIdentifier expected = new PlayerIdentifier("FIRSTNAMELASTNAME".toLowerCase());
        assertEquals(expected,results);
    }
}