package fantasy.footballer.borischen;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import fantasy.footballer.player.Player;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FantasyFootballTiers {
    private final static String BASE_URL = "https://s3-us-west-1.amazonaws.com/fftiers/out/text_";
    private final static String FILE_EXTENSION = ".txt";

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private final LeagueType leagueType;

    Map<PlayerType,List<Player>> tier = new HashMap<>();


    public FantasyFootballTiers(LeagueType leagueType){
       this.leagueType = leagueType;
    }

    String getUrl(PlayerType playerType){
        StringBuilder sb = new StringBuilder(BASE_URL)
            .append(playerType);
        if( playerType.hasLeagueType ) {
            sb.append('-')
                .append(leagueType);
        }
        sb.append(FILE_EXTENSION);
        return sb.toString();
    }

    public List<Player> getTiers(PlayerType playerType){
        return tier.computeIfAbsent(playerType,this::computeTeir);
    }

    private List<Player> computeTeir(PlayerType playerType) {
        String[] response = new String[0];
        try {
            response = sendRequest(new GenericUrl(getUrl(playerType))).split("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Player> tierList = new ArrayList<>();

        for( String tierAndPlayers : response ){
            int i = tierAndPlayers.indexOf(':');
            Integer tier = new Scanner(tierAndPlayers.substring(0,i)).useDelimiter("\\D+").nextInt();

            String[] players = tierAndPlayers.substring(i+1,tierAndPlayers.length()).split(",");
            for (String player : players) {
                tierList.add(new BorischenPlayer( playerType, player, tier ) );
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
