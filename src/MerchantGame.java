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
        rumorSystem();

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
            if (!currentCity.getRumor().isEmpty()) {
               System.out.println("\n[Merchant Whispers..] " + currentCity.getRumor());
            }

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
        String[] marketOptions = {"Buy Items", "Sell Items", "Leave Market"};
        int mChoice = ui.displayMenu(marketOptions);
        if (mChoice == 1){
            handleBuying(player, ui);
        }
        else if (mChoice == 2){
            handleSelling(player, ui);
        }
        ui.clear();

    }
    public static void handleBuying(Player player, GameUI ui){
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
                                newItem = new FoodItem(food.getName(), food.getPrice(), food.getWeight(), food.getNutrition(), food.getRumorDescription());
                            } else {
                                newItem = new Item(selected.getName(), selected.getPrice(), selected.getWeight(), selected.getRumorDescription());
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
    public static void handleSelling(Player player, GameUI ui){
        boolean selling = true;
        while (selling) {
            ui.clear();
            ui.printHeader("Sell Goods - " + currentCity.getName());
            player.getCaravan().sortInventory();
            ArrayList<Item> inv = player.getCaravan().getInventory();

            if (inv.isEmpty()) {
                System.out.println("Your wagon is empty.");
                ui.waitForEnter();
                return;
            }

            ArrayList<Item> uniqueOnes = new ArrayList<>();
            ArrayList<Integer> counts = new ArrayList<>();

            for (Item item : inv) {
                boolean found = false;
                for (int i = 0; i < uniqueOnes.size(); i++) {
                    if (uniqueOnes.get(i).getName().equals(item.getName())) {
                        counts.set(i, counts.get(i) + 1);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    uniqueOnes.add(item);
                    counts.add(1);
                }
            }

            String[] options = new String[uniqueOnes.size() + 1];
            for (int i = 0; i < uniqueOnes.size(); i++) {
                Item item = uniqueOnes.get(i);
                int sellPrice = item.getPrice();

                if (item.getName().equals(currentCity.getBoomingItemName())) {
                    sellPrice = (int)(sellPrice * currentCity.getPriceMultiplier());
                    options[i] = String.format("%d x %-15s | [HOT] %d Silver each", counts.get(i), item.getName(), sellPrice);
                } else {
                    sellPrice = (int)(sellPrice * 0.9);
                    options[i] = String.format("%d x %-15s | %d Silver each", counts.get(i), item.getName(), sellPrice);
                }
            }
            options[uniqueOnes.size()] = "Back to Market Menu";

            int choice = ui.displayMenu(options);
            if (choice > uniqueOnes.size()) {
                selling = false;
            } else {
                Item selected = uniqueOnes.get(choice - 1);
                System.out.print("How many " + selected.getName() + " to sell? ");
                int qty = ui.readInt();

                if (qty > 0 && qty <= counts.get(choice - 1)) {
                    int unitPrice = selected.getName().equals(currentCity.getBoomingItemName()) ?
                            (int)(selected.getPrice() * currentCity.getPriceMultiplier()) :
                            (int)(selected.getPrice() * 0.8);

                    int totalGain = unitPrice * qty;
                    player.setSilver(player.getSilver() + totalGain);

                    int removed = 0;
                    for (int i = inv.size() - 1; i >= 0 && removed < qty; i--) {
                        if (inv.get(i).getName().equals(selected.getName())) {
                            inv.remove(i);
                            removed++;
                        }
                    }
                    System.out.println("Sold " + qty + " for " + totalGain + " Silver!");
                    ui.waitForEnter();
                }
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
                System.out.println("\nDay " + d + " on the road.");
                //Random events here

                int energyBurned = (int)(Math.random() * 11) + 10;

                player.setHunger(player.getHunger() - energyBurned);
                ui.printMessage((energyBurned) + " energy burned");
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
                    ui.printMessage("You tighten your belt and decide to save the food for market.");
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
    public static void rumorSystem() {
        for (int i = 0; i < worldMap.length - 2; i++) {
            City current = worldMap[i];
            City nextCity = worldMap[i + 1];
            City targetCity = worldMap[i + 2];

            ArrayList<Item> nextCityStock = nextCity.getMarket().getShelf();

            if (!nextCityStock.isEmpty()) {
                Item luckyItem = nextCityStock.get((int)(Math.random() * nextCityStock.size()));

                targetCity.setBoomingItem(luckyItem.getName(), 2.2);

                String rumor = changeTextToCity(luckyItem, targetCity.getName());
                current.setRumor(rumor);
            }
        }
    }
    private static String changeTextToCity(Item item, String targetCityName) {
        return item.getRumorDescription().replace("[CITY]", targetCityName);
    }
    private static ArrayList<Item> initializeMasterPool() {
    ArrayList<Item> pool = new ArrayList<>();
        //food
        pool.add(new FoodItem("Hard Tack", 5, 2, 10, "A naval fleet is docking in [CITY]; they're buying up every bit of Hard Tack for the voyage."));
        pool.add(new FoodItem("Salted Pork", 15, 10, 25, "The garrisons in [CITY] are complaining about lean rations. They're desperate for Salted Pork."));
        pool.add(new FoodItem("Dried Figs", 8, 3, 15, "A wealthy merchant in [CITY] is hosting a desert-themed gala and needs Dried Figs."));
        pool.add(new FoodItem("Smoked Herring", 12, 5, 20, "An inland famine near [CITY] has made preserved fish like Smoked Herring worth its weight in silver."));
        pool.add(new FoodItem("Wheel of Cheese", 20, 15, 30, "The monks near [CITY] had a bad harvest; they are paying top coin for a good Wheel of Cheese."));
        pool.add(new FoodItem("Pickled Beets", 7, 4, 12, "A bizarre health craze in [CITY] has everyone looking for Pickled Beets."));
        pool.add(new FoodItem("Rye Bread", 4, 3, 8, "The local bakeries in [CITY] have run out of flour; even simple Rye Bread is selling fast."));
        pool.add(new FoodItem("Salted Cod", 14, 8, 22, "Lent is approaching in [CITY], and the demand for Salted Cod is skyrocketing."));
        pool.add(new FoodItem("Dried Venison", 25, 6, 35, "The hunting grounds around [CITY] are frozen over; Dried Venison is a rare luxury now."));
        pool.add(new FoodItem("Honey Pot", 18, 5, 15, "The royal meadery in [CITY] needs every Honey Pot they can find for the spring festival."));
        pool.add(new FoodItem("Cured Ham", 30, 12, 40, "The Governor of [CITY] is throwing a feast; only the finest Cured Ham will do."));
        pool.add(new FoodItem("Barley Sack", 10, 20, 15, "The breweries in [CITY] are empty; they need Barley Sacks to restart production."));
        pool.add(new FoodItem("Olive Oil", 22, 10, 18, "A shortage of fuel in [CITY] has people using Olive Oil for their lamps."));
        pool.add(new FoodItem("Walnuts", 12, 5, 10, "Bakers in [CITY] are making traditional nut cakes and are short on Walnuts."));
        pool.add(new FoodItem("Dried Dates", 15, 4, 20, "Caravans from the south haven't reached [CITY], making Dried Dates very valuable."));
        pool.add(new FoodItem("Smoked Eel", 28, 7, 30, "A visiting diplomat in [CITY] has a peculiar craving for Smoked Eel."));
        pool.add(new FoodItem("Lard Bucket", 6, 15, 25, "The cold snap in [CITY] has everyone buying Lard Buckets for heavy winter cooking."));
        pool.add(new FoodItem("Dried Apricots", 16, 3, 15, "The apothecaries in [CITY] are using Dried Apricots in their new tonics."));
        pool.add(new FoodItem("Hard Sausage", 20, 6, 28, "Soldiers marching through [CITY] need Hard Sausage for their long trek."));
        pool.add(new FoodItem("Preserved Plums", 10, 5, 14, "The confectioners in [CITY] are paying high prices for Preserved Plums this month."));

        // luxury goods
        pool.add(new Item("Silk Bolt", 120, 8, "The wedding of a high noble in [CITY] has created a massive demand for every Silk Bolt available."));
        pool.add(new Item("Frankincense", 150, 2, "The grand cathedral in [CITY] is preparing for a holy week and needs Frankincense."));
        pool.add(new Item("Myrrh", 140, 2, "Physicians in [CITY] are using Myrrh for a new medicinal ointment."));
        pool.add(new Item("Velvet Fabric", 90, 10, "Tailors in [CITY] are rushing to finish new winter robes; they need Velvet Fabric."));
        pool.add(new Item("Fine Perfume", 200, 1, "The elite socialites in [CITY] are trying to out-smell each other with Fine Perfume."));
        pool.add(new Item("Ivory Carving", 350, 5, "A famous collector in [CITY] is paying a premium for any Ivory Carving brought to town."));
        pool.add(new Item("Gold Filigree", 500, 2, "The palace in [CITY] is being renovated; Gold Filigree is in high demand."));
        pool.add(new Item("Tapestry", 250, 40, "The castle walls in [CITY] are drafty; the Lord is buying every Tapestry to keep them warm."));
        pool.add(new Item("Jeweled Comb", 180, 1, "A trend at the court in [CITY] has made the Jeweled Comb the must-have accessory."));
        pool.add(new Item("Incense Burner", 70, 6, "The local temples in [CITY] are replacing their old rituals with new ones requiring an Incense Burner."));
        pool.add(new Item("Stained Glass", 160, 25, "A new chapel is being built in [CITY], and they've run out of Stained Glass."));
        pool.add(new Item("Embroidered Cape", 220, 5, "The city guard in [CITY] is being given ceremonial uniforms requiring an Embroidered Cape."));
        pool.add(new Item("Silver Mirror", 300, 12, "A theater troupe in [CITY] needs a Silver Mirror for their latest stage production."));
        pool.add(new Item("Peacock Feathers", 45, 1, "The milliners in [CITY] are making eccentric hats with Peacock Feathers this season."));
        pool.add(new Item("Rosewater", 55, 3, "The public baths in [CITY] are trying to attract more customers with scented Rosewater."));
        pool.add(new Item("Lacquer Box", 130, 4, "Merchants in [CITY] are looking for Lacquer Boxes to hold their precious gems."));
        pool.add(new Item("Ebony Statue", 400, 30, "The university in [CITY] is building a new library and wants an Ebony Statue for the hall."));
        pool.add(new Item("Satin Ribbon", 35, 1, "A festival in [CITY] requires miles of Satin Ribbon for decoration."));
        pool.add(new Item("Crystal Carafe", 210, 7, "The wine merchants in [CITY] want to present their vintage in a Crystal Carafe."));
        pool.add(new Item("Porcelain Vase", 280, 10, "A trade envoy from the east is arriving in [CITY], and they want a Porcelain Vase to impress him."));

        // spices and medicine
        pool.add(new Item("Saffron", 450, 1, "The Guild of Chefs in [CITY] is desperate for Saffron for the Sultan's jubilee."));
        pool.add(new Item("Black Pepper", 60, 2, "A spice merchant in [CITY] lost his shipment at sea; Black Pepper prices are peaking."));
        pool.add(new Item("Cinnamon Stick", 50, 2, "The winter air in [CITY] has everyone brewing spiced cider with Cinnamon Sticks."));
        pool.add(new Item("Cloves", 80, 2, "An outbreak of toothaches in [CITY] has made Cloves a medicinal necessity."));
        pool.add(new Item("Nutmeg", 85, 2, "A new law in [CITY] requires Nutmeg for certain medicinal distillations."));
        pool.add(new Item("Ginger Root", 40, 3, "Sea travelers arriving in [CITY] are buying Ginger Root to cure their nausea."));
        pool.add(new Item("Cardamom", 95, 2, "The coffee houses in [CITY] have run out of Cardamom for their signature brews."));
        pool.add(new Item("Turmeric Powder", 30, 3, "Dyers in [CITY] are looking for Turmeric Powder to color their spring silk."));
        pool.add(new Item("Star Anise", 75, 1, "A local bakery in [CITY] has a secret recipe that requires every Star Anise in town."));
        pool.add(new Item("Aloe Resin", 55, 2, "The sun-scorched workers in [CITY] are begging for Aloe Resin to treat their burns."));
        pool.add(new Item("Mace Spice", 110, 1, "The elite banquet halls in [CITY] are competing for the finest Mace Spice."));
        pool.add(new Item("Coriander Seed", 25, 4, "Farmers near [CITY] are looking to plant Coriander Seeds after a failed harvest."));
        pool.add(new Item("Cumin", 28, 4, "A popular street food in [CITY] is causing a sudden shortage of Cumin."));
        pool.add(new Item("Licorice Root", 32, 5, "Confectioners in [CITY] are trying to make a new tonic using Licorice Root."));
        pool.add(new Item("Camphor", 120, 2, "The libraries in [CITY] are using Camphor to protect their ancient scrolls from moths."));
        pool.add(new Item("Mustard Seed", 15, 10, "A condiment manufacturer in [CITY] is buying up Mustard Seeds by the wagon-load."));
        pool.add(new Item("Dried Mint", 10, 2, "The herbalists in [CITY] are making a relaxing tea and need more Dried Mint."));
        pool.add(new Item("Poppy Seeds", 45, 2, "Bread makers in [CITY] have a surplus of flour but a total lack of Poppy Seeds."));
        pool.add(new Item("Rhubarb Root", 65, 6, "A new medical text in [CITY] claims Rhubarb Root can cure the common cold."));
        pool.add(new Item("Sarsaparilla", 50, 5, "Soft drink vendors in [CITY] are paying high coin for Sarsaparilla extract."));

        // materials
        pool.add(new Item("Iron Ingot", 25, 50, "The armory in [CITY] is being restocked; they need every Iron Ingot they can get."));
        pool.add(new Item("Copper Plate", 35, 40, "Coppersmiths in [CITY] are busy making new roofing for the palace domes."));
        pool.add(new Item("Tin Bar", 30, 35, "The bronze foundries in [CITY] are stalled because they've run out of Tin Bars."));
        pool.add(new Item("Lead Block", 15, 80, "A plumbing project in [CITY] is requiring a massive amount of Lead Blocks."));
        pool.add(new Item("Beeswax Block", 20, 15, "The candle makers in [CITY] are working overtime and need Beeswax Blocks."));
        pool.add(new Item("Raw Wool", 12, 30, "The spinning wheels in [CITY] are idle; the weavers are desperate for Raw Wool."));
        pool.add(new Item("Flax Fiber", 18, 20, "Linen production in [CITY] has increased, driving up the price of Flax Fiber."));
        pool.add(new Item("Unhide Leather", 22, 25, "The bootmakers in [CITY] have a contract for the army and need Unhide Leather."));
        pool.add(new Item("Oak Timber", 40, 100, "Shipbuilders in [CITY] are looking for high-quality Oak Timber for their hulls."));
        pool.add(new Item("Pine Tar", 15, 30, "The docks in [CITY] are sealing their ships and are paying well for Pine Tar."));
        pool.add(new Item("Charcoal Sack", 8, 40, "The glassblowers in [CITY] are burning through Charcoal Sacks at an alarming rate."));
        pool.add(new Item("Limestone", 5, 90, "A new cathedral foundation in [CITY] is being laid with tons of Limestone."));
        pool.add(new Item("Raw Amber", 140, 3, "Jewelers in [CITY] have discovered a way to polish Raw Amber into high-priced art."));
        pool.add(new Item("Obsidian", 90, 15, "The local scholars in [CITY] are studying volcanic glass and need Obsidian samples."));
        pool.add(new Item("Quicksilver", 260, 10, "Alchemists in [CITY] are performing secret experiments that require Quicksilver."));
        pool.add(new Item("Alum Salt", 45, 20, "The tanneries in [CITY] are paying a premium for Alum Salt to process their hides."));
        pool.add(new Item("Potash", 30, 25, "Soap makers in [CITY] are struggling to keep up with demand and need Potash."));
        pool.add(new Item("Sulfur", 55, 10, "The pyrotechnicians in [CITY] are preparing for a festival and need Sulfur."));
        pool.add(new Item("Tallow", 10, 20, "The leather workers in [CITY] are using Tallow to soften their heavy-duty saddles."));
        pool.add(new Item("Hemp Rope", 20, 25, "The shipping cranes in [CITY] are being refitted with new Hemp Rope."));

        // textiles and hardware
        pool.add(new Item("Linen Roll", 40, 15, "The hospital in [CITY] is running low on bandages; they are buying every Linen Roll in sight."));
        pool.add(new Item("Cotton Bale", 50, 40, "A new weaving mill has opened in [CITY] and needs a massive supply of Cotton Bales."));
        pool.add(new Item("Woolen Blanket", 25, 10, "A sudden cold front has the citizens of [CITY] scrambling for a warm Woolen Blanket."));
        pool.add(new Item("Fur Pelts", 80, 15, "The mountain hunters near [CITY] moved away, making Fur Pelts a high-priced rarity."));
        pool.add(new Item("Feather Duster", 15, 2, "The museum in [CITY] is being cleaned for a royal visit; they need more Feather Dusters."));
        pool.add(new Item("Parchment", 60, 1, "The legal courts in [CITY] are overwhelmed with paperwork and have run out of Parchment."));
        pool.add(new Item("Ink Well", 45, 2, "A famous poet has taken residency in [CITY], causing a local shortage of the finest Ink Wells."));
        pool.add(new Item("Quill Pen", 10, 1, "The scribes at the university in [CITY] are complaining about a lack of decent Quill Pens."));
        pool.add(new Item("Bronze Pot", 55, 15, "The communal kitchens in [CITY] are being upgraded; they are paying well for a sturdy Bronze Pot."));
        pool.add(new Item("Iron Horseshoe", 12, 5, "The cavalry stationed at [CITY] is preparing for a long patrol and needs an Iron Horseshoe for every mount."));
        pool.add(new Item("Leather Saddle", 110, 25, "The royal stables in [CITY] are being refurbished; they are looking for a master-crafted Leather Saddle."));
        pool.add(new Item("Whetstone", 20, 5, "The city guard in [CITY] has been ordered to sharpen their blades, making the Whetstone a hot commodity."));
        pool.add(new Item("Glass Beads", 35, 2, "A new jewelry trend in [CITY] has created a sudden market boom for colorful Glass Beads."));
        pool.add(new Item("Clay Bricks", 5, 100, "The city walls of [CITY] are being repaired after a storm; they need thousands of Clay Bricks."));
        pool.add(new Item("Tallow Candles", 15, 8, "The winter solstice festival in [CITY] has everyone buying up Tallow Candles for the vigil."));
        pool.add(new Item("Wicker Basket", 8, 4, "The harvest season in [CITY] is better than expected; farmers are desperate for a Wicker Basket."));
        pool.add(new Item("Iron Nails", 18, 10, "Construction on the docks in [CITY] has stalled due to a severe lack of Iron Nails."));
        pool.add(new Item("Wooden Clogs", 12, 4, "The muddy season in [CITY] has made the humble Wooden Clogs the most popular footwear in town."));
        pool.add(new Item("Hemp Sack", 5, 2, "The grain silos in [CITY] are overflowing; merchants are paying double for a simple Hemp Sack."));
        pool.add(new Item("Copper Kettle", 65, 12, "The tea houses in [CITY] are expanding and need a new Copper Kettle for every table."));
    return pool;
}

}


