package fantasy.footballer.player;

import fantasy.footballer.espn.api.json.player.EspnPlayerAPI;
import fantasy.footballer.fanduel.player.FanDuelPlayer;

import java.util.HashMap;
import java.util.Map;

public class PlayerIdentifier {

    private final String identifier;

    private static final Map<PlayerIdentifier, PlayerIdentifier> ESPN_EXCEPTIONS;
    static {
        ESPN_EXCEPTIONS = new HashMap<>();
        ESPN_EXCEPTIONS.put(new PlayerIdentifier("mitchelltrubisky"), new PlayerIdentifier("mitchtrubisky"));
    }

    public PlayerIdentifier(String identifier){
        if( identifier == null ) throw new IllegalArgumentException("Identifier can not be null");
        this.identifier = identifier;
    }

    public static PlayerIdentifier createForBorichen(String player,Position position){
        if(position.equals(Position.DEFENCE)){
            String[] words = player.split(" ");
            player = words[words.length-1];
        }

        return new PlayerIdentifier(scrubIdentifier(player));
    }

    public static PlayerIdentifier createForEspn(EspnPlayerAPI EspnPlayerAPI){
        PlayerIdentifier identifier = createForName(EspnPlayerAPI.info.firstName, EspnPlayerAPI.info.lastName);
        if ( ESPN_EXCEPTIONS.containsKey(identifier) ){
            return ESPN_EXCEPTIONS.get(identifier);
        }
        return identifier;
    }

    public static PlayerIdentifier createForFanDuel(FanDuelPlayer player){
        if(Position.DEFENCE.equals(player.getPosition())){
            return new PlayerIdentifier(player.getLastName().toLowerCase());
        }
        return createForName(player.getFirstName(),player.getLastName());
    }

    private static PlayerIdentifier createForName(String firstName, String lastName){
        String identifier = scrubIdentifier(firstName) + scrubIdentifier(lastName);
        return new PlayerIdentifier(identifier);
    }

    private static String scrubIdentifier(String str){
        str = str.toLowerCase();
        str = str.replaceAll(" ii","");
        str = str.replaceAll(" jr.", "");
        str = str.replaceAll(" sr.", "");
        str = str.replaceAll("d/st","");
        return str.trim().replaceAll("[^a-z]","");
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PlayerIdentifier && ((PlayerIdentifier) obj).identifier.equals(this.identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public String toString() {
        return identifier;
    }
}
