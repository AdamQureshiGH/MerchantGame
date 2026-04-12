public class Item {
    private String name;
    private int price;
    private int weight;

    // pre: n is not null, p and w
    // post: Item is initialized
    public Item(String n, int p, int w) {
        this.name = n;
        this.price = p;
        this.weight = w;
    }

    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getWeight() { return weight; }
    public void setPrice(int newPrice) {
        this.price = newPrice;
    }


    public String toString() {
        return String.format("%-15s | Price: %d | Weight (lbs): %.1f", name, price, weight);
    }
}