package fantasy.footballer.borischen;

public class FantasyFootballTiers {
    private final static String BASE_URL = "https://s3-us-west-1.amazonaws.com/fftiers/out/text_";
    private final static String FILE_EXTENSION = ".txt";
    private String url;

    public FantasyFootballTiers(PlayerType playerType, LeagueType leagueType){
        StringBuilder sb = new StringBuilder(BASE_URL)
            .append(playerType);
        if( playerType.hasLeagueType ) {
            sb.append('-')
                .append(leagueType);
        }
        sb.append(FILE_EXTENSION);
        url = sb.toString();
    }
    public String getUrl(){
        return url;
    }
}
