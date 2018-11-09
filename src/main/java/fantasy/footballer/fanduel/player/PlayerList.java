package fantasy.footballer.fanduel.player;

import fantasy.footballer.borischen.Position;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerList {
    public static List<FanDuelPlayer> getPlayerListFromFile(String fileName){
        Iterable<CSVRecord> records;
        try {
            Reader in = new FileReader(fileName);
            records = CSVFormat.DEFAULT.withHeader().parse(in);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        List<FanDuelPlayer> playerList = new ArrayList<>();
        for(CSVRecord record : records){
            Position position = Position.fromFanDuel(record.get("Position"));
            FanDuelPlayer player = new FanDuelPlayer(position);
            player.setFirstName(record.get("First Name"));
            player.setLastName(record.get("Last Name"));
            player.setSalary(record.get("Salary"));
            playerList.add(player);
        }
        return playerList;
    }
}
