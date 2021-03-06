package fantasy.footballer.borischen;

public enum LeagueType {
    PPR("PPR"),
    NORMAL("NORMAL"),
    HALF("HALF");

    String abbreviation;
    LeagueType(String type) {
        abbreviation = type;
    }

    @Override
    public String toString() {
        return abbreviation;
    }
}
