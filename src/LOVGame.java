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
        SoundPlayUtil.playGameStart();
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
            SoundPlayUtil.playJoinTeam();
            System.out.printf("%s joins the team!\n\n", target.toString());
        }
//        generateNewMonster();
    }

    @Override
    public void start() throws InterruptedException {
        SoundPlayUtil.playGoIntoMap();
        for (Hero hero : heroes) {
            System.out.println("Starting From Nexus cell! Come and Buy your first Item!");
            lovGrid.enter(hero, hero.getPos());
        }

        while (true){
            if (round % 8 == 0){
                generateNewMonster();
            }

            for (Hero hero : heroes) {
                while(true){
                    printMap(hero);
                    int[] pos = hero.getPos().clone();
                    String next = askMovement(hero);

                    if (next.equalsIgnoreCase("q")){
                        printEndGame();
                        return;
                    }else if (next.equalsIgnoreCase("i")){
                        showInfo();
                        continue;
                    }else if (next.equalsIgnoreCase("b")){
                        int line = hero.getPos()[1];
                        lovGrid.enter(hero, new int[]{lovGrid.getSize()-1, line});
                        break;
                    }else if(java.lang.Character.isDigit(next.charAt(0))){
                        heroAttack(Integer.parseInt(next), hero);
                        break;
                    }else if (next.equalsIgnoreCase("t")){
                        // teleport
                        int laneId = askTeleportLane(hero);
                        teleportHero(hero, laneId);
                        break;
                    }else{
                        if (next.equalsIgnoreCase("w")){
                            pos[0]--;
                        }else if (next.equalsIgnoreCase("a")){
                            pos[1]--;
                        }else if (next.equalsIgnoreCase("s")){
                            pos[0]++;
                        }else if (next.equalsIgnoreCase("d")) {
                            pos[1]++;
                        }
                        try {
                            lovGrid.enter(hero, pos);
                            break;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            SoundPlayUtil.playError();
                            System.out.println(MyFont.ANSI_RED + "Sorry you can't go there, it is out of the playing area." + MyFont.ANSI_RESET);
                        }
                    }
                }
                hero.recover();
                if (hero.getPos()[0] == 0){
                    SoundPlayUtil.playWin();
                    System.out.println("Hero team reach the opponents' Nexus Cell. Hero team wins!");
                    printMap(hero);
                    printEndGame();
                    return;
                }
            }

            // monster movement
            for (Monster monster : monsters) {
                Hero target = getNearByHeroOrNull(monster);
                if (target != null){
                    monster.attack(target);
                    if(target.getHP()<=0){
                        System.out.printf("%s%s is faint, go back to Nexus Cell.\n",MyFont.ANSI_RED+MyFont.ANSI_BOLD, target);
                        int line = target.getPos()[1];
                        target.revive(true);
                        lovGrid.enter(target, new int[]{lovGrid.getSize()-1, line});
                    }
                }else{
                    int[] pos = monster.getPos().clone();
                    pos[0]++;
                    lovGrid.enter(monster, pos);
                }

                if (monster.getPos()[0] == lovGrid.getSize()-1){
                    SoundPlayUtil.playLose();
                    System.out.println("Monster team reach the opponents' Nexus Cell. Monster team wins...");
                    printEndGame();
                    return;
                }
            }


            round++;
        }
    }

    private String askMovement(Hero hero){
        System.out.printf("Decide %s's next movement!(%s is in lane %s, row %d, col %d)\n",
                hero, hero, laneList[hero.getPos()[1]/3], hero.getPos()[0], hero.getPos()[1]);
        String pattern = "[sdqiaASDQIbBtT";
        if (hasSameLineMonster(hero)){
            System.out.print(MyFont.ANSI_DELETE);
        }else {
            pattern = pattern + "wW";
        }
        System.out.println("W/w: move up");
        System.out.print(MyFont.ANSI_RESET);

        System.out.println("""
                A/a: move left
                S/s: move down
                D/d: move right
                T/t: teleport to help you friends
                B/b: back to Nexus Cell
                """);

        int available = 0;
        if (hasNearByMonster(hero)) {
            available = howToAttackInBattle(hero);
        }


        if (hero.getBag().hasItem("Potions")){
            System.out.println("3: use a potion. ");
            available = available * 10 + 3;
        }

        if (hero.getBag().hasItem("Armory")){
            System.out.println("4: change your armor. ");
            available = available * 10 + 4;
        }

        if (hero.getBag().hasItem("Weaponry")){
            System.out.println("5: change your weapon. ");
            available = available * 10 + 5;
        }


        System.out.print("\n");

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
//                return scannerUtil.readLine(pattern);
                Scanner scanner = new Scanner(System.in);
//                SoundPlayUtil.playInput();
                return scanner.next(pattern).toLowerCase();
            } catch (NoSuchElementException e) {
                SoundPlayUtil.playError();
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

    private Hero getNearByHeroOrNull(Monster monster){

        int[] pos = monster.getPos();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (pos[0]+i < 0 || pos[1]+j-1 < 0 || pos[1]+j-1 > lovGrid.getSize()-1){
                    continue;
                }
                Cell cell = lovGrid.getCell(pos[0]+i, pos[1]+j-1);
                if (cell instanceof AccessibleCell && ((AccessibleCell) cell).getHero()!= null){
                    return ((AccessibleCell) cell).getHero();
                }
            }
        }
        return null;
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


    private int howToAttackInBattle(Hero hero){
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
                return available;
            } catch (Exception e){
                SoundPlayUtil.playError();
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
            if (isTargetDead(target)){
                hero.gainAndLevelUp(target.getLevel()+2, hero.getLevel()*100);

                System.out.print("You are close to victory!\n");
                monsters.removeDead(target);
                int [] monsterPos = target.getPos();
                ((AccessibleCell<Character>)lovGrid.getCell(monsterPos)).setMonsterNull();

            }
        }else{
            hero.attack(null, ans);
        }
    }

    private void showHeroInfo(){
        // more specifically their level, their hp, their mana and their currently equipped weapons
        // and armors
        // print Hero Info
        printUtil.printObjectInfoTableWithId("Heroes", heroes.getMembers(),
                0, "Occupation", "Name","Lv","HP","Mana", "EXP","Money", "Strength", "Agility", "Dexterity", "Weaponry","Armory");

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
                if (pos[0]-i < 0 || pos[1]+j-1 < 0 || pos[1]+j-1 > lovGrid.getSize()-1){
                    continue;
                }
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
                    printEndGame();
                    System.exit(-1);
                }else if (ans.equalsIgnoreCase("m")) {
                    Legends.getInstance().printMap();
                }else if (ans.equalsIgnoreCase("i")){
                    Legends.getInstance().showInfo();
                }else if (monsters.get(Integer.parseInt(ans)).getHP() <= 0){
                    SoundPlayUtil.playError();
                    System.out.println("You should choose a alive target.");
                }else {
                    return Integer.parseInt(ans);
                }
            } catch (NoSuchElementException e) {
                SoundPlayUtil.playError();
                System.out.println("Please input the corresponding number.");
            }
        }
    }

    private int askTeleportLane(Hero hero){
        int[] pos = hero.getPos();
        int laneId = pos[1]/3;
        String lane = laneList[laneId];
        System.out.printf("%s is in Lane %s now, which Lane do you want to teleport to?\n", hero.toString(), lane);
        int available = 0;
        for (int i = 0; i < laneList.length; i++) {
            if (i == laneId){
                System.out.print(MyFont.ANSI_GREY + MyFont.ANSI_DELETE);
                System.out.printf("%d: Lane %s", i+1 , laneList[i]);
                System.out.print(MyFont.ANSI_RESET);
                System.out.print(MyFont.ANSI_GREY + MyFont.ANSI_ITALIC);
                System.out.println(" You can't teleport here because you are currently in this lane." + MyFont.ANSI_RESET);
            }else{
                System.out.printf("%d: Lane %s\n", i+1, laneList[i]);
                available = available * 10 + i+1;
            }
        }
        while (true){
            try{
                Scanner scanner = new Scanner(System.in);
                String s = scanner.next("["+available+"]");
                return Integer.parseInt(s) - 1;
            }catch (Exception e){
                SoundPlayUtil.playError();
                System.out.println("Please choose an available lane.");
            }
        }
    }

    private void teleportHero(Hero hero, int laneId){
        System.out.printf("%s will teleport to lane %s\n", hero, laneList[laneId]);
        int monsterRow = getMonsterRow(laneId);
        printLane(laneId,monsterRow);
        System.out.println("Please enter the id of cell that you want to teleport(Only those cells that have number can be chosen.)\n" +
                MyFont.ANSI_RED+"Input instruction: if map shows: "+MyFont.ANSI_BOLD + "6,7, please enter 67"+MyFont.ANSI_RESET);
        while (true){
            try {
//                String s = scannerUtil.readLine();
                int ans = scannerUtil.readInt();
                if(ans >= monsterRow * 10 && ans % 10 >= (laneId-1) * 3 && ans % 10 < laneId * 3){
                    Cell cell = lovGrid.getCell(ans/10, ans%10);
                    if (((AccessibleCell<Character>) cell).isExplored()){
                        SoundPlayUtil.playTeleport();
                        lovGrid.enter(hero, new int[]{ans/10, ans%10});
                        break;
                    }else{
                        SoundPlayUtil.playError();
                        System.out.println("Please enter the correct number.");
                    }
                }else{
                    SoundPlayUtil.playError();
                    System.out.println("Please enter the correct number.");
                }
            }catch (Exception e){
                SoundPlayUtil.playError();
                System.out.println("Please enter the correct number.");
            }
        }
    }

    private int getMonsterRow(int laneId){
        int laneStart = laneId * 3;
        int size = 2;
        int max = 0;
        for (int i = laneStart; i < laneStart + size; i++) {
            for (int j = 0; j < size; j++) {
                Cell cell = lovGrid.getCell(j,laneStart);
                if (((AccessibleCell)cell).getMonster() != null){
                    max = cell.getRow();
                    max = cell.getRow();
                    break;
                }

            }
        }
        return max;
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
//                Scanner scanner = new Scanner(System.in);
                index = scannerUtil.readInt();
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
                Welcome to the Legends Of Valor!
                You are in the Legends world. There are several types of area in the world.
                ▩▩Inaccessible: can't move into
                  Plain: common area
                \u001B[42m  \u001B[0mBush: increase the dexterity of hero
                \u001B[44m  \u001B[0mCave:increase the agility of hero
                \u001B[43m  \u001B[0mKoulou: increase the strength of hero
                \u001B[41m  \u001B[0m\u001B[46m  \u001B[0mNexus: where monsters and heroes are born. Hero can buy or sell item here.
                
                The game map is an \u001B[1m8x8\u001B[0m grid. \u001B[1m\u001B[33mcol 2 & col 5\u001B[0m are inaccessible. And therefore 
                the map are seperated into three lanes by the inaccessible cells.
                \u001B[35mRow 0 is Monster's Nexus Area. \u001B[36mRow 7 is Hero's Nexus Area. \u001B[0mOther areas are distributed ramdonly.
                
                Every round, hero can choose to move ,change weapon/armor, use a potion, go back to Nexus Cell or teleport to other lane.
                When there are monsters near by, besides the actions above, heroes can also do aregular attack or cast a spell to the monster.
                One hero can only take one action in a round.
                \u001B[3m\u001B[37mNotice1: If heroes don't have spell, potion, armor or weapon in their bag, they can't take corresponding actions.
                If they don't have weapon/armor in their bag but have worn the armor or had weapon 
                in the hand, they still can't change their armor/weapon. Obviously, it's unwise to remove them)        
                Notice2: You can't cancel your action during battle, but you can check hero information and their bags at any moment.
                So please check the info before you make decision if you don't know you really want to change weapon/armor.\u001B[0m
                                
                Your team members will be born in the Hero's Nexus Cells, which are in the \u001B[1m\u001B[3m\u001B[36mbottom\u001B[0m of the map.
                
                \u001B[1mYou can enter m,i or q to show map, show info or quit the game at any time.\u001B[0m
                Now! Choose your game settings and begin your adventure!
                 \u001B[1m\u001B[31mAfter an attack, you will see the battle info, press enter to continue.
                 After an attack, you will see the battle info, press enter to continue.
                 After an attack, you will see the battle info, press enter to continue.\u001B[0m
                """);
    }

    // initializing the hero.
    private Hero initHero(int occId){
        String occName = occupationList[occId];

        heroCreator.showOccInfo(occName);

        System.out.println("Please choose your partner and enter the corresponding number:");
        int index;
        while (true){
            try{
//                Scanner scanner = new Scanner(System.in);
                index = scannerUtil.readInt();
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
                "Type", "Name","Lv","HP","Mana", "EXP","Money", "Strength", "Agility", "Dexterity", "Weaponry","Armory");
        printUtil.printObjectInfoTableWithId("Monsters", monsters.getMembers(),
                0, "Type", "Name","Lv","HP","Defense", "Damage");
    }

    public void printMap(Character target){
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
                        Hero hero = ((AccessibleCell<?>) cell).getHero();
                        if (target != null && hero == target){
                            s.append(MyFont.ANSI_RED).append(MyFont.ANSI_BOLD);
                        }
                        s.append(hero.mark()).append(MyFont.ANSI_RESET);
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

    public void printMap(){
        printMap(null);
    }

    private void printLane(int laneId, int monsterRow){
        int laneStart = laneId * 3;
        StringBuilder s = new StringBuilder();
        int size = 2;

        for (int j = 0; j < lovGrid.getSize(); j++) {
            s.append("+------".repeat(size));
            s.append("+");
            s.append("\n");
            for (int i = laneStart; i < laneStart + size; i++) {
                Cell cell = lovGrid.getCell(j, i);
                s.append("|");
                s.append(cell.getColor());
                if (((AccessibleCell<?>) cell).getMonster()!=null){
                    s.append(((AccessibleCell<?>) cell).getMonster().mark());
                }else {
                    s.append(cell.toString());
                }

                if (((AccessibleCell<?>) cell).getHero()!=null){
                    s.append(((AccessibleCell<?>) cell).getHero().mark());
                }else if (((AccessibleCell<?>) cell).isExplored() && j > monsterRow) {
                    s.append(cell.getRow()).append(",").append(cell.getCol());
                }else {
                    s.append(cell.toString());
                }

                s.append(MyFont.ANSI_RESET);
            }
            s.append("|\n");
        }
        s.append("+------".repeat(Math.max(0, size)));
        s.append("+");
        System.out.println(s);
    }

    private boolean isTargetDead(Character character){
        return character.getHP() <= 0;
    }

    public void printEndGame(){
        System.out.println("Adventure ends.");
    }
}
