public class MerchantGame
{
    public static void main(String[] args) {
        GameUI ui = new GameUI();
        City[] worldMap = {
                new City("Venice", "Ragusa", 4, "The floating city of canals."),
                new City("Ragusa", "Budapest", 6, "A powerful stone fortress on the sea."),
                new City("Budapest", "Adrianople", 5, "The jewel of the Danube river."),
                new City("Adrianople", "Constantinople", 3, "The capital of the Ottoman Empire."),
                new City("Constantinople", "FINISHED", 0, "The crossroads between Europe and Asia.")
        };
        int currentCityIndex = 0;
        int silver = 100;
        int health = 100;
        int foodSupplies = 100;
        boolean isRunning = true;

        /*
        System.out.println("The year is 1422. You stand in the bustling docks of Venice.");
        System.out.println("Your caravan is ready to be loaded, and the road to the East awaits.");
        */

        ui.printHeader("Merchant Game");
        ui.waitForEnter();

        while(isRunning)
        {
            for(int i = 0; i < 30; i++)
            {
                System.out.println();
            }

            City currentCity = worldMap[currentCityIndex];
            ui.printHeader(currentCity.getName());
            System.out.println(currentCity.getDescription());

            System.out.println("\n--- CURRENT STATUS ---");
            ui.printProgressBar("Health", health    , 100);
            ui.printProgressBar("Food Supplies", foodSupplies, 100);
            ui.printProgressBar("Wagon Space", 0, 200);
            ui.printCurrency("Wallet", silver);

            String[] menuOptions = {
                    "Visit the " + currentCity.getName() + " Merchant Guild Stand",
                    "Check Wagon Inventory",
                    "Depart for " + currentCity.getNextCityName() + " (" + currentCity.getDistance() + " days)",
                    "Retire from Trading"
            };
            int choice = ui.displayMenu(menuOptions);

            if (choice == 1) {
                System.out.println("\n[ Entering the Market... ]");
            }
            else if (choice == 2) {
                System.out.println("\n[ Opening Wagon... ]");
            }
            else if (choice == 3) {
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
                        if (foodSupplies >= foodNeeded) {
                            System.out.println(foodNeeded + " units of food consumed.");
                            foodSupplies -= foodNeeded;
                            System.out.println("  > Remaining: " + foodSupplies);
                        } else {
                            foodSupplies = 0;
                            health -= 20;
                            System.out.println("  > !! STARVATION !! You are out of food.");
                            ui.printProgressBar("Health", health    , 100);

                        }

                        //Random events here
                        //Rumors here "You hear rumors from a small town that (Town up ahead) is xyz"

                        if (health <= 0) {
                            System.out.println("\n****************************************");
                            System.out.println("The journey was too much for you.");
                            System.out.println("****************************************");
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
            else if (choice == 4) {
                System.out.println("Final Silver: " + silver);
                System.out.println("You quit your life as a travelling merchant and settled down in " + currentCity.getName() +
                        ".\n You are happy working at your small vendor's table, but you wonder if you could have been something more.");
                // ADD GRADING SYSTEM LATER BASED ON FINAL WEALTH
                isRunning = false;
            }
            else if(choice == 415)
            {
                System.out.println("Cheater Cheater Pumpkin Eater");
                 silver = 1000;
                 health = 100;
                 foodSupplies = 100;
            }

            if (isRunning) {
                ui.waitForEnter();
            }
        }
        System.out.println("\n*** Thank you for playing! ***");

    }
    public static void printDeathMessage() {
        String[] messages = {
                "The silk road has claimed another soul. You died of exhaustion.",
                "Your journey ends here. The elements were harsher than your resolve.",
                "A final sunset over the dunes. You have perished on the road.",
                "Hunger and fatigue have won. Your wagon stands abandoned in the mud."
        };

        int randomIndex = (int)(Math.random() * messages.length);
        System.out.println("\n****************************************");
        System.out.println("          GAME OVER");
        System.out.println("****************************************");

    }
}


