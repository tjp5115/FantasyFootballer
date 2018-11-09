package fantasy.footballer.borischen;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import fantasy.footballer.player.Player;

import java.io.IOException;
import java.util.*;

public class FantasyFootballTiers {
    private final static String BASE_URL = "https://s3-us-west-1.amazonaws.com/fftiers/out/text_";
    private final static String FILE_EXTENSION = ".txt";

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private final LeagueType leagueType;

    Map<Position,List<Player>> tier = new HashMap<>();


    public FantasyFootballTiers(LeagueType leagueType){
       this.leagueType = leagueType;
    }

    String getUrl(Position position){
        StringBuilder sb = new StringBuilder(BASE_URL)
            .append(position);
        if( position.hasLeagueType ) {
            sb.append('-')
                .append(leagueType);
        }
        sb.append(FILE_EXTENSION);
        return sb.toString();
    }

    public List<Player> getTiers(Position position){
        return tier.computeIfAbsent(position,this::computeTeir);
    }

    private List<Player> computeTeir(Position position) {
        String[] response = new String[0];
        try {
            response = sendRequest(new GenericUrl(getUrl(position))).split("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Player> tierList = new ArrayList<>();

        for( String tierAndPlayers : response ){
            int i = tierAndPlayers.indexOf(':');
            Integer tier = new Scanner(tierAndPlayers.substring(0,i)).useDelimiter("\\D+").nextInt();

            String[] players = tierAndPlayers.substring(i+1,tierAndPlayers.length()).split(",");
            for (String player : players) {
                tierList.add(new BorischenPlayer(position, player, tier ) );
            }
        }
        return tierList;
    }


    private String sendRequest(GenericUrl url) throws IOException {
        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();
        HttpResponse response = requestFactory.buildGetRequest(url).execute();
        return response.parseAsString();
    }
}
