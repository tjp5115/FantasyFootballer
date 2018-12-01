import com.google.api.client.http.HttpResponse;
import fantasy.footballer.borischen.FantasyFootballTiers;
import fantasy.footballer.borischen.LeagueType;
import fantasy.footballer.espn.api.json.player.EspnPlayerAPI;
import fantasy.footballer.espn.api.json.player.PlayerInfoJSON;
import fantasy.footballer.espn.api.json.scoreboard.ScoreBoard;
import fantasy.footballer.espn.api.league.LeagueInfo;
import fantasy.footballer.espn.api.player.PlayerInfo;
import fantasy.footballer.fanduel.player.FanDuelPlayer;
import fantasy.footballer.fanduel.player.PlayerList;
import fantasy.footballer.player.Position;
import fantasy.footballer.player.finder.EspnPlayerFinder;
import fantasy.footballer.player.finder.FanduelPlayerFinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class Main {

    //private static final int leagueID = 102116; //blaz
    //private static final int MY_TEAM_ID = 4; //blaz
    //private static final int leagueID = 155338; //burn
    //private static final int MY_TEAM_ID = 15; //burn


    public static void main(String args[]) throws IOException {
        makeMyESPNTeamBetter("BLAZ", 102116, 4);
        makeMyESPNTeamBetter("BURN", 155338, 15);
        //makeMyESPNTeamBetter("DAD", 1002004, 11);

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


        System.out.println("Find For Salary");
        fanduelPlayerFinder.findFlexPlayer(6800).forEach(player -> {if ( player != null ) System.out.println(player.getPosition().getName() + " : " +player );});
    }

    public static void makeMyESPNTeamBetter(String teamName, int leagueID, int teamID) throws IOException{
        System.out.println("Lineup for " + teamName);
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
        //espnPlayerFinder.findAllPossibleTrades().forEach(System.out::println);
        System.out.println("Lineup:");
        espnPlayerFinder.rankPlayersForTeam().forEach((k,v) -> System.out.println(k.getName() +" : " + v.toString()));

        System.out.println();
    }

}
