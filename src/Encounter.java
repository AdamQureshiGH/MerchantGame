import java.util.Random;
public class Encounter {
    private String title;
    private String description;
    private String option1;
    private String option2;

    public Encounter(String title, String description, String option1, String option2){
        this.title = title;
        this.description = description;
        this.option1 = option1;
        this.option2 = option2;
    }
    public void execute(Player player, GameUI ui){
        ui.clear();
        ui.printHeader("TRAVEL ENCOUNTER");
        System.out.println(description);

        String[] choices = {option1, option2};
        int choice = ui.displayMenu(choices);

        Random rand = new Random();
        ui.printLine();

        if(choice == 1){
            handleOption1(player, rand);
        } else {
            handleOption2(player, rand);
        }
        ui.waitForEnter();
    }
    private void handleOption1(Player player, Random rand)
    {
        switch(this.title){
            case "The Fat Cat":
                if(rand.nextBoolean()){
                    System.out.println("Purr... You find a gold coin behind its ear!");
                    player.getCaravan().addItem(new Item("Gold Coin", 300, 2, "GOLD"));
                }  else {
                    System.out.println("Hiss! The cat mauls you. (-30 Health)");
                    player.setHealth(player.getHealth() - 30);
                }
                break;
            case "Wandering Alchemist":
                if(player.getSilver()>=100){
                    System.out.println("You drink the glowing potion. You feel refreshed! (+30 Health, -100 Silver)");
                    player.setSilver(player.getSilver() - 100);
                    player.setHealth(Math.min(100, player.getHealth() + 30));
                } else {
                    System.out.println("You don't have enough silver! The Alchemist scoffs and vanishes.");
                }
                break;
            case "Highway Bandit Tolling":
                System.out.println("You handed over your coin safely. The bandits let your wagon pass. (-150 Silver)");
                player.setSilver(Math.max(0, player.getSilver() - 150));
                break;
            case "Old Lady":
                if(rand.nextBoolean()){
                    System.out.println("You help her and she gives you her special cookies.");
                    player.getCaravan().addItem(new FoodItem("Jar of Cookies", 20, 10, 100, "COOKIES"));
                }
                else{
                    System.out.println("She tells you to go find a proper job. (-2 health)");
                    player.setHealth(player.getHealth() - 2);
                }
                break;
            case "Travelling Bard":
                System.out.println("You think you might now have ear damage. (-10 health)");
                player.setHealth(player.getHealth() - 10);
                break;
            case "The Haunted Bridge":
                System.out.println("You pay the toll. (-25 Silver)");
                player.setSilver(Math.max(0, player.getSilver() - 25));
                break;
        }
    }
    private void handleOption2(Player player, Random rand){
        switch(this.title){
            case "The Fat Cat":
                System.out.println("You walk past carefully. The fat cat gives you a dirty look and judges you silently.");
                break;
            case "Wandering Alchemist":
                System.out.println("You shake your head and keep driving. Better safe than poisoned.");
                System.out.println("As you stroll away the old geezer shouts WIMP.");
                break;
            case "Highway Bandit Tolling":
                if(rand.nextInt(100) < 40){
                    System.out.println("Success! You whip your horses and sprint past their barricade unscathed!");
                } else {
                    System.out.println("Failure! The bandits shoot your wagon wheel and take your silver. (-500 Silver, -20 Health)");
                    player.setSilver(Math.max(0, player.getSilver() - 500));
                    player.setHealth(player.getHealth() - 20);
                }
                break;
            case "Old Lady":
                System.out.println("You go past her and suddenly feel sick. (-10 Health)");
                player.setHealth(player.getHealth() - 10);
                break;
            case "Travelling Bard":
                System.out.println("You pay him so he shuts up. (-25 Silver)");
                player.setSilver(Math.max(0, player.getSilver() - 25));
                break;
            case "The Haunted Bridge":
                int random = rand.nextInt(100);
                if(random <= 20){
                    System.out.println("You tell them about the time you tripped at your high school graduation ceremony");
                } else if(random <= 40){
                    System.out.println("You tell them about the time you faked drowning so that your crush would perform mouth to mouth\n" +
                            "but you ended up kissing some old geezer");
                } else if(random <= 60){
                    System.out.println("You tell them about the time you tried to cover a fart with a cough, but you mistimed it");
                }else if(random <= 80){
                    System.out.println("You tell them about the time you tried to do a split on the dance floor, but ended up fracturing your hip");
                }else{
                    System.out.println("You tell them about the time you accidentally stood up to receive somebody else's award because you misheard");
                }
                break;
        }
    }
}
