package fantasy.footballer.borischen;

// todo this should be called position. Needs to be reworked -- move to a generic place and let everyone use it.
public enum PlayerType {
    WIDE_RECEIVER("WR",true),
    RUNNING_BACK("RB",true),
    TIGHT_END("TE",true),
    QUARTER_BACK("QB"),
    KICKER("K"),
    DEFENCE("DST");


    String abbreviation;
    boolean hasLeagueType;
    PlayerType(String type) {
        abbreviation = type;
    }

    PlayerType(String type, boolean hasLeagueType){
        abbreviation = type;
        this.hasLeagueType = hasLeagueType;
    }

    @Override
    public String toString() {
        return abbreviation;
    }


    public static PlayerType fromEspn(int i){
        switch( i ){
            case 1: return QUARTER_BACK;
            case 2: return RUNNING_BACK;
            case 3: return WIDE_RECEIVER;
            case 4: return TIGHT_END;
            case 5: return KICKER;
            case 16: return DEFENCE;
            default: throw new IllegalArgumentException("'"+Integer.toString(i)+"' is not a valid position");
        }
    }

    public String getName() {
        return abbreviation;
    }

    public static PlayerType fromFanDuel(String postition) {
        switch( postition ){
            case "QB": return QUARTER_BACK;
            case "RB": return RUNNING_BACK;
            case "WR": return WIDE_RECEIVER;
            case "TE": return TIGHT_END;
            case "K": return KICKER;
            case "D": return DEFENCE;
            default: throw new IllegalArgumentException("'"+postition+"' is not a valid position");
        }
    }
}
