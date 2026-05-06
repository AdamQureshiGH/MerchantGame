public class Item {
    private String name;
    private int price;
    private int weight;
    private String rumorDescription;

    public Item(String n, int p, int w, String rumor) {
        this.name = n;
        this.price = p;
        this.weight = w;
        this.rumorDescription = rumor;
    }

    public String getRumorDescription() { return rumorDescription; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getWeight() { return weight; }
    public void setPrice(int newPrice) {
        this.price = newPrice;
    }


    public String toString() {
        return String.format("%-15s | Weight: %d lbs", name, weight);
    }
}