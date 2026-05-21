public class Player {
    private Caravan caravan;
    private int silver = 100000;
    private int health = 100;
    private int hunger = 100;

    //Player constructor which creates a caravan which acts as an inventory
    public Player(String catName) {
        caravan = new Caravan(1000, catName);
    }

    public Caravan getCaravan() {
        return caravan;
    }

    public int getHunger() {
        return hunger;
    }

    //Set hunger and cap it at 100
    public void setHunger(int hunger) {
        this.hunger = hunger;
        if (this.hunger > 100){
            this.hunger = 100;
        }
        if (this.hunger < 0) {
            this.hunger = 0;
        }
    }

    public int getHealth() {
        return health;
    }

    //set health and cap it at 100
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

