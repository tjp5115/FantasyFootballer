package fantasy.footballer.player;

import fantasy.footballer.borischen.Position;

public abstract class Player {
    private Position position;
    private String lastName;
    private String firstName;
    protected Integer tier;

    protected Player(Position position){
        this.position = position;
    }

    public Position getPosition(){
        return position;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
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
