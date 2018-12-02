package fantasy.footballer.fanduel.player;

import com.google.api.client.util.Key;
import fantasy.footballer.player.Position;
import fantasy.footballer.player.Player;
import fantasy.footballer.player.PlayerIdentifier;

public class FanDuelPlayer extends Player {
    @Key
    private Integer salary;

    public FanDuelPlayer(Position position) {
        super(position);
    }

    public void setSalary(String salary) {
        this.salary = Integer.parseInt(salary);
    }

    @Override
    public PlayerIdentifier getPlayerIdentifier() {
        if( playerIdentifier == null ) {
            this.playerIdentifier = PlayerIdentifier.createForFanDuel(this);
        }
        return playerIdentifier;
    }

    public int getSalary() {
        return salary;
    }

    @Override
    public String toString(){
        return getFirstName() + " " + getLastName() + " - salary:" + salary;
    }
}
