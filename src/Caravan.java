import java.util.ArrayList;

public class Caravan {
    private ArrayList<Item> inventory;
    private int maxWeight;

    // pre: maxW > 0
    // post: Caravan initialized
    public Caravan(int maxW) {
        this.inventory = new ArrayList<>();
        this.maxWeight = maxW;
        addItem(new Item("Bingus", 25, 10, "He's a mean one."));

    }

    // pree : newItem is not null
    // post: Adds item if weight allows and it returns true if it was successful
    public boolean addItem(Item newItem) {
        if (getCurrentWeight() + newItem.getWeight() <= maxWeight) {
            inventory.add(newItem);
            return true;
        }
        return false;
    }

    // pre: n/a
    // post: Calculates total weight of all items
    public int getCurrentWeight() {
        int total = 0;
        for (Item i : inventory) {
            total += i.getWeight();
        }
        return total;
    }

    public int getMaxWeight() { return maxWeight; }

    public ArrayList<Item> getInventory() { return inventory; }

    public void sortInventory() {

        for(int i = 0; i < inventory.size(); i++){
            Item smallest = inventory.get(i);
            int index = i;

            for(int j = i; j < inventory.size(); j++){
                if(smallest.getName().compareTo(inventory.get(j).getName()) > 0) {
                    smallest = inventory.get(j);
                    index = j;
                }
            }
            Item temp = inventory.get(i);
            inventory.set(i, smallest);
            inventory.set(index, temp);

        }
    }
}