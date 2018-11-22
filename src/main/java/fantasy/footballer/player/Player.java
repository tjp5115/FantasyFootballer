package fantasy.footballer.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.api.client.util.Key;

public abstract class Player {
    private Position position;
    @Key
    private String lastName;
    @Key
    private String firstName;
    @Key
    protected Integer tier;

    @JsonIgnore
    protected transient PlayerIdentifier playerIdentifier;

    protected Player(Position position){
        this.position = position;
    }

    public Position getPosition(){
        return position;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public void setLastName(String lastName){
        this.lastName = lastName.trim();
    }

    public String getFirstName(){
        return this.firstName;
    }

    public String getLastName(){
        return this.lastName;
    }

    public Integer getTier() {
        return tier;
    }

    public void setTier(Integer tier){
        this.tier = tier;
    }

    public abstract PlayerIdentifier getPlayerIdentifier();

    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) {
            return false;
        }

        PlayerIdentifier playerIdentifier = null;
        if ( obj instanceof Player ){
            playerIdentifier = ((Player) obj).getPlayerIdentifier();
        }else if( obj instanceof PlayerIdentifier ){
            playerIdentifier = (PlayerIdentifier)obj;
        }

        return playerIdentifier != null && playerIdentifier.equals(getPlayerIdentifier());
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " ( tier " + tier + " ) " ;
    }
}
