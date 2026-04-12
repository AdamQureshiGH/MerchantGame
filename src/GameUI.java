import java.util.Scanner;

public class GameUI {
    private Scanner scanner;

    public GameUI() {
        scanner = new Scanner(System.in);
    }
    public void clear(){
        for(int i = 0; i < 30; i++)
        {
            System.out.println();
        }
    }
    // pre: title is not null
    // post:  prints a nice header
    public void printHeader(String title) {
        System.out.println("\n****************************************************");
        System.out.println("    --- " + title.toUpperCase() + " ---");
        System.out.println("****************************************************");
    }

    // pre: label is not null andd current and max are > 0
    // post: prints a bar for Health or Wagon Capacity
    public void printProgressBar(String label, int current, int max) {
        System.out.print(String.format("%-12s [", label));
        int dots = (int) ((double) current / max * 20);
        for (int i = 0; i < 20; i++) {
            if (i < dots) System.out.print("=");
            else System.out.print(".");
        }
        System.out.println("] " + current + "/" + max);
    }

    // pre:  options string are not null
    // post: prints a menu with numbers and returns the user's choice
    public int displayMenu(String[] options) {
        System.out.println("\nPick an Option");
        for (int i = 0; i < options.length; i++) {
            System.out.println("  " + (i + 1) + ") " + options[i]);
        }

        System.out.print("\nSelection: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Enter a number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }
    //pre: label and amount are not null
    //post: prints currency with a label in a nice format
    public void printCurrency(String label, int amount) {
        System.out.printf("%-15s : %d Silver %n", label, amount);
    }
    //pre: msg is not null
    //post: Easy to read line of text
    public void printMessage(String msg) {
        System.out.println("  > " + msg);
    }

// self explanatory
    public void waitForEnter() {
        System.out.println("\n[ Press ENTER to continue... ]");
        scanner.nextLine();
        scanner.nextLine();
    }
}