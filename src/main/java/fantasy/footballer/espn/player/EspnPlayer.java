package fantasy.footballer.espn.player;

import com.google.api.client.util.Key;
import fantasy.footballer.player.Position;
import fantasy.footballer.espn.api.json.player.EspnPlayerAPI;
import fantasy.footballer.player.Player;
import fantasy.footballer.player.PlayerIdentifier;

public class EspnPlayer extends Player{
    @Key
    private int teamId;

    public EspnPlayer(EspnPlayerAPI player) {
        super(Position.fromEspn(player.name.positionId));
        setFirstName(player.name.firstName);
        setLastName(player.name.lastName);
        teamId = player.teamId;
        playerIdentifier = PlayerIdentifier.createForEspn(player);
    }

    public int getTeamId() {
        return teamId;
    }

    @Override
    public PlayerIdentifier getPlayerIdentifier() {
        return playerIdentifier;
    }
}
