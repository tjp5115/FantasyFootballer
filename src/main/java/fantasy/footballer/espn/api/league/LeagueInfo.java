package fantasy.footballer.espn.api.league;

import fantasy.footballer.espn.api.EspnAPI;

public class LeagueInfo extends EspnAPI{
    public static final String ENDPOINT = "scoreboard";
    public LeagueInfo(){
        super();
        request.appendRawPath(ENDPOINT);
    }
}
