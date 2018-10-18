package fantasy.footballer.espn.api.player;

import org.junit.jupiter.api.Test;

class PositionTest {
    @Test
    void no_exception_from_int_to_position_enum() {
        for( Position p : Position.values()){
            Position.fromInt(p.id);
        }
    }

}