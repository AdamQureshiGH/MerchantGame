import java.util.ArrayList;

public class MerchantGame
{

    //main menu with what the game is, how to play, select difficulty, and play,
    //difficulty modifier could affect distance and food consumption
    //events with moral decisions or bad things happening or good things happenign, or meeting wandering traders
    //alchemist offers you potions, spend a day searching for chicken 50% for big reward
    static City currentCity;
    static boolean isRunning = true;
    static int currentCityIndex = 0;
    private static final ArrayList<Item> masterPool = initializeMasterPool();

    static City[] worldMap = {
            new City("Venice", "Ragusa", 4, "The floating city of canals.", generateRandomMarket()),
            new City("Ragusa", "Budapest", 6, "A powerful stone fortress on the sea.", generateRandomMarket()),
            new City("Budapest", "Adrianople", 5, "The jewel of the Danube river.", generateRandomMarket()),
            new City("Adrianople", "Constantinople", 3, "The capital of the Ottoman Empire.",generateRandomMarket()),
            new City("Constantinople", "FINISHED", 0, "The crossroads between Europe and Asia.", generateRandomMarket())
    };

    public static void main(String[] args) {
        GameUI ui = new GameUI();
        Player player = new Player();

        ui.printHeader("Merchant Game");
        System.out.println("The year is 1422. You stand in the bustling docks of Venice.");
        System.out.println("Your caravan is ready to be loaded, and the road to the East awaits.");

        ui.waitForEnter();

        mainLoop(player, ui);

        System.out.println("\n*** Thank you for playing! ***");
    }
    public static void mainLoop(Player player, GameUI ui){
        while(isRunning)
        {
            ui.clear();

            currentCity = worldMap[currentCityIndex];
            ui.printHeader(currentCity.getName());
            System.out.println(currentCity.getDescription());

            System.out.println("\n--- CURRENT STATUS ---");
            ui.printProgressBar("Health", player.getHealth(), 100);
            ui.printProgressBar("Hunger", player.getHunger(), 100);
            ui.printProgressBar("Wagon Space", player.getCaravan().getCurrentWeight(), player.getCaravan().getMaxWeight());
            ui.printCurrency("Wallet", player.getSilver());

            String[] menuOptions = {
                    "Visit the " + currentCity.getName() + " Merchant Guild Stand",
                    "Check Wagon Inventory",
                    "Depart for " + currentCity.getNextCityName() + " (" + currentCity.getDistance() + " days)",
                    "Retire from Trading",
            };
            int choice = ui.displayMenu(menuOptions);

            if (choice == 1) {
                handleMarket(player, ui);
            }
            else if (choice == 2) {
                checkInventory(player, ui);
            }
            else if (choice == 3) {
                handleTravel(player, ui);
            }
            else if (choice == 4) {
                System.out.println("Final Silver: " + player.getSilver());
                System.out.println("You quit your life as a travelling merchant and settled down in " + currentCity.getName() +
                        ".\n You are happy working at your small vendor's table, but you wonder if you could have been something more.");
                // ADD GRADING SYSTEM LATER BASED ON FINAL WEALTH
                isRunning = false;
            }
            else if(choice == 415)
            {
                System.out.println("Cheater Cheater Pumpkin Eater");
                player.setSilver(1000);
                player.setHealth(100);
                player.setHunger(100);
            }

            if (isRunning) {
                ui.waitForEnter();
            }
        }
    }
    public static void printDeathMessage() {
        String[] messages = {
                "Scorpions crawl over your decomposing corpse. Welcome hOMe.",
                "You hit the dirt, staring at the bluish gray sky. Was it worth leaving your family in Venice to chase the wealth that moth and rust destroy and thieves break in and steal?",
                "Your knees strike the ground, and you crawl over to a tree. As your vision fades, you lean the tree thinking about your family, who you promised you would return to with new hope.",
                "Get pwned.",
                "Your wagon stands abandoned in the mud and your horse walks to a local bar",
                "You fall over next to a running stream. You can't help but smile thinking about the journey and the struggle of building yourself up, and you die with no regrets.",
                "You poop yourself and die a shameful death.",
                "Your eyes feel heavy and your field of view closes in. You see vultures circling in the sky above you. I'm not dead yet!!"
        };

        int randomIndex = (int)(Math.random() * messages.length);
        System.out.println(messages[randomIndex]);
        System.out.println("\n****************************************");
        System.out.println("          GAME OVER");
        System.out.println("****************************************");

    }
    public static void handleMarket(Player player, GameUI ui){
        boolean shopping = true;
        Market market = currentCity.getMarket();

        while(shopping)
        {
            ui.clear();
            ui.printHeader(currentCity.getName() + " Merchant Guild Stand");
            player.getCaravan().sortInventory();
            System.out.println("Wallet: " + player.getSilver() + " Silver");
            System.out.println("Wagon: " + player.getCaravan().getCurrentWeight() + "/" + player.getCaravan().getMaxWeight() + " lbs\n");

            ArrayList<Item> stock = market.getShelf();
            String[] options = new String[stock.size() + 1];

            for(int i =0; i < stock.size(); i++){
                Item item = stock.get(i);
                options[i] = String.format("%-15s | %d Silver | %d lbs", item.getName(), item.getPrice(), item.getWeight());
            }
            options[stock.size()] = "Leave Market";
            int choice = ui.displayMenu(options);
            if(choice == options.length){
                shopping = false;
            }
            else{
                Item selected = stock.get(choice - 1);

                System.out.print("How many " + selected.getName() + " would you like to buy? ");
                int quantity = ui.readInt();

                if (quantity <= 0) {
                    System.out.println("Invalid amount.");
                } else {
                    int totalCost = selected.getPrice() * quantity;
                    int totalWeight = selected.getWeight() * quantity;

                    if (player.getSilver() < totalCost) {
                        System.out.println("\n[!] Not enough Silver for that many");
                    } else if (player.getCaravan().getCurrentWeight() + totalWeight > player.getCaravan().getMaxWeight()) {
                        System.out.println("\n[!] Your wagon cant hold that much weight!");
                    } else {
                        for (int i = 0; i < quantity; i++) {
                            Item newItem;
                            if (selected instanceof FoodItem) {
                                FoodItem food = (FoodItem)selected;
                                newItem = new FoodItem(food.getName(), food.getPrice(), food.getWeight(), food.getNutrition());
                            } else {
                                newItem = new Item(selected.getName(), selected.getPrice(), selected.getWeight());
                            }
                            player.getCaravan().addItem(newItem);                        }
                        player.setSilver(player.getSilver() - totalCost);
                        System.out.println("\n[+] Loaded " + quantity + "x " + selected.getName() + " into the wagon.");
                    }
                }
                ui.waitForEnter();
            }
        }
    }
    public static void checkInventory(Player player, GameUI ui){
        ui.clear();
        ui.printHeader("Wagon Inventory");
        player.getCaravan().sortInventory();
        ArrayList<Item> items = player.getCaravan().getInventory();

        if(items.isEmpty()){
            ui.printMessage("Your wagon is empty and rattling. A sorry sight...");
        }
        else{
            for(int i = 0; i < items.size(); i++)
            {
                Item currentItem = items.get(i);
                int count = 1;

                while(i+1 < items.size() && items.get(i).getName().equals(items.get(i+1).getName()))
                {
                    count++;
                    i++;
                }
                int totalWeight = currentItem.getWeight() * count;
                if(count > 1){
                    System.out.println(String.format(" > %d x %-15s (%d lbs total)", count, currentItem.getName(), totalWeight));
                }
                else{
                    System.out.println(String.format(" > 1 x %-15s (%d lbs)", currentItem.getName(), totalWeight));
                }
            }
        }

        ui.printProgressBar("Weight Capacity", player.getCaravan().getCurrentWeight(), player.getCaravan().getMaxWeight());
    }
    public static void handleTravel(Player player, GameUI ui)
    {
        ui.clear();
        if (currentCity.getNextCityName().equals("FINISHED")) {
            System.out.println("You reached the end of the road!");
            //Grading system here
            isRunning = false;
        } else {
            int travelDays = currentCity.getDistance();
            System.out.println("You begin the " + currentCity.getDistance() + " day journey...");

            for(int d = 1; d<= travelDays; d++)
            {
                System.out.println("Day " + d + " on the road.");
                //Random events here

                int energyBurned = (int)(Math.random() * 11) + 10;

                player.setHunger(player.getHunger() - energyBurned);
                ui.printMessage((-(player.getHunger() - energyBurned)) + " energy burned");
                ui.printProgressBar("Hunger", player.getHunger(), 100);

                System.out.println("Do you want to stop and eat from your supplies? (1: Yes / 2: No)");
                int eatChoice = ui.readInt();
                if (eatChoice == 1) {
                    checkAndEatWagonFood(player, ui);
                }

                if (player.getHunger() <=0) {
                    player.setHunger(0);
                    System.out.println("  > !! STARVATION !!");
                    player.setHealth(player.getHealth()-20);
                    ui.printProgressBar("Health", player.getHealth(), 100);
                } else {
                    player.setHealth(Math.min(100, player.getHealth() + 10));
                    if(player.getHealth() < 100){
                        ui.printProgressBar("Health", player.getHealth(), 100);
                    }
                }


                if (player.getHealth() <= 0) {
                    ui.clear();
                    System.out.println("The journey was too much for you.");
                    printDeathMessage();
                    isRunning = false;
                    break;
                }
                ui.waitForEnter();
            }
            if (isRunning) {
                currentCityIndex++;
                System.out.println("\nWelcome to " + worldMap[currentCityIndex].getName() + "!");
            }

        }
    }
    public static void checkAndEatWagonFood(Player player, GameUI ui){
        ArrayList<Item> inv = player.getCaravan().getInventory();
        boolean stillEating = true;

        while(stillEating){
            ArrayList<FoodItem> foodInWagon = new ArrayList<>();
            for(Item i: inv){
                if(i instanceof FoodItem){
                    foodInWagon.add((FoodItem) i);
                }
            }

            if(foodInWagon.isEmpty()){
                ui.printMessage("You search your wagon but find no food. Your belly grumbles.");
                stillEating = false;
                break;
            }

            String[] options = new String[foodInWagon.size() + 1];
            for (int i = 0; i < foodInWagon.size(); i++) {
                FoodItem f = foodInWagon.get(i);
                options[i] = String.format("Eat %-15s (+%d Hunger)", f.getName(), f.getNutrition());
            }
            options[foodInWagon.size()] = "Finish";

            ui.clear();
            ui.printHeader("Choose a Meal!");
            ui.printProgressBar("Hunger", player.getHunger(), 100);
            int choice = ui.displayMenu(options);
            {
                if(choice <= foodInWagon.size()){
                    FoodItem selectedFood = foodInWagon.get(choice - 1);
                    player.setHunger(player.getHunger() + selectedFood.getNutrition());
                    System.out.println("\n[+] You ate the " + selectedFood.getName() + ".");
                    inv.remove(selectedFood);

                    if (player.getHunger() >= 100) {
                        System.out.println("You are completely full!");
                        stillEating = false;
                    } else {
                        System.out.println("Your belly thanks you");
                        ui.waitForEnter();
                    }
                }
                else {
                    ui.printMessage("\nYou tighten your belt and decide to save the food for market.");
                    stillEating = false;
                    ui.waitForEnter();
                }
            }
        }

    }
    public static Market generateRandomMarket() {
        Market newMarket = new Market();
        ArrayList<Item> foodOptions = new ArrayList<>();
        ArrayList<Item> normalOptions = new ArrayList<>();

        for (Item i : masterPool) {
            if (i instanceof FoodItem) foodOptions.add(i);
            else normalOptions.add(i);
        }
        newMarket.addItemToStock(foodOptions.get((int)(Math.random() * foodOptions.size())));
        int itemsToTarget = (int)(Math.random() * 5) + 5;

        while(newMarket.getShelf().size() < itemsToTarget){
            Item randomItem = normalOptions.get((int)(Math.random() * normalOptions.size()));

            boolean duplicate = false;
            for(Item shelfItem : newMarket.getShelf()){
                if(shelfItem.getName().equals(randomItem.getName())){
                    duplicate=true;
                    break;
                }
            }
            if(duplicate==false){
                newMarket.addItemToStock(randomItem);
            }

        }
        return newMarket;
    }
    private static ArrayList<Item> initializeMasterPool() {
    ArrayList<Item> pool = new ArrayList<>();
    //food items
    pool.add(new FoodItem("Hard Tack", 5, 2, 10));
    pool.add(new FoodItem("Salted Pork", 15, 10, 25));
    pool.add(new FoodItem("Dried Figs", 8, 3, 15));
    pool.add(new FoodItem("Smoked Herring", 12, 5, 20));
    pool.add(new FoodItem("Wheel of Cheese", 20, 15, 30));
    pool.add(new FoodItem("Pickled Beets", 7, 4, 12));
    pool.add(new FoodItem("Rye Bread", 4, 3, 8));
    pool.add(new FoodItem("Salted Cod", 14, 8, 22));
    pool.add(new FoodItem("Dried Venison", 25, 6, 35));
    pool.add(new FoodItem("Honey Pot", 18, 5, 15));
    pool.add(new FoodItem("Cured Ham", 30, 12, 40));
    pool.add(new FoodItem("Barley Sack", 10, 20, 15));
    pool.add(new FoodItem("Olive Oil", 22, 10, 18));
    pool.add(new FoodItem("Walnuts", 12, 5, 10));
    pool.add(new FoodItem("Dried Dates", 15, 4, 20));
    pool.add(new FoodItem("Smoked Eel", 28, 7, 30));
    pool.add(new FoodItem("Lard Bucket", 6, 15, 25));
    pool.add(new FoodItem("Dried Apricots", 16, 3, 15));
    pool.add(new FoodItem("Hard Sausage", 20, 6, 28));
    pool.add(new FoodItem("Preserved Plums", 10, 5, 14));
    // luxury goods
    pool.add(new Item("Silk Bolt", 120, 8));
    pool.add(new Item("Frankincense", 150, 2));
    pool.add(new Item("Myrrh", 140, 2));
    pool.add(new Item("Velvet Fabric", 90, 10));
    pool.add(new Item("Fine Perfume", 200, 1));
    pool.add(new Item("Ivory Carving", 350, 5));
    pool.add(new Item("Gold Filigree", 500, 2));
    pool.add(new Item("Tapestry", 250, 40));
    pool.add(new Item("Jeweled Comb", 180, 1));
    pool.add(new Item("Incense Burner", 70, 6));
    pool.add(new Item("Stained Glass", 160, 25));
    pool.add(new Item("Embroidered Cape", 220, 5));
    pool.add(new Item("Silver Mirror", 300, 12));
    pool.add(new Item("Peacock Feathers", 45, 1));
    pool.add(new Item("Rosewater", 55, 3));
    pool.add(new Item("Lacquer Box", 130, 4));
    pool.add(new Item("Ebony Statue", 400, 30));
    pool.add(new Item("Satin Ribbon", 35, 1));
    pool.add(new Item("Crystal Carafe", 210, 7));
    pool.add(new Item("Porcelain Vase", 280, 10));
    // spices and medicine
    pool.add(new Item("Saffron", 450, 1));
    pool.add(new Item("Black Pepper", 60, 2));
    pool.add(new Item("Cinnamon Stick", 50, 2));
    pool.add(new Item("Cloves", 80, 2));
    pool.add(new Item("Nutmeg", 85, 2));
    pool.add(new Item("Ginger Root", 40, 3));
    pool.add(new Item("Cardamom", 95, 2));
    pool.add(new Item("Turmeric Powder", 30, 3));
    pool.add(new Item("Star Anise", 75, 1));
    pool.add(new Item("Aloe Resin", 55, 2));
    pool.add(new Item("Mace Spice", 110, 1));
    pool.add(new Item("Coriander Seed", 25, 4));
    pool.add(new Item("Cumin", 28, 4));
    pool.add(new Item("Licorice Root", 32, 5));
    pool.add(new Item("Camphor", 120, 2));
    pool.add(new Item("Mustard Seed", 15, 10));
    pool.add(new Item("Dried Mint", 10, 2));
    pool.add(new Item("Poppy Seeds", 45, 2));
    pool.add(new Item("Rhubarb Root", 65, 6));
    pool.add(new Item("Sarsaparilla", 50, 5));
    // materials
    pool.add(new Item("Iron Ingot", 25, 50));
    pool.add(new Item("Copper Plate", 35, 40));
    pool.add(new Item("Tin Bar", 30, 35));
    pool.add(new Item("Lead Block", 15, 80));
    pool.add(new Item("Beeswax Block", 20, 15));
    pool.add(new Item("Raw Wool", 12, 30));
    pool.add(new Item("Flax Fiber", 18, 20));
    pool.add(new Item("Unhide Leather", 22, 25));
    pool.add(new Item("Oak Timber", 40, 100));
    pool.add(new Item("Pine Tar", 15, 30));
    pool.add(new Item("Charcoal Sack", 8, 40));
    pool.add(new Item("Limestone", 5, 90));
    pool.add(new Item("Raw Amber", 140, 3));
    pool.add(new Item("Obsidian", 90, 15));
    pool.add(new Item("Quicksilver", 260, 10));
    pool.add(new Item("Alum Salt", 45, 20));
    pool.add(new Item("Potash", 30, 25));
    pool.add(new Item("Sulfur", 55, 10));
    pool.add(new Item("Tallow", 10, 20));
    pool.add(new Item("Hemp Rope", 20, 25));
    // textiles
    pool.add(new Item("Linen Roll", 40, 15));
    pool.add(new Item("Cotton Bale", 50, 40));
    pool.add(new Item("Woolen Blanket", 25, 10));
    pool.add(new Item("Fur Pelts", 80, 15));
    pool.add(new Item("Feather Duster", 15, 2));
    pool.add(new Item("Parchment", 60, 1));
    pool.add(new Item("Ink Well", 45, 2));
    pool.add(new Item("Quill Pen", 10, 1));
    pool.add(new Item("Bronze Pot", 55, 15));
    pool.add(new Item("Iron Horseshoe", 12, 5));
    pool.add(new Item("Leather Saddle", 110, 25));
    pool.add(new Item("Whetstone", 20, 5));
    pool.add(new Item("Glass Beads", 35, 2));
    pool.add(new Item("Clay Bricks", 5, 100));
    pool.add(new Item("Tallow Candles", 15, 8));
    pool.add(new Item("Wicker Basket", 8, 4));
    pool.add(new Item("Iron Nails", 18, 10));
    pool.add(new Item("Wooden Clogs", 12, 4));
    pool.add(new Item("Hemp Sack", 5, 2));
    pool.add(new Item("Copper Kettle", 65, 12));

    return pool;
}

}


;