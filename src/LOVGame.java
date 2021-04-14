import java.util.*;
import java.util.regex.Pattern;

public class LOVGame implements RpgGame {

    private HeroCreator heroCreator = HeroCreator.getInstance();


    private static final String[] occupationList = {"Warrior", "Sorcerer", "Paladin"};
    private static final String[] laneList = {"Top", "Mid", "Bot"};

    private Grid<Character> lovGrid;
    private LOVHeroTeam heroes;
    private LOVMonsterTeam monsters = new LOVMonsterTeam();

    private int round = 0;

    private HashSet<Integer> heroIds = new HashSet<>();
    private int isDead = 0;

    private int heroNum = configUtil.getConfigInt("heroNum");

    private static LOVGame instance = new LOVGame();

    public static LOVGame getInstance(){
        return instance;
    }

    private LOVGame(){
        printInitWords();
        lovGrid = new LOVGrid();
        heroes = new LOVHeroTeam(heroNum);
        for (int i = 0; i < heroNum; i++) {
            System.out.print("\n");
            System.out.printf("Choose Lane %s Hero %d\n",laneList[i],i);
            Hero target = initHero(chooseOccupation(i));
            target.setPos(lovGrid.getSize()-1, i * 3 + randomUtil.nextInt(2));
            ((AccessibleCell)lovGrid.getCell(target.getPos())).setHero(target);
            heroes.setMember(i, target);
            System.out.printf("%s joins the team!\n", target.toString());
        }
//        generateNewMonster();
    }

    @Override
    public void start() throws InterruptedException {
        while (true){
            if (round % 8 == 0){
                generateNewMonster();
            }

            for (Hero hero : heroes) {
                while(true){
                    printMap();
                    int[] pos = hero.getPos().clone();
                    String next = askMovement(hero);

                    if (next.equalsIgnoreCase("w")){
                        pos[0]--;
                        try {
                            lovGrid.enter(hero, pos);
                            break;
                        }catch (ArrayIndexOutOfBoundsException e){
                            System.out.println(MyFont.ANSI_RED + "Sorry you can't go there, it is out of the playing area."+MyFont.ANSI_RESET);
                        }
                    }else if (next.equalsIgnoreCase("a")){
                        pos[1]--;
                        try {
                            lovGrid.enter(hero, pos);
                            break;
                        }catch (ArrayIndexOutOfBoundsException e){
                            System.out.println(MyFont.ANSI_RED + "Sorry you can't go there, it is out of the playing area."+MyFont.ANSI_RESET);
                        }
                    }else if (next.equalsIgnoreCase("s")){
                        pos[0]++;
                        try {
                            lovGrid.enter(hero, pos);
                            break;
                        }catch (ArrayIndexOutOfBoundsException e){
                            System.out.println(MyFont.ANSI_RED + "Sorry you can't go there, it is out of the playing area."+MyFont.ANSI_RESET);
                        }
                    }else if (next.equalsIgnoreCase("d")){
                        pos[1]++;
                        try {
                            lovGrid.enter(hero, pos);
                            break;
                        }catch (ArrayIndexOutOfBoundsException e){
                            System.out.println(MyFont.ANSI_RED + "Sorry you can't go there, it is out of the playing area."+MyFont.ANSI_RESET);
                        }
                    }else if (next.equalsIgnoreCase("q")){
                        printEndGame();
                        return;
                    }else if (next.equalsIgnoreCase("i")){
                        showInfo();
                        continue;
                    }else{
                        heroAttack(Integer.parseInt(next), hero);
                        break;
                    }
                }

            }

            // monster movement
            round++;
        }
    }

    private String askMovement(Hero hero){
        System.out.println("Decide your next movement!");
        String pattern = "[wsdqiWSDQI";
        if (hasSameLineMonster(hero)){
            System.out.print(MyFont.ANSI_DELETE);
        }else {
            pattern = pattern + "aA";
        }
        System.out.println("W/w: move up");
        System.out.print(MyFont.ANSI_RESET);

        System.out.println("""
                A/a: move left
                S/s: move down
                D/d: move right
                """);
        int available = -1;
        if (hasNearByMonster(hero)) {
            available = howToAttack(hero);
        }
        System.out.println("""
                Q/q: quit game
                I/i: show information.
                """);
        if (available>0){
            pattern = pattern+String.valueOf(available)+"]";
        }
        else{
            pattern = pattern+"]";
        }
        while(true){
            try {
                Scanner scanner = new Scanner(System.in);
                return scanner.next(Pattern.compile(pattern)).toLowerCase();
            } catch (NoSuchElementException e) {
                System.out.println("Sorry, you can only enter the letter above:");
            }
        }
    }

    private boolean hasNearByMonster(Hero hero){
        // code here
        if (hasSameLineMonster(hero)){
            return true;
        }

        int[] pos = hero.getPos();
        for (int j = 0; j < 3; j++) {
            if (pos[0] < 1 || pos[1]+j-1<0 || pos[1]+j-1 > lovGrid.getSize()-1){
                continue;
            }
            Cell cell = lovGrid.getCell(pos[0]-1, pos[1]+j-1);
            if (cell instanceof AccessibleCell && ((AccessibleCell) cell).getMonster()!= null){
                return true;
            }
        }

        return false;
    }

    private boolean hasSameLineMonster(Hero hero){
        // code here
        int[] pos = hero.getPos();
        for (int j = 0; j < 3; j++) {
            if (pos[1]+j-1 < 0 || pos[1]+j-1  >= lovGrid.getSize()){
                continue;
            }
            Cell cell = lovGrid.getCell(pos[0], pos[1]+j-1);
            if (cell instanceof AccessibleCell && ((AccessibleCell) cell).getMonster()!= null){
                return true;
            }
        }
        return false;
    }

    public void generateNewMonster(){
        for (int i = 0; i < heroes.size(); i++) {
            Monster newMonster = MonsterCreator.createMonsterByLevel(heroes.getMember(i).getLevel());
            assert newMonster != null;
            newMonster.setPos(0, i * 3 + randomUtil.nextInt(2));
            ((AccessibleCell)lovGrid.getCell(newMonster.getPos())).setMonster(newMonster);
            monsters.addMember(newMonster);
        }
    }


    private int howToAttack(Hero hero){
        int available = 1;
        while(true){
            try{
                System.out.println("You can also choose to attack or defense:");
                System.out.println("1: do a regular attack. ");
                if (!hero.getBag().hasItem("Spell")){
                    System.out.print(MyFont.ANSI_DELETE);
                    System.out.print("2: cast a spell. ");
                    System.out.print(MyFont.ANSI_RESET);
                    System.out.println(MyFont.ANSI_ITALIC+ MyFont.ANSI_GREY + hero.getType() + " " +
                            hero.getName() + " has no spell in the bag. " + MyFont.ANSI_RESET);
                }else if (!hero.getBag().canUseSpell()){
                    System.out.print(MyFont.ANSI_DELETE);
                    System.out.print("2: cast a spell. ");
                    System.out.print(MyFont.ANSI_RESET);
                    System.out.println(MyFont.ANSI_ITALIC+ MyFont.ANSI_GREY +
                            "The least mana-cost spell in the bag costs more than the mana"+hero.getType() + " " +
                            hero.getName()+"has."  + MyFont.ANSI_RESET);

                }else  {
                    System.out.println("2: cast a spell. ");
                    available = available * 10 + 2;
                }

                if (!hero.getBag().hasItem("Potion")){
                    System.out.print(MyFont.ANSI_DELETE);
                    System.out.print("3: use a potion. ");
                    System.out.print(MyFont.ANSI_RESET);
                    System.out.println(MyFont.ANSI_ITALIC+ MyFont.ANSI_GREY + hero.getType() + " " +
                            hero.getName() + " has no potion in the bag." + MyFont.ANSI_RESET);
                }else {
                    System.out.println("3: use a potion. ");
                    available = available * 10 + 3;
                }

                if (!hero.getBag().hasItem("Armor")){
                    System.out.print(MyFont.ANSI_DELETE);
                    System.out.print("4: change your armor. ");
                    System.out.print(MyFont.ANSI_RESET);
                    System.out.println(MyFont.ANSI_ITALIC+ MyFont.ANSI_GREY + hero.getType() + " " +
                            hero.getName() + " has no armor in the bag." + MyFont.ANSI_RESET);
                }else {
                    System.out.println("4: change your armor. ");
                    available = available * 10 + 4;
                }

                if (!hero.getBag().hasItem("Weapon")){
                    System.out.print(MyFont.ANSI_DELETE);
                    System.out.print("5: change your weapon. ");
                    System.out.print(MyFont.ANSI_RESET);
                    System.out.println(MyFont.ANSI_ITALIC+ MyFont.ANSI_GREY + hero.getType() + " " +
                            hero.getName() + " has no weapon in the bag." + MyFont.ANSI_RESET);
                }else {
                    System.out.println("5: change your weapon. ");
                    available = available * 10 + 5;
                }

                return available;
            } catch (Exception e){
                System.out.println("Please enter correct number:");
            }
        }
    }

    private void heroAttack(int ans, Hero hero){
        if (ans == 1 || ans == 2){
            List<Monster> targets = getNearbyMonster(hero);
            showTargetMonsterInfo(targets);
            System.out.printf("Choose the monster that %s want to attack\n",hero.toString());
            int index = askAttackTarget(targets);
            Monster target = targets.get(index);
            hero.attack(target, ans);
        }else{
            hero.attack(null, ans);
        }
    }

    private void showHeroInfo(){
        // more specifically their level, their hp, their mana and their currently equipped weapons
        // and armors
        // print Hero Info
        printUtil.printObjectInfoTableWithId("Heroes", heroes.getMembers(),
                0, "Occupation", "Name","Lv","HP","Mana", "EXP","Money", "Strength", "Agility", "Dexterity", "Weapon","Armor");

    }

    private void showTargetMonsterInfo(List<Monster> targets){
        // more specifically their level, their hp, their defense and their damage
        // print Monster Info
        printUtil.printObjectInfoTableWithId("Monsters", targets,
                0, "Type",  "Name","Lv","HP","Defense", "Damage");
    }

    private ArrayList<Monster> getNearbyMonster(Hero hero){
        int[] pos = hero.getPos();
        ArrayList<Monster> targets = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                Cell cell = lovGrid.getCell(pos[0]-i, pos[1]+j-1);
                if (cell instanceof AccessibleCell && ((AccessibleCell) cell).getMonster()!= null){
                    targets.add(((AccessibleCell) cell).getMonster());
                }
            }
        }
        return targets;
    }

    // get the target monster. Ask hero which monster to attack.
    private int askAttackTarget(List<Monster> monsters){
        String ans ;

        while (true){
            try {
                Scanner scanner = new Scanner(System.in);
                ans = scanner.next("[0-"+ (monsters.size()-1) +"qmi]");
                if (ans.equalsIgnoreCase("q")){
                    showHeroInfo();
                }else if (ans.equalsIgnoreCase("m")) {
                    Legends.getInstance().printMap();
                }else if (ans.equalsIgnoreCase("i")){
                    Legends.getInstance().showInfo();
                }else if (monsters.get(Integer.parseInt(ans)).getHP() <= 0){
                    System.out.println("You should choose a alive target.");
                }else {
                    return Integer.parseInt(ans);
                }
            } catch (NoSuchElementException e) {
                System.out.println("Please input the corresponding number.");
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

    public void showInfo(){
        //        more specifically their hp, their level, their mana, their current exp, their money
//        and their skill levels
        printUtil.printObjectInfoTableWithId("INFORMATION", heroes.getMembers(),0,
                "Type", "Name","Lv","HP","Mana", "EXP","Money", "Strength", "Agility", "Dexterity", "Weapon","Armor");
        printUtil.printObjectInfoTableWithId("Monsters", monsters.getMembers(),
                0, "Type", "Name","Lv","HP","Defense", "Damage");
    }

    public void printMap(){
        StringBuilder s = new StringBuilder();
        int size = lovGrid.getSize();
//        int[] heroPos = heroes.getPos();
        for (int i = 0; i < size; i++) {
            s.append("+------".repeat(size));
            s.append("+");
            s.append("\n");
            for (int j = 0; j < size; j++) {
                Cell cell = lovGrid.getCell(i, j);
                if (cell instanceof AccessibleCell){
                    s.append("|");
                    s.append(cell.getColor());
                    if (((AccessibleCell<?>) cell).getMonster()!=null){
                        s.append(((AccessibleCell<?>) cell).getMonster().mark());
                    }else {
                        s.append(cell.toString());
                    }

                    if (((AccessibleCell<?>) cell).getHero()!=null){
                        s.append(((AccessibleCell<?>) cell).getHero().mark());
                    }else {
                        s.append(cell.toString());
                    }

                    s.append(MyFont.ANSI_RESET);
                }else{
                    s.append("|").append(lovGrid.getCell(i, j).toString().repeat(2));
                }
            }
            s.append("|\n");
        }
        s.append("+------".repeat(Math.max(0, size)));
        s.append("+");
        System.out.println(s);
    }


    public void printEndGame(){
        System.out.println("Adventure ends.");
    }

}
