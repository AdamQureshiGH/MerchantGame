import java.util.ArrayList;
public class Market {
    private ArrayList<Item> shelf;

    public Market(ArrayList<Item> shelf){
        this.shelf = shelf;
    }

    public Market() {
        shelf = new ArrayList<Item>();
    }

    public void addItemToStock(Item item){
        shelf.add(item);
    }
    public ArrayList<Item> getShelf(){
        return shelf;
    }
    public Item buyItem(int index){
        return shelf.remove(index);
    }
}
