package fantasy.footballer.player;

import fantasy.footballer.borischen.Position;
import fantasy.footballer.espn.api.json.player.ESPNPlayer;
import fantasy.footballer.fanduel.player.FanDuelPlayer;

public class PlayerIdentifier {

    String identifier;

    private PlayerIdentifier(){

    }

    public static PlayerIdentifier createForBorichen(String player,Position position){
        if(position.equals(Position.DEFENCE)){
            String[] words = player.split(" ");
            player = words[words.length-1];
        }

        PlayerIdentifier playerIdentifier = new PlayerIdentifier();
        playerIdentifier.identifier = playerIdentifier.scrubIdentifier(player);
        return playerIdentifier;
    }

    public static PlayerIdentifier createForEspn(ESPNPlayer ESPNPlayer){
        return createForName(ESPNPlayer.name.firstName, ESPNPlayer.name.lastName);
    }

    public static PlayerIdentifier createForFanDuel(FanDuelPlayer player){
        if(Position.DEFENCE.equals(player.getPosition())){
            PlayerIdentifier playerIdentifier = new PlayerIdentifier();
            playerIdentifier.identifier = player.getLastName().toLowerCase();
            return playerIdentifier;
        }
        return createForName(player.getFirstName(),player.getLastName());
    }

    private static PlayerIdentifier createForName(String firstName, String lastName){
        PlayerIdentifier playerIdentifier = new PlayerIdentifier();
        playerIdentifier.identifier = new StringBuilder()
            .append(playerIdentifier.scrubIdentifier(firstName))
            .append(playerIdentifier.scrubIdentifier(lastName))
            .toString();
        return playerIdentifier;
    }

    private String scrubIdentifier(String str){
        str = str.toLowerCase();
        str = str.replaceAll(" ii","");
        str = str.replaceAll(" jr.", "");
        str = str.replaceAll(" sr.", "");
        str = str.replaceAll("d/st","");
        return str.trim().replaceAll("[^a-z]","");
    }

    @Override
    public boolean equals(Object obj) {
        if ( obj instanceof PlayerIdentifier ){
            return ((PlayerIdentifier) obj).identifier.equals(this.identifier);
        }
        return false;
    }

    @Override
    public String toString() {
        return identifier;
    }
}
