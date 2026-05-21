import java.util.Random;
public class Encounter {
    private String title;
    private String description;
    private String option1;
    private String option2;
    //initializes an encounter for its title, description, and two possible options
    public Encounter(String title, String description, String option1, String option2){
        this.title = title;
        this.description = description;
        this.option1 = option1;
        this.option2 = option2;
    }
    //allows the player to pick one of the two options
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
    //Executes the result of the player picking option 1
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
            case "The Dynamic Duo":
                System.out.println("The knight takes the silver, bows majestically, and trips over his own shoes. You pass safely. (-75 Silver)");
                player.setSilver(Math.max(0, player.getSilver() - 75));
                break;

            case "The Aggressive Goose":
                System.out.println("The battle is fierce. You win, but at what cost? (-15 Health)");
                player.setHealth(player.getHealth() - 15);
                player.getCaravan().addItem(new FoodItem("Goose", 20, 10, 50, "GOOSE"));
                break;
            case "Suspicious Free Soup":
                if (rand.nextBoolean()) {
                    System.out.println("Delicious! It restores your energy entirely. (+90 Hunger, +30 Health)");
                    player.setHunger(Math.min(100, player.getHunger() + 90));
                    player.setHealth(Math.min(100, player.getHealth() + 30));
                } else {
                    System.out.println("Severe food poisoning. You spend the night vomiting behind a bush. (-30 Hunger, -30 Health)");
                    player.setHunger(Math.max(0, player.getHunger() - 30));
                    player.setHealth(player.getHealth() - 30);
                }
                break;
            case "The Time Traveling Peasant":
                System.out.println("He panics, screams about a butterfly effect, throws a weird metal brick at you, and flees.");
                System.out.println("(You pocket the 'Dead Smartphone' to sell later.)");
                player.getCaravan().addItem(new Item("Dead Smartphone", 10, 5, "SMARTPHONE"));
                break;
            case "The Runaway Cheese Wheel":
                if (rand.nextInt(100) < 40) {
                    System.out.println("You catch it perfectly! Incredible. Heavy, but delicious. (+1 Luxury Cheese)");
                    player.getCaravan().addItem(new FoodItem("Luxury Cheese Wheel", 100, 30, 80, "CHEESE WHEEL"));
                } else {
                    System.out.println("It smashes right into your ribs and shatters. (-30 Health)");
                    player.setHealth(player.getHealth() - 30);
                }
                break;
            case "The Quicksand Scam":
                System.out.println("It's an ambush! Three bandits jump out of a nearby bush while you are distracted pulling him out! (-100 Silver, -10 Health)");
                player.setSilver(Math.max(0, player.getSilver() - 100));
                player.setHealth(player.getHealth() - 10);
                break;
            case "The Horse Strike":
                if (player.getSilver() >= 40) {
                    System.out.println("You pay a passing traveler 40 Silver to hand them an expensive sugar treat. They happily stand back up and pull with renewed energy. (-40 Silver)");
                    player.setSilver(player.getSilver() - 40);
                } else {
                    System.out.println("You don't even have 40 silver to pay the passerby! Your horses glare at your poverty and continue sitting.");
                }
                break;
        }
    }
    //Executes the result of player picking option 2
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
            case "Suspicious Free Soup":
                System.out.println("You stay hungry but safe. The peasants look mildly insulted.");
                break;

            case "The Time-Traveling Peasant":
                System.out.println("You drive past. You hear him muttering about buying stock in something called 'Apple'.");
                break;

            case "The Runaway Cheese Wheel":
                System.out.println("The cheese barrels past and explodes against a boulder. The air smells vaguely of dairy.");
                break;

            case "The Quicksand Scam":
                System.out.println("The man stops screaming, sighs, unties the rope himself, and sits down on a rock, looking disappointed in his failed career choice.");
                break;

            case "The Horse Strike":
                System.out.println("One horse turns around and bites your arm. You waste a day waiting for them to care again. (-10 Health, -10 Hunger)");
                player.setHealth(player.getHealth() - 10);
                player.setHunger(Math.max(0, player.getHunger() - 10));
                break;
            case "The Dynamic Duo":
                if (rand.nextInt(100) < 60) {
                    System.out.println("You tell the squire his armor looks blindingly radiant. He is so touched he convinces the knight to waive your fee and gives you a snack. (+15 Hunger)");
                    player.setHunger(Math.min(100, player.getHunger() + 15));
                } else {
                    System.out.println("The squire sees through your lies. The knight fines you double for emotional manipulation. (-150 Silver)");
                    player.setSilver(Math.max(0, player.getSilver() - 150));
                }
                break;

            case "The Aggressive Goose":
                System.out.println("You toss it some grain from your pockets. The goose accepts the tribute and allows your caravan to cross. (-15 Hunger)");
                player.setHunger(Math.max(0, player.getHunger() - 15));
                break;

        }
    }
}
