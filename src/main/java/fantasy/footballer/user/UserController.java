package fantasy.footballer.user;

import fantasy.footballer.api.Data;
import fantasy.footballer.api.Root;
import fantasy.footballer.espn.EspnLeague;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class UserController {

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/user/leagues")
    public Root leagues() throws IOException {

        Data<EspnLeague> data = new Data<>();
//      <Trade leagueId="155338" teamId="15" leagueName="BURN"/>
  //    <Trade leagueId="1002004" teamId="11" leagueName="DAD"/>
            data.addItem(new EspnLeague(102116,4,"BLAZ"));

        Root root = new Root(data);
        return root;
    }
}
