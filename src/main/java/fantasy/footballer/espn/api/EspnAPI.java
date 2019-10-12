package fantasy.footballer.espn.api;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;

public class EspnAPI {
    private static final String ESPN_API = "https://fantasy.espn.com/apis/v3/games/ffl/seasons/2019/segments/0/leagues/";
    /*
    different views
view=mDraftDetail
view=mLiveScoring
view=mMatchupScore
view=mPendingTransactions
view=mPositionalRatings
view=mSettings
view=mTeam
view=modular
view=mNav
view=mMatchupScore
view=kona_player_info
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
        HttpRequest httprequest = requestFactory.buildGetRequest(request);
        httprequest.getHeaders().setCookie("14232620376587108709023%7CMCAID%7CNONE%7CMCOPTOUT-1570399422s%7CNONE%7CMCAAMLH-1568935867%7C7%7CMCAAMB-1570392218%7Cj8Odv6LonN4r3an7LhD3WZrU1bUpAkFkkiY1ncBR96t2PTI%7CMCSYNCSOP%7C411-18155%7CvVersion%7C3.1.2; s_ecid=MCMID%7C02422244152236362084085205036958203108; UNID=a9cc48ab-f5f7-449f-9d4b-dd7677a308f8; espn_s2=AECFXqrb0iUsjM70qxXwzOuQQI7Bm%2BkZWchOe67pWWnE9OM1SrlbSg8wHvRIX60MIWauhbZr%2BqTtZ%2Fgx6T%2BFFfKPl2hyuElIQ0u42zvv0aW4mv2db3OYPzC9sMUbj24nPZFwtkmi6J0tg9Z1dBv1iResK%2BaRyWwgdVUcqpVzAC6jSz3GiIHV7UVny57xZF9DsXVEatc7NacfPr9qMmhJVvmxjkn8hulCTQcMpHhuAXmLbKaZcpHJ7x6p1bXhUzeBKHxZXssvy9ToX2GoQ30EorGW; ESPN-ONESITE.WEB-PROD-ac=XUS; espnAuth={\"swid\":\"{9E740C71-A401-4C38-9202-8748CCD921CC}\"}; _cb_ls=1; _cb=Bw9ZEePyWqkCoxrVs; _chartbeat2=.1566220895965.1568331137825.0100001000010001.DHtrbsB-q8mjB1sDmwCo0biKBGYIIm.1; UNID=a9cc48ab-f5f7-449f-9d4b-dd7677a308f8; s_c6=1568331148897-Repeat; trc_cookie_storage=taboola%2520global%253Auser-id%3Db38c95af-3470-494d-8161-7d17c7a08a33-tuct4542820; s_pers=%20s_c24%3D1570842132622%7C1665450132622%3B%20s_c24_s%3DLess%2520than%25207%2520days%7C1570843932622%3B%20s_gpv_pn%3Despndevcenter%253Adocumentation%253Aindex%7C1570843932628%3B; s_vi=[CS]v1|2EBA422905079138-4000010D40072708[CE]; s_sess=%20s_cc%3Dtrue%3B%20s_omni_lid%3D%3B%20s_sq%3D%3B%20s_ppv%3D38%3B");
        return httprequest.execute();
    }

    public EspnAPI forLeague(Integer leagueId){
        request.appendRawPath(String.valueOf(leagueId));
        return this;
    }

    public EspnAPI forYear(String year){
        request.put("seasonId",year);
        return this;
    }

    public EspnAPI usePlayerView(){
        request.put("view","kona_player_info");
        return this;
    }

    public GenericUrl getRequest(){
        return request.clone();
    }
}
