public class MerchantGame
{

    //main menu with what the game is, how to play, select difficulty, and play,
    //difficulty modifier could affect distance and food consumption
    //events with moral decisions or bad things happening or good things happenign, or meeting wandering traders
    static City currentCity;
    static boolean isRunning = true;
    static int currentCityIndex = 0;
    static City[] worldMap = {
            new City("Venice", "Ragusa", 4, "The floating city of canals."),
            new City("Ragusa", "Budapest", 6, "A powerful stone fortress on the sea."),
            new City("Budapest", "Adrianople", 5, "The jewel of the Danube river."),
            new City("Adrianople", "Constantinople", 3, "The capital of the Ottoman Empire."),
            new City("Constantinople", "FINISHED", 0, "The crossroads between Europe and Asia.")
    };
    public static void main(String[] args) {
        GameUI ui = new GameUI();
        Player player = new Player();
        /*
        System.out.println("The year is 1422. You stand in the bustling docks of Venice.");
        System.out.println("Your caravan is ready to be loaded, and the road to the East awaits.");
        */

        ui.printHeader("Merchant Game");
        ui.waitForEnter();

        while(isRunning)
        {
            ui.clear();

            currentCity = worldMap[currentCityIndex];
            ui.printHeader(currentCity.getName());
            System.out.println(currentCity.getDescription());

            System.out.println("\n--- CURRENT STATUS ---");
            ui.printProgressBar("Health", player.getHealth(), 100);
            ui.printProgressBar("Food Supplies", player.getFoodSupplies(), 100);
            ui.printProgressBar("Wagon Space", 0, 200);
            ui.printCurrency("Wallet", player.getSilver());

            String[] menuOptions = {
                    "Visit the " + currentCity.getName() + " Merchant Guild Stand",
                    "Check Wagon Inventory",
                    "Depart for " + currentCity.getNextCityName() + " (" + currentCity.getDistance() + " days)",
                    "Retire from Trading",
            };
            int choice = ui.displayMenu(menuOptions);

            if (choice == 1) {
                System.out.println("\n[ Entering the Market... ]");
            }
            else if (choice == 2) {
                System.out.println("\n[ Opening Wagon... ]");
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
                 player.setFoodSupplies(100);
            }

            if (isRunning) {
                ui.waitForEnter();
            }
        }
        System.out.println("\n*** Thank you for playing! ***");

    }
    public static void printDeathMessage() {
        String[] messages = {
                "Scorpions crawl over your decomposing corpse. Welcome hOMe.",
                "You hit the dirt, staring at the bluish gray sky. Was it worth leaving your family in Venice to chase the wealth that moth and rust destroy and thieves break in and steal?",
                "Your knees strike the ground, and you crawl over to a tree. As your vision fades, you lean the tree thinking about your secret lover, whom you promised you would return to with new hope.",
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
                int foodNeeded = (int)(Math.random() * 11) + 10;
                if (player.getFoodSupplies() >= foodNeeded) {
                    System.out.println(foodNeeded + " units of food consumed.");
                    player.setFoodSupplies(player.getFoodSupplies() - foodNeeded);
                    player.setHealth(Math.min(100, player.getHealth() + 10));
                    System.out.println("  > Remaining: " + player.getFoodSupplies());
                } else {
                    player.setFoodSupplies(0);
                    player.setHealth(player.getHealth()-20);
                    System.out.println("  > !! STARVATION !! You are out of food.");
                    ui.printProgressBar("Health", player.getHealth(), 100);

                }

                //Random events here
                //Rumors here "You hear rumors from a small town that (Town up ahead) is xyz"

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
}


