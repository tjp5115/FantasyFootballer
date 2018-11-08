package fantasy.footballer.borischen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FantasyFootballTiersTest {
    @Test
    void wide_reciever_and_ppr_url() {
        String result = new FantasyFootballTiers(LeagueType.PPR).getUrl(PlayerType.WIDE_RECEIVER);
        Assertions.assertEquals("https://s3-us-west-1.amazonaws.com/fftiers/out/text_WR-PPR.txt",result);
    }

    @Test
    void running_back_and_ppr_url() {
        String result = new FantasyFootballTiers(LeagueType.PPR).getUrl(PlayerType.RUNNING_BACK);
        Assertions.assertEquals("https://s3-us-west-1.amazonaws.com/fftiers/out/text_RB-PPR.txt",result);
    }

    @Test
    void tight_end_and_ppr_url() {
        String result = new FantasyFootballTiers(LeagueType.PPR).getUrl(PlayerType.TIGHT_END);
        Assertions.assertEquals("https://s3-us-west-1.amazonaws.com/fftiers/out/text_TE-PPR.txt",result);
    }

    @Test
    void quarter_back_and_ppr_url() {
        String result = new FantasyFootballTiers(LeagueType.PPR).getUrl(PlayerType.QUARTER_BACK);
        Assertions.assertEquals("https://s3-us-west-1.amazonaws.com/fftiers/out/text_QB.txt",result);
    }

    @Test
    void kicker_and_ppr_url() {
        String result = new FantasyFootballTiers(LeagueType.PPR).getUrl(PlayerType.KICKER);
        Assertions.assertEquals("https://s3-us-west-1.amazonaws.com/fftiers/out/text_K.txt",result);
    }

    @Test
    void defence_and_ppr_url() {
        String result = new FantasyFootballTiers(LeagueType.PPR).getUrl(PlayerType.DEFENCE);
        Assertions.assertEquals("https://s3-us-west-1.amazonaws.com/fftiers/out/text_DST.txt",result);
    }
}