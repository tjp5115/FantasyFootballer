package fantasy.footballer.api;

import com.google.api.client.util.Key;

public class Root {
    @Key
    private Data data = new Data();

    public Data getData() {
        return data;
    }

    public Root(Data data) {
        this.data = data;
    }

}
