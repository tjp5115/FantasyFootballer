package fantasy.footballer.api;

import java.util.ArrayList;
import java.util.Collection;

public class Data<T> {
    private ArrayList<T> items = new ArrayList<>();

    public ArrayList<T> getItems() {
        return items;
    }

    public void addItem(T item){
        items.add(item);
    }

    public void addItems(Collection<T> items){
        this.items.addAll(items);
    }
}
