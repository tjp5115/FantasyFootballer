package fantasy.footballer.espn.api.player;

import fantasy.footballer.espn.api.EspnAPI;

public class PlayerInfo extends EspnAPI{
    public static final String ENDPOINT = "playerInfo";
    public PlayerInfo(){
        super();
        request.appendRawPath(ENDPOINT);
    }

    public PlayerInfo forLeague(Integer league){
        request.put("leagueId",league);
        return this;
    }

    public PlayerInfo forPage(Integer page){
        request.put("offset",page);
        return this;
    }

    public PlayerInfo forPlayerId(String playerId){
        request.put("playerId",playerId);
        return this;
    }
}
