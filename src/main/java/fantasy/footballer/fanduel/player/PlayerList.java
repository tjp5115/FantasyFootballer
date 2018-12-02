package fantasy.footballer.fanduel.player;

import fantasy.footballer.player.Position;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerList {
    public static List<FanDuelPlayer> getPlayerListFromFile(Reader reader){
        Iterable<CSVRecord> records;
        try {
            records = CSVFormat.DEFAULT.withHeader().parse(reader);
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

    public static List<FanDuelPlayer> getPlayerListFromFile(MultipartFile multipart){
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+multipart.getOriginalFilename());
        try {
            multipart.transferTo(convFile);
            Reader in = new FileReader(convFile);
            return getPlayerListFromFile(in);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<FanDuelPlayer> getPlayerListFromFile(String fileName){
        try {
            return getPlayerListFromFile(new FileReader(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
