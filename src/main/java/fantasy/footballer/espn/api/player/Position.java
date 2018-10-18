package fantasy.footballer.espn.api.player;

public enum Position {
    WIDE_REICEVER(3),
    RUNNING_BACK(2),
    TIGHT_END(4),
    QUARTER_BACK(1),
    KICKER(5),
    DEFENCE(16);

    int id;

    Position(int id){
        this.id = id;
    }

    public static Position fromInt(int i){
        switch( i ){
            case 1: return QUARTER_BACK;
            case 2: return RUNNING_BACK;
            case 3: return WIDE_REICEVER;
            case 4: return TIGHT_END;
            case 5: return KICKER;
            case 16: return DEFENCE;
            default: throw new IllegalArgumentException("'"+Integer.toString(i)+"' is not a valid position");
        }
    }
}
