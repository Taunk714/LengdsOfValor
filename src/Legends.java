import java.util.*;
import java.util.regex.Pattern;

//  Legends extends Game. It's a singleton.
//  I make it a singleton because it's really helpful to implement
//  show map and inrfomation at any moment. And since it's not a cyber
//  game, it will only have one Legend class.
public class Legends implements RpgGame{

    private static int heroNameLen = 22;
    private static int monsterNameLen = 15;

    private static final String[] occupationList = {"Warrior", "Sorcerer", "Paladin"};

    private HeroCreator heroCreator = HeroCreator.getInstance();

    private LegendsGrid legendsGrid;
    private Team<Hero> heroes;
    private HashSet<Integer> heroIds = new HashSet<>();
    private int isDead = 0;

    private static Legends instance = new Legends();
    private Legends(){
        printInitWords();
        legendsGrid = new LegendsGrid();
        heroes = new Team<>(pickHeroNumber(), 0, 0);
        for (int i = 0; i < heroes.getTeamSize(); i++) {
            System.out.print("\n");
            System.out.printf("Choose Hero %d\n",i);
            Hero target = initHero(chooseOccupation(i));
            heroes.setMember(i, target);
            System.out.printf("%s joins the team!\n", target.toString());
        }
    }

    public static Legends getInstance(){
        return instance;
    }

    public void start() throws InterruptedException {
        legendsGrid.enter(heroes, new int[]{0,0});
        while (true){
            printMap();

            while (true){
                int[] pos = heroes.getPos().clone();
                String next = askMovement();

                switch (next) {
                    case "w"->{
                        pos[0]--;
                    }
                    case "a"-> {
                        pos[1]--;
                    }
                    case "s"-> {
                        pos[0]++;
                    }
                    case "d"-> {
                        pos[1]++;
                    }
                    case "q"-> {
                        printEndGame();
                        return;
                    }
                    case "i"-> {
                        showInfo();
                        continue;
                    }
                }
                try {
                    legendsGrid.enter(heroes, pos);
                    break;
                }catch (ArrayIndexOutOfBoundsException e){
                    System.out.println(MyFont.ANSI_RED + "Sorry you can't go there, it is out of the playing area."+MyFont.ANSI_RESET);
                }
            }


            if (heroes.isDead()){
                printFailure();
                if (askRevive()){
                    heroes.revive();
                }else {
                    break;
                }
            }
        }

        printEndGame();

    }

    // Decide the number of team member.
    private int pickHeroNumber(){
        System.out.println("Please choose the total number of heroes(Default is 1, Maximum is 5):");
        int heroNum = 1;
        try {
            Scanner scanner = new Scanner(System.in);
            heroNum = scanner.nextInt();
        }catch (InputMismatchException e){
            System.out.println("Incorrect format! So use the default value: 1");
        }
        return heroNum;
    }

    // initializing the hero.
    private Hero initHero(int occId){
//        String[][] target = null;
        String occName = occupationList[occId];

        heroCreator.showOccInfo(occName);

        System.out.println("Please choose your partner and enter the corresponding number:");
        int index;
        while (true){
            try{
                Scanner scanner = new Scanner(System.in);
                index = scanner.nextInt();
                if (heroIds.contains(occId * 10 + index)){
                    System.out.println("This hero has already been chosen. Please choose another hero.");
                }else if (index >= 0 && index < heroCreator.getHeroNum(occName)){
                    heroIds.add(occId * 10 + index);
                    heroCreator.markUsed(occId * 10 + index);
                    break;
                }else{
                    System.out.printf("Input out of bounds. Please enter 0 to %d.\n", heroCreator.getHeroNum(occName)-1);
                }
            }catch (InputMismatchException e){
                System.out.printf("Incorrect format! Please enter 0 to %d", heroCreator.getHeroNum(occName)-1);
            }
        }

        return heroCreator.createHero(occName, index);
    }

    // decide the next movement. WSAD
    private String askMovement(){
        System.out.println("Decide your next movement!");
        System.out.println("""
                W/w: move up
                A/a: move left
                S/s: move down
                D/d: move right
                Q/q: quit game
                I/i: show information.
                """);
        while(true){
            try {

                Scanner scanner = new Scanner(System.in);
                return scanner.next(Pattern.compile("[wasdqiWASDQI]")).toLowerCase();
            } catch (NoSuchElementException e) {
                System.out.println("Sorry, you can only enter w, s, a, d, q or i:");
            }
        }
    }

    // choose the occupation, return occupation id
    // 0: "Warrior",
    // 1: "Sorcerer",
    // 2: "Paladin"
    private static int chooseOccupation(int id){
        System.out.println("Hero " + id +", you have 3 choices of occupation.");
        printOccupationInfo();
        System.out.println("Choose your occupation: ");
        String occupation;
        int index;
        while (true){
            try{
                Scanner scanner = new Scanner(System.in);
                index = scanner.nextInt();
                occupation = occupationList[index-1];
                break;
            }catch (InputMismatchException e){
                System.out.println("Incorrect format! Please enter a number:");
            }catch (IndexOutOfBoundsException e){
                System.out.println("The number is out of bounds. Please enter 1 to 3:");
            }
        }
        System.out.println("Hero "+ id + " chose to be an " + occupation + "!");
        return index-1;
    }

    private static void printOccupationInfo(){
        System.out.println("1: Warrior. Warriors are favored on strength and agility.");
        System.out.println("2: Sorcerer. Sorcerers are favored on the dexterity and the agility.");
        System.out.println("3: Paladins. Paladins are favored on strength and dexterity.");
    }

    // print game rule.
    public void printInitWords(){
        System.out.println(MyFont.ANSI_BOLD +MyFont.ANSI_BLUE+ """
                _|        _|_|_|_|    _|_|_|  _|_|_|_|  _|      _|  _|_|_|      _|_|_| \s
                _|        _|        _|        _|        _|_|    _|  _|    _|  _|       \s
                _|        _|_|_|    _|  _|_|  _|_|_|    _|  _|  _|  _|    _|    _|_|   \s
                _|        _|        _|    _|  _|        _|    _|_|  _|    _|        _| \s
                _|_|_|_|  _|_|_|_|    _|_|_|  _|_|_|_|  _|      _|  _|_|_|    _|_|_|\s""" + MyFont.ANSI_RESET);
        System.out.println("""
                Welcome to the Legends!
                You are in the Legends world. There are \u001B[1m3\u001B[0m types of area in the legends world,
                common area, inaccessible area and market area. You \u001B[0mcan't go to inaccessible area. 
                You \u001B[1mcan\u001B[0m go to market area to buy or sell items. If you go to common area, there is a change 
                that you will meet a group of monster. \u001B[1m\u001B[31mBeat them and Level up!\u001B[0m
                
                In the battle, every round, alive hero can choose to do a regular attack, cast a spell, use a potion, 
                change your armor or weapon once. And their hp and mana will recover 10% each round.
                \u001B[3m\u001B[37mNotice1: Heroes can't do the last three actions unless they have spell, potion, armor or weapon 
                in their bag. If they don't have weapon/armor in their bag but have worn the armor or had weapon 
                in the hand, they still can't change their armor/weapon. Obviously, it's unwise to remove them)
                                
                Notice2: You can't cancel your action during battle, but you can check hero information and their bags at any moment.
                So please check the info before you make decision if you don't know you really want to change weapon/armor.\u001B[0m
                                
                Your team will be born in a market in the \u001B[1m\u001B[3m\u001B[36mupper-left\u001B[0m of the map...
                
                You can enter m,i or q to show map, show info or quit the game at any time.
                Now! Choose your game settings and begin your adventure!
                
                """);
    }

    // show info when enter i/I.
    public void showInfo(){
        //        more specifically their hp, their level, their mana, their current exp, their money
//        and their skill levels
        StringBuilder s = new StringBuilder();
        StringBuilder sLine = new StringBuilder();
        int[] tableSize = new int[]{heroNameLen, 3, 5, 6, 6, 7, 10, 10, 10};
        s.append("|%") ;
        sLine.append("+");
        for (int i = 0; i < tableSize.length; i++) {
            s.append(tableSize[i]).append("s|%");
            sLine.append("-".repeat(tableSize[i])).append("+");
        }
        s.deleteCharAt(s.length()-1);
        s.append("\n");
        String it = "INFORMATION";
        System.out.println(MyFont.ANSI_BACKGROUNDWHITE+" ".repeat((sLine.length()-it.length())/2)+
                it+ " ".repeat(sLine.length()-it.length() - (sLine.length()-it.length())/2) + MyFont.ANSI_RESET);
        System.out.println(sLine.toString());
        System.out.printf(s.toString(), "Hero Name","Lv","HP","Mana", "EXP","Money", "Strength", "Agility", "Dexterity");
        System.out.println(sLine);
        for (int i = 0; i < heroes.getTeamSize(); i++) {
            Hero target = heroes.getMember(i);
            System.out.printf(s.toString(), target.getName(), target.getLevel(),
                    target.getHP(), target.getMana(),target.getExp(), target.getMoney(),
                    target.getStrengthVal(),target.getAgilityVal(),target.getDexterityVal());
        }
        System.out.println(sLine.toString());
    }

    public void printMap(){
        StringBuilder s = new StringBuilder();
        int size = legendsGrid.getSize();
        int[] heroPos = heroes.getPos();
        for (int i = 0; i < size; i++) {
            s.append("+---".repeat(size));
            s.append("+");
            s.append("\n");
            for (int j = 0; j < size; j++) {
                if (heroPos[0] == i && heroPos[1] == j){
                    s.append("|").append(heroes.toString());
                }else{
                    s.append("|").append(legendsGrid.getCell(i, j).toString());
                }
            }
            s.append("|\n");
        }
        s.append("+---".repeat(Math.max(0, size)));
        s.append("+");
        System.out.println(s);
    }

    // Print message when heroes lose.
    private void printFailure() throws InterruptedException {
        MyFont.showDEAD();
        Thread.sleep(1000);
        for (int i = 0; i < heroes.getTeamSize(); i++) {
            heroes.getMember(i).printHeroDeadInfo();
        }
        System.out.println("All the heroes died.......");
        Thread.sleep(500);
        System.out.println(MyFont.ANSI_ITALIC + "WHO CAN SAVE THE WORLD.....");
        Thread.sleep(500);
        System.out.println("WHERE IS THE AVALON............" + MyFont.ANSI_RESET);
        System.out.print("\n");
        Thread.sleep(2000);
    }

    // When heroes lose the game, they have change to revive and restart the adventure.
    private boolean askRevive() throws InterruptedException {
        for (int i = 0; i < 20; i++) {
            Thread.sleep(600);
            System.out.print(".");
        }
        Thread.sleep(60);
        System.out.println("?");
        Thread.sleep(800);
        System.out.println("Still brave? ");
        Thread.sleep(1500);
        System.out.println("Still hopeful? ");
        Thread.sleep(1500);
        System.out.println("Still want to fight? ");
        Thread.sleep(1500);
        System.out.println("Still want to save the world? ");
        System.out.println(MyFont.ANSI_ITALIC + "Enter y/Y if you want to survive, press other keys to give up.");
        System.out.println("(Notice: Heroes' level won't change, but the current exp will be 0.)");
        Scanner scanner = new Scanner(System.in);
        String ans = scanner.nextLine();
        return "y".equalsIgnoreCase(ans);
    }



    public void printEndGame(){
        System.out.println("Adventure ends.");
    }

    public static int getHeroNameLen(){
        return heroNameLen;
    }

    public static int getMonsterNameLen(){
        return monsterNameLen;
    }
}
