package fantasy.footballer.borischen;

public enum LeagueType {
    PPR("PPR"),
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
