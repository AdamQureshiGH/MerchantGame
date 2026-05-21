import java.util.ArrayList;
public class Market {
    private ArrayList<Item> shelf;

    public Market(ArrayList<Item> shelf){
        this.shelf = shelf;
    }

    public Market() {
        shelf = new ArrayList<Item>();
    }
    //Add a specific Item to the market list
    public void addItemToStock(Item item){
        shelf.add(item);
    }
    //Return the market list of items
    public ArrayList<Item> getShelf(){
        return shelf;
    }
}
