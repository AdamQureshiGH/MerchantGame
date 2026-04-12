public class Player {
    private Caravan caravan;
    private int silver = 100;
    private int health = 100;
    private int foodSupplies = 100;

    public Player() {
        caravan = new Caravan(1000);
    }

    public Caravan getCaravan() {
        return caravan;
    }

    public int getFoodSupplies() {
        return foodSupplies;
    }

    public void setFoodSupplies(int foodSupplies) {
        this.foodSupplies = foodSupplies;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        if(this.health > 100)
        {
            this.health = 100;
        }
    }

    public int getSilver() {
        return silver;
    }

    public void setSilver(int silver) {
        this.silver = silver;
    }
}

