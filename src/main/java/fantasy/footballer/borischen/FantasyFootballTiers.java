package fantasy.footballer.borischen;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;
import java.util.*;

public class FantasyFootballTiers {
    private final static String BASE_URL = "https://s3-us-west-1.amazonaws.com/fftiers/out/text_";
    private final static String FILE_EXTENSION = ".txt";
    private String url;

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

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
    String getUrl(){
        return url;
    }

    public Map<Integer,List<String>> getTiers(){
        String[] response = new String[0];
        try {
             response = sendRequest().split("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<Integer,List<String>> tierList = new HashMap<>();

        for( String teirAndPlayers : response ){
            int i = teirAndPlayers.indexOf(':');
            Integer teir = new Scanner(teirAndPlayers.substring(0,i)).useDelimiter("\\D+").nextInt();

            String[] players = teirAndPlayers.substring(i+1,teirAndPlayers.length()).split(",");
            tierList.put(teir, Arrays.asList(players));
        }

        return tierList;
    }




    private String sendRequest() throws IOException {
        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();
        HttpResponse response = requestFactory.buildGetRequest(new GenericUrl(url)).execute();
        return response.parseAsString();
    }
}
