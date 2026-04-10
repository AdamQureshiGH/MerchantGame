public class MerchantGame
{
    public static void main(String[] args) {
        GameUI ui = new GameUI();

        ui.printHeader("Merchant Game");
        System.out.println("The year is 1422. You stand in the bustling docks of Venice.");
        System.out.println("Your caravan is ready to be loaded, and the road to the East awaits.");

        System.out.println("\n--- CURRENT STATUS ---");
        ui.printProgressBar("Health", 100, 100);
        ui.printProgressBar("Wagon Space", 0, 200);
        ui.printCurrency("Wallet", 100);

        String[] menuOptions = {
                "Visit the Guild Factor (Market)",
                "Check Wagon Inventory",
                "Depart for Ragusa",
                "Retire from Trading (Quit)"
        };

        int choice = ui.displayMenu(menuOptions);

        if (choice == 1) {
            ui.printHeader("Venice Guild Stand");
            ui.printMessage("The Factor looks at you expectantly...");
        } else if (choice == 4) {
            ui.printMessage("You settle down in Venice and live a quiet life.");
        }

        ui.waitForEnter();
    }
}


