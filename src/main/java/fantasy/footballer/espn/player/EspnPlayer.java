package fantasy.footballer.espn.player;

import fantasy.footballer.borischen.Position;
import fantasy.footballer.espn.api.json.player.EspnPlayerAPI;
import fantasy.footballer.player.Player;
import fantasy.footballer.player.PlayerIdentifier;

public class EspnPlayer extends Player{
    private final PlayerIdentifier playerIdentifier;
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
