package fantasy.footballer.borischen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FantasyFootballTiersTest {
    @Test
    void wide_reciever_and_ppr_url() {
        String result = new FantasyFootballTiers().getUrl(PlayerType.WIDE_RECEIVER,LeagueType.PPR);
        Assertions.assertEquals("https://s3-us-west-1.amazonaws.com/fftiers/out/text_WR-PPR.txt",result);
    }

    @Test
    void running_back_and_ppr_url() {
        String result = new FantasyFootballTiers().getUrl(PlayerType.RUNNING_BACK,LeagueType.PPR);
        Assertions.assertEquals("https://s3-us-west-1.amazonaws.com/fftiers/out/text_RB-PPR.txt",result);
    }

    @Test
    void tight_end_and_ppr_url() {
        String result = new FantasyFootballTiers().getUrl(PlayerType.TIGHT_END,LeagueType.PPR);
        Assertions.assertEquals("https://s3-us-west-1.amazonaws.com/fftiers/out/text_TE-PPR.txt",result);
    }

    @Test
    void quarter_back_and_ppr_url() {
        String result = new FantasyFootballTiers().getUrl(PlayerType.QUARTER_BACK,LeagueType.PPR);
        Assertions.assertEquals("https://s3-us-west-1.amazonaws.com/fftiers/out/text_QB.txt",result);
    }

    @Test
    void kicker_and_ppr_url() {
        String result = new FantasyFootballTiers().getUrl(PlayerType.KICKER,LeagueType.PPR);
        Assertions.assertEquals("https://s3-us-west-1.amazonaws.com/fftiers/out/text_K.txt",result);
    }

    @Test
    void defence_and_ppr_url() {
        String result = new FantasyFootballTiers().getUrl(PlayerType.DEFENCE,LeagueType.PPR);
        Assertions.assertEquals("https://s3-us-west-1.amazonaws.com/fftiers/out/text_DST.txt",result);
    }
}