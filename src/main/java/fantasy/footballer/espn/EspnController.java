package fantasy.footballer.espn;

import com.google.api.client.http.HttpResponse;
import fantasy.footballer.api.Data;
import fantasy.footballer.api.Root;
import fantasy.footballer.borischen.FantasyFootballTiers;
import fantasy.footballer.borischen.LeagueType;
import fantasy.footballer.espn.api.json.player.EspnPlayerAPI;
import fantasy.footballer.espn.api.json.player.PlayerInfoJSON;
import fantasy.footballer.espn.api.json.scoreboard.KonaPlayerInfo;
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
              @RequestParam() Integer leagueID,
              @RequestParam(required = false, defaultValue = "PPR") String leagueType) throws IOException {
        HttpResponse leagueInfo = new LeagueInfo()
            .forLeague(leagueID)
            .usePlayerView()
            .sendRequest();

        KonaPlayerInfo konaPlayerInfo = leagueInfo.parseAs(KonaPlayerInfo.class);
        EspnPlayerFinder espnPlayerFinder = new EspnPlayerFinder(new FantasyFootballTiers(LeagueType.valueOf(leagueType)));
        espnPlayerFinder.addEspnPlayers(konaPlayerInfo.players);
        espnPlayerFinder.setTeamId(teamID);
        List<PlayerTrade> playerTrades = espnPlayerFinder.findAllPossibleTrades();
        Data<PlayerTrade> playerTradeData = new Data<>();
        playerTradeData.addItems(playerTrades);
        return new Root(playerTradeData);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/footballer/espn/lineup")
    public Root espnLineup(@RequestParam() Integer teamID,
                           @RequestParam() Integer leagueID,
                           @RequestParam(required = false, defaultValue = "PPR") String leagueType) throws IOException {

        HttpResponse leagueInfo = new LeagueInfo()
            .forLeague(leagueID)
            .usePlayerView()
            .sendRequest();

        KonaPlayerInfo konaPlayerInfo = leagueInfo.parseAs(KonaPlayerInfo.class);

        //get all the players that are in the league.
        List<EspnPlayerAPI> leaguePlayerIds = konaPlayerInfo.players.stream()
                .filter(player -> player.teamId.equals(teamID))
                .collect(Collectors.toList());

        EspnPlayerFinder espnPlayerFinder = new EspnPlayerFinder(new FantasyFootballTiers(LeagueType.valueOf(leagueType)));
        espnPlayerFinder.addEspnPlayers(leaguePlayerIds);
        espnPlayerFinder.setTeamId(teamID);

        Data<Player> playerData = new Data<>();
        espnPlayerFinder.rankPlayersForTeam().forEach((position,players) -> playerData.addItems(players));

        return new Root(playerData);
    }

}
