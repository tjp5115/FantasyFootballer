package fantasy.footballer.espn.api;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;

public class EspnAPI {
    private static final String ESPN_API = "http://games.espn.com/ffl/api/v2/";
    /*
    Endpoints not accounted for:
        leagueSettings
        player/news
        recentActivity
        leagueSchedules
        teams
        rosterInfo
        schedule
        polls
        messageboard
        status
        teams/pendingMoveBatches
        tweets
        stories
        livescoring (doesn’t seem to be working right)
        boxscore
     */
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new GsonFactory();
    protected GenericUrl request;

    public EspnAPI(){
        request = new GenericUrl(ESPN_API);
    }

    public HttpResponse sendRequest() throws IOException {
        HttpRequestFactory requestFactory =
            HTTP_TRANSPORT.createRequestFactory(request -> request.setParser(new JsonObjectParser(JSON_FACTORY)));
        HttpResponse response = requestFactory.buildGetRequest(request).execute();
        return response;
    }

    public EspnAPI forLeague(Integer leagueId){
        request.put("leagueId", leagueId);
        return this;
    }

    public EspnAPI forYear(String year){
        request.put("seasonId",year);
        return this;
    }

    public EspnAPI forWeek(String week){
        request.put("matchupPeriodId",week);
        return this;
    }

    public GenericUrl getRequest(){
        return request.clone();
    }
}
