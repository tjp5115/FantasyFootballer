package fantasy.footballer.borischen;

import fantasy.footballer.player.Player;
import fantasy.footballer.player.PlayerIdentifier;

public class BorischenPlayer extends Player{
    private final PlayerIdentifier playerIdentifier;
    private final String playerName;

    public BorischenPlayer(Position position, String player, Integer tier) {
        super(position);
        playerIdentifier = PlayerIdentifier.createForBorichen(player, getPosition());
        this.tier = tier;
        this.playerName = player;
    }

    @Override
    public PlayerIdentifier getPlayerIdentifier() {
        return playerIdentifier;
    }

    @Override
    public String toString() {
        return playerName;
    }
}
