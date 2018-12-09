package fantasy.footballer.borischen;

import fantasy.footballer.player.Player;
import fantasy.footballer.player.PlayerIdentifier;
import fantasy.footballer.player.Position;

public class BorischenPlayer extends Player{
    private final String playerName;

    public BorischenPlayer(Position position, String player, Integer tier) {
        super(position);
        playerIdentifier = PlayerIdentifier.createForBorichen(player, getPosition());
        this.tier = tier;
        this.playerName = player.trim();

        int i = this.playerName.indexOf(' ');
        this.setFirstName(this.playerName.substring(0,i));
        this.setLastName(this.playerName.substring(i,this.playerName.length()));
    }

    @Override
    public PlayerIdentifier getPlayerIdentifier() {
        return playerIdentifier;
    }

    @Override
    public String toString() {
        return playerName + " ( tier " + tier + " ) ";
    }
}
