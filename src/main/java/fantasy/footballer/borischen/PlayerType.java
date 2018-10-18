package fantasy.footballer.borischen;

public enum PlayerType {
    WIDE_RECIEVER("WR",true),
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
}
