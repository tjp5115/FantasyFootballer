package fantasy.footballer.espn.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class EspnAPITest {
    EspnAPI espnAPI;

    @Test
    public void forLeague(){
        espnAPI = new EspnAPI();
        espnAPI.forLeague(12345);
        Assertions.assertTrue(espnAPI.getRequest().values().contains(12345));

    }

    @Test
    public void forYear(){
        espnAPI = new EspnAPI();
        espnAPI.forYear("2018");
        Assertions.assertTrue(espnAPI.getRequest().values().contains("2018"));
    }

    @Test
    public void forWeek(){
        espnAPI = new EspnAPI();
        Assertions.assertTrue(espnAPI.getRequest().values().contains("10"));
    }
}