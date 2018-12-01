package fantasy.footballer;

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
import fantasy.footballer.fanduel.player.FanDuelPlayer;
import fantasy.footballer.fanduel.player.PlayerList;
import fantasy.footballer.player.Player;
import fantasy.footballer.player.Position;
import fantasy.footballer.player.finder.EspnPlayerFinder;
import fantasy.footballer.player.finder.FanduelPlayerFinder;
import fantasy.footballer.player.finder.PlayerTrade;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class FootballerController {
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

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/footballer/fanduel/")
    public Root fanduelTierRanking(@RequestParam() Integer depth) throws IOException {
        String fileName = "FanDuel.csv";
        List<FanDuelPlayer> players = PlayerList.getPlayerListFromFile("C:\\Users\\tydro\\IdeaProjects\\FantasyFootballer\\resource\\" + fileName);
        FanduelPlayerFinder fanduelPlayerFinder = new FanduelPlayerFinder(players,new FantasyFootballTiers(LeagueType.HALF));

        for (int i = 1; i < 9; ++i) {
            System.out.println("Tier " + i);
            fanduelPlayerFinder.findCheapestPlayersForTier(i).forEach((key, value) -> {
                if(!Position.KICKER.equals(key)) {
                    System.out.println(key.getName() + " : " + value);
                }
            });
            System.out.println();
        }

        Data<FanDuelPlayer> fanDuelPlayerData = new Data<>();
        fanDuelPlayerData.addItems(players);
        System.out.println("Find For Salary");
        return null;
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/footballer/fanduel/salary")
    public Root fanduelForSalary(@RequestParam() Integer targetSalary) throws IOException {
        String fileName = "FanDuel.csv";
        List<FanDuelPlayer> players = PlayerList.getPlayerListFromFile("C:\\Users\\tydro\\IdeaProjects\\FantasyFootballer\\resource\\" + fileName);
        FanduelPlayerFinder fanduelPlayerFinder = new FanduelPlayerFinder(players,new FantasyFootballTiers(LeagueType.HALF));
        Data<Player> fanDuelPlayerData = new Data<>();
        for (int i = 1; i < 9; ++i) {
            System.out.println("Tier " + i);
                //if(!Position.KICKER.equals(key)) {
                   // fanDuelPlayerData.addItems(fanduelPlayerFinder.findCheapestPlayersForTier(i));
               // }
        }


        System.out.println("Find For Salary");
       // return fanduelPlayerFinder.findFlexPlayer(6800);
        return null;
    }
}
