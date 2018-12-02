package fantasy.footballer.fanduel;

import fantasy.footballer.api.Data;
import fantasy.footballer.api.Root;
import fantasy.footballer.borischen.FantasyFootballTiers;
import fantasy.footballer.borischen.LeagueType;
import fantasy.footballer.fanduel.player.FanDuelPlayer;
import fantasy.footballer.fanduel.player.PlayerList;
import fantasy.footballer.player.Position;
import fantasy.footballer.player.finder.FanduelPlayerFinder;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class FanduelController {
    Logger logger = Logger.getLogger(FanduelController.class);

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/footballer/fanduel/lineup")
    public Root fanduelTierRanking(@RequestParam("file") MultipartFile file,
                                    @RequestParam(defaultValue = "9") Integer numberOfTiers,
                                   @RequestParam(defaultValue = "3") Integer numberOfPlayersPerTier) {
        List<FanDuelPlayer> players = PlayerList.getPlayerListFromFile(file);
        FanduelPlayerFinder fanduelPlayerFinder = new FanduelPlayerFinder(players,new FantasyFootballTiers(LeagueType.HALF));

        Data<FanDuelPlayer> fanDuelPlayerData = new Data<>();
        for (int i = 1; i < numberOfTiers; ++i) {
            System.out.println("Tier " + i);
            for( Map.Entry<Position,List<FanDuelPlayer>> entry : fanduelPlayerFinder.findCheapestPlayersForTier(i,numberOfPlayersPerTier).entrySet()) {
                if(!Position.KICKER.equals(entry.getKey()) || ! entry.getValue().isEmpty()) {
                    fanDuelPlayerData.addItems(entry.getValue());
                }
            }
        }
        return new Root(fanDuelPlayerData);
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/footballer/fanduel/salary")
    public Root fanduelSalary(@RequestParam("file") MultipartFile file,
                              @RequestParam() Integer targetSalary) throws IOException {
        List<FanDuelPlayer> players = PlayerList.getPlayerListFromFile(file);
        FanduelPlayerFinder fanduelPlayerFinder = new FanduelPlayerFinder(players,new FantasyFootballTiers(LeagueType.HALF));
        Data<FanDuelPlayer> fanDuelPlayerData = new Data<>();
        for(FanDuelPlayer player : fanduelPlayerFinder.findFlexPlayer(targetSalary)){
            if( player != null ) {
                fanDuelPlayerData.addItem(player);
            }else{
                logger.error("Null Player. Did not fine player map for fanduel and tier.");
            }
        }
        return new Root(fanDuelPlayerData);
    }
}
