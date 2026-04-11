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
            ui.printProgressBar("Health", 100, 100);
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
                // travel logic
                if (currentCity.getNextCityName().equals("END")) {
                    System.out.println("You have reached the end of the road!");
                    isRunning = false;
                } else {
                    System.out.println("You begin the " + currentCity.getDistance() + " day journey...");
                    currentCityIndex++;
                }
            }
            else if (choice == 4) {
                System.out.println("Final Silver: " + silver);
                System.out.println("You quit your life as a travelling merchant and settled down in " + currentCity.getName() +
                        ".\n You are happy working at your small vendor's table, but you wonder if you could have been something more.");
                // ADD GRADING SYSTEM LATER BASED ON FINAL WEALTH
                isRunning = false;
            }

            if (isRunning) {
                ui.waitForEnter();
            }
        }
        System.out.println("\n*** Thank you for playing! ***");

    }
}


