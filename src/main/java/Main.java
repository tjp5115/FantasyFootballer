import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.GenericJson;
import fantasy.footballer.espn.api.json.player.Player;
import fantasy.footballer.espn.api.json.player.PlayerInfoJSON;
import fantasy.footballer.espn.api.json.scoreboard.ScoreBoard;
import fantasy.footballer.espn.api.league.LeagueInfo;
import fantasy.footballer.espn.api.player.PlayerInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    private static final int leagueID = 102116;

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

            List<Player> players = response.parseAs(PlayerInfoJSON.class).players.player;
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


        HttpResponse response1 = new PlayerInfo()
            .forPage(1)
            .sendRequest();
        GenericJson genJson = response1.parseAs(GenericJson.class);
        Map<Integer, List<Player>> positions = leaguePlayerInfo.stream()
            .collect( Collectors.groupingBy( player -> player.name.positionId ));
        System.out.println(genJson.toPrettyString());
    }
}
