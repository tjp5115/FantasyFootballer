package fantasy.footballer.player;

import fantasy.footballer.borischen.PlayerType;
import fantasy.footballer.espn.api.json.player.Player;

public class PlayerIdentifier {

    String identifier;

    private PlayerIdentifier(){

    }

    public static PlayerIdentifier createForBorichen(String player,PlayerType playerType){
        if(playerType.equals(PlayerType.DEFENCE)){
            String[] words = player.split(" ");
            player = words[words.length-1];
        }

        PlayerIdentifier playerIdentifier = new PlayerIdentifier();
        playerIdentifier.identifier = playerIdentifier.scrubIdentifier(player);
        return playerIdentifier;
    }

    public static PlayerIdentifier createForEspn(Player player){
        PlayerIdentifier playerIdentifier = new PlayerIdentifier();
        playerIdentifier.identifier = new StringBuilder()
            .append(playerIdentifier.scrubIdentifier(player.name.firstName))
            .append(playerIdentifier.scrubIdentifier(player.name.lastName))
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
