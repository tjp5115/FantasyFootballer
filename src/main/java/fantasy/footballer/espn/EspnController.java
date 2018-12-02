package fantasy.footballer.espn;

import com.google.api.client.http.HttpResponse;
import fantasy.footballer.api.Data;
import fantasy.footballer.api.Root;
import fantasy.footballer.borischen.FantasyFootballTiers;
import fantasy.footballer.borischen.LeagueType;
import fantasy.footballer.espn.api.json.player.EspnPlayerAPI;
import fantasy.footballer.espn.api.json.player.PlayerInfoJSON;
import fantasy.footballer.espn.api.json.scoreboard.ScoreBoard;
import fantasy.footballer.espn.api.league.LeagueInfo;
import fantasy.footballer.espn.api.player.PlayerInfo;
import fantasy.footballer.player.Player;
import fantasy.footballer.player.finder.EspnPlayerFinder;
import fantasy.footballer.player.finder.PlayerTrade;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class EspnController {
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/footballer/espn/trade")
    public Root espnTrade(@RequestParam() Integer teamID,
              @RequestParam() Integer leagueID) throws IOException {
        HttpResponse leagueInfo = new LeagueInfo()
            .forLeague(leagueID)
            .sendRequest();

        ScoreBoard scoreBoard = leagueInfo.parseAs(ScoreBoard.class);

        //get all the players that are in the league.
        Set<Integer> leaguePlayerIds = scoreBoard.matchups.teams.stream()
            .flatMap(team -> team.playerIds.stream())
            .flatMap(playerId -> playerId.ids.stream())
            .collect(Collectors.toSet());

        List<EspnPlayerAPI> leagueEspnPlayerAPIInfo = new ArrayList<>();
        // We have all of the players in the league, but only by ID.
        // We need to map the ID to a something more generic we can match on with other APIs, I.E. their name
        for( int page = 0; true; page++) {
            HttpResponse response = new PlayerInfo()
                .forLeague(leagueID)
                .forPage(page)
                .sendRequest();

            List<EspnPlayerAPI> EspnPlayerAPIS = response.parseAs(PlayerInfoJSON.class).team.EspnPlayerAPIS;
            leagueEspnPlayerAPIInfo.addAll(
                EspnPlayerAPIS.stream()
                    .filter(ESPNPlayer -> ESPNPlayer.teamId != -1)
                    .collect(Collectors.toList())
            );

            leaguePlayerIds.removeAll(EspnPlayerAPIS.stream()
                .map(ESPNPlayer -> ESPNPlayer.name.playerId)
                .collect(Collectors.toSet())
            );

            if (leaguePlayerIds.isEmpty() || EspnPlayerAPIS.isEmpty()) {
                break;
            }
        }

        EspnPlayerFinder espnPlayerFinder = new EspnPlayerFinder( new FantasyFootballTiers(LeagueType.PPR));
        espnPlayerFinder.addEspnPlayers(leagueEspnPlayerAPIInfo);
        espnPlayerFinder.setTeamId(teamID);
        List<PlayerTrade> playerTrades = espnPlayerFinder.findAllPossibleTrades();
        Data<PlayerTrade> playerTradeData = new Data<>();
        playerTradeData.addItems(playerTrades);
        return new Root(playerTradeData);
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/footballer/espn/lineup")
    public Root espnLineup(@RequestParam() Integer teamID,
                     @RequestParam() Integer leagueID) throws IOException {

        HttpResponse leagueInfo = new LeagueInfo()
            .forLeague(leagueID)
            .sendRequest();

        ScoreBoard scoreBoard = leagueInfo.parseAs(ScoreBoard.class);

        //get all the players that are in the league.
        Set<Integer> leaguePlayerIds = scoreBoard.matchups.teams.stream()
            .flatMap(team -> team.playerIds.stream())
            .flatMap(playerId -> playerId.ids.stream())
            .collect(Collectors.toSet());

        List<EspnPlayerAPI> leagueEspnPlayerAPIInfo = new ArrayList<>();
        // We have all of the players in the league, but only by ID.
        // We need to map the ID to a something more generic we can match on with other APIs, I.E. their name
        for( int page = 0; true; page++) {
            HttpResponse response = new PlayerInfo()
                .forLeague(leagueID)
                .forPage(page)
                .sendRequest();

            List<EspnPlayerAPI> EspnPlayerAPIS = response.parseAs(PlayerInfoJSON.class).team.EspnPlayerAPIS;
            leagueEspnPlayerAPIInfo.addAll(
                EspnPlayerAPIS.stream()
                    .filter(ESPNPlayer -> ESPNPlayer.teamId != -1)
                    .collect(Collectors.toList())
            );

            leaguePlayerIds.removeAll(EspnPlayerAPIS.stream()
                .map(ESPNPlayer -> ESPNPlayer.name.playerId)
                .collect(Collectors.toSet())
            );

            if (leaguePlayerIds.isEmpty() || EspnPlayerAPIS.isEmpty()) {
                break;
            }
        }

        EspnPlayerFinder espnPlayerFinder = new EspnPlayerFinder( new FantasyFootballTiers(LeagueType.PPR));
        espnPlayerFinder.addEspnPlayers(leagueEspnPlayerAPIInfo);
        espnPlayerFinder.setTeamId(teamID);

        Data<Player> playerData = new Data<>();
        espnPlayerFinder.rankPlayersForTeam().forEach((position,players) -> playerData.addItems(players));

        return new Root(playerData);
    }

}
