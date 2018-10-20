import com.google.api.client.http.HttpResponse;
import fantasy.footballer.borischen.FantasyFootballTiers;
import fantasy.footballer.espn.api.json.player.Player;
import fantasy.footballer.espn.api.json.player.PlayerInfoJSON;
import fantasy.footballer.espn.api.json.scoreboard.ScoreBoard;
import fantasy.footballer.espn.api.league.LeagueInfo;
import fantasy.footballer.espn.api.player.PlayerInfo;
import fantasy.footballer.player.finder.EspnPlayerFinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    private static final int leagueID = 102116; //blaz
    private static final int MY_TEAM_ID = 4; //blaz
    //private static final int leagueID = 155338; //burn
    //private static final int MY_TEAM_ID = 15; //burn

    public static void main(String args[]) throws IOException {


        HttpResponse leagueInfo = new LeagueInfo()
            .forLeague(leagueID)
            .sendRequest();

        ScoreBoard scoreBoard = leagueInfo.parseAs(ScoreBoard.class);

        Set<Integer> leaguePlayerIds = scoreBoard.matchups.teams.stream()
            .flatMap(team -> team.playerIds.stream())
            .flatMap(playerId -> playerId.ids.stream())
            .collect(Collectors.toSet());

        List<Player> leaguePlayerInfo = new ArrayList<>();
        for( int page = 0; true; page++) {
            HttpResponse response = new PlayerInfo()
                .forLeague(leagueID)
                .forPage(page)
                .sendRequest();

            List<Player> players = response.parseAs(PlayerInfoJSON.class).team.players;
            leaguePlayerInfo.addAll(
                players.stream()
                    .filter(player -> player.teamId != -1)
                    .collect(Collectors.toList())
            );

            leaguePlayerIds.removeAll(players.stream()
                .map(player -> player.name.playerId)
                .collect(Collectors.toSet())
            );

            if (leaguePlayerIds.isEmpty() || players.isEmpty()) {
                break;
            }
        }

        EspnPlayerFinder espnPlayerFinder = new EspnPlayerFinder( new FantasyFootballTiers());
        espnPlayerFinder.addEspnPlayers(leaguePlayerInfo);
        espnPlayerFinder.setMyTeam(MY_TEAM_ID);
        espnPlayerFinder.findAllPossible();
    }
}
