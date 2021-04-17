import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

// Battle class. When enter a common cell, there is a chance to meet monsters and instantiating Battle class.
// Battle class will create the same number and level monsters as the heroes.
public class Battle {
    private Monster[] monsters;
    private Team<Hero> heroes;
    private int round = 0;
    public Battle(Team heroes){
        this.heroes = heroes;
        initMonster();
        fight();
    }


    private void initMonster(){
        monsters = new Monster[heroes.getTeamSize()];
        for (int i = 0; i < monsters.length; i++) {
            monsters[i] = MonsterCreator.createMonsterByLevel(heroes.getMember(i).getLevel());
        }
    }


    public void fight(){
        printWarning();
        while (true){
            System.out.println(MyFont.ANSI_RED + MyFont.ANSI_BOLD + "Round " + round++ + MyFont.ANSI_RESET);

            System.out.println("Heroes' Turn!");
            for (Hero hero: heroes){
                if (hero.getHP()>0){
                    System.out.printf("%s's turn:\n", hero.toString());

                    int ans = howToAttack(hero);
                    if (ans == 1 || ans == 2){
                        showMonsterInfo();
                        System.out.printf("Choose the monster that %s want to attack\n",hero.toString());
                        int index = askAttackTarget();
                        Monster target = monsters[index];
                        hero.attack(target, ans);
                    }else{
                        hero.attack(null, ans);
                    }
                    hero.recover();
                }else {
                    System.out.printf("%s is faint...Skip...", hero.toString());
                    continue;
                }

                if(isMonsterDead()){
                    heroWins();
                    return;
                }
            }

            Random rnd = new Random();
            System.out.println("\nMonsters' Turn!");
            for (Monster monster: monsters){
                if (monster.getHP() > 0){
                    while (true){
                        int index = rnd.nextInt(heroes.getTeamSize());
                        Hero target = heroes.getMember(index);
                        if (target.getHP() > 0){
                            monster.attack(target);
                            break;
                        }
                    }
                }

                if (heroes.isDead()){
                    monsterWins();
                    return;
                }
            }
        }
    }

    // get the target monster. Ask hero which monster to attack.
    private int askAttackTarget(){
        String ans ;

        while (true){
            try {
                Scanner scanner = new Scanner(System.in);
                ans = scanner.next("[0-"+ (monsters.length-1) +"qmi]");
                if (ans.equalsIgnoreCase("q")){
                    showHeroInfo();
                }else if (ans.equalsIgnoreCase("m")) {
                    Legends.getInstance().printMap();
                }else if (ans.equalsIgnoreCase("i")){
                    Legends.getInstance().showInfo();
                }else if (monsters[Integer.parseInt(ans)].getHP() <= 0){
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

    // ask hero how to attack. If the hero don't have enough items in the bag, the corresponding action is forbidden.
    // 1 do a regular attack
    // 2 cast a spell
    // 3 use a portion
    // 4 change armor
    // 5 change weapon
    private int howToAttack(Hero hero){
        int ans;
        int available = 1;
        while(true){
            try{
                System.out.println("Please decide what to do:");
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

                if (!hero.getBag().hasItem("Potions")){
                    System.out.print(MyFont.ANSI_DELETE);
                    System.out.print("3: use a potion. ");
                    System.out.print(MyFont.ANSI_RESET);
                    System.out.println(MyFont.ANSI_ITALIC+ MyFont.ANSI_GREY + hero.getType() + " " +
                            hero.getName() + " has no potion in the bag." + MyFont.ANSI_RESET);
                }else {
                    System.out.println("3: use a potion. ");
                    available = available * 10 + 3;
                }

                if (!hero.getBag().hasItem("Armory")){
                    System.out.print(MyFont.ANSI_DELETE);
                    System.out.print("4: change your armor. ");
                    System.out.print(MyFont.ANSI_RESET);
                    System.out.println(MyFont.ANSI_ITALIC+ MyFont.ANSI_GREY + hero.getType() + " " +
                            hero.getName() + " has no armor in the bag." + MyFont.ANSI_RESET);
                }else {
                    System.out.println("4: change your armor. ");
                    available = available * 10 + 4;
                }

                if (!hero.getBag().hasItem("Weaponry")){
                    System.out.print(MyFont.ANSI_DELETE);
                    System.out.print("5: change your weapon. ");
                    System.out.print(MyFont.ANSI_RESET);
                    System.out.println(MyFont.ANSI_ITALIC+ MyFont.ANSI_GREY + hero.getType() + " " +
                            hero.getName() + " has no weapon in the bag." + MyFont.ANSI_RESET);
                }else {
                    System.out.println("5: change your weapon. ");
                    available = available * 10 + 5;
                }


                Scanner scanner = new Scanner(System.in);
                String get = scanner.next("["+ available +"imq]");
                if (get.equalsIgnoreCase("i")){
                    showInfo();
                    return howToAttack(hero);
                }else if (get.equalsIgnoreCase("m")){
                    Legends.getInstance().printMap();
                }else if (get.equalsIgnoreCase("q")){
                    Legends.getInstance().printEndGame();
                    System.exit(-1);
                }else {
                    return Integer.parseInt(get);
                }
            } catch (Exception e){
                System.out.println("Please enter correct number:");
            }
        }
    }


    // check whether all the monster are dead.
    private boolean isMonsterDead(){
        for (Monster monster : monsters) {
            if (monster.getHP() > 0) {
                return false;
            }
        }
        return true;
    }


    // print init message
    private void printWarning(){
        System.out.println(MyFont.ANSI_BACKGROUNDRED+ MyFont.ANSI_BOLD+
                "                      !!!WARNING!!!                      " + MyFont.ANSI_RESET);
        System.out.println(MyFont.ANSI_BACKGROUNDYELLOW+ MyFont.ANSI_BOLD+ "                   !!!MONSTER ATTACK!!!                  " + MyFont.ANSI_RESET);
        System.out.println(monsters.length + " Monsters!");
        showMonsterInfo();
    }


    // print info when enter i/I
    private void showInfo(){
        showHeroInfo();
        showMonsterInfo();
    }

    private void showHeroInfo(){
        // more specifically their level, their hp, their mana and their currently equipped weapons
        // and armors
        // print Hero Info
        printUtil.printObjectInfoTableWithId("Heroes", heroes.getMembers(),
                0, "Occupation",  "Name","Lv","HP","Mana", "EXP","Money", "Strength", "Agility", "Dexterity", "Weaponry","Armory");

    }

    private void showMonsterInfo(){
        // more specifically their level, their hp, their defense and their damage
        // print Monster Info
        printUtil.printObjectInfoTableWithId("Monsters", Arrays.asList(monsters),
                0, "Type",  "Name","Lv","HP","Defense", "Damage");
    }

    // settlement after heroes win
    private void heroWins(){
        System.out.println(MyFont.ANSI_BACKGROUNDCYAN + "            BATTLE ENDS!            " + MyFont.ANSI_RESET);
        System.out.println(MyFont.ANSI_BOLD + MyFont.ANSI_CYAN + "_|          _|  _|_|_|  _|      _|  \n" +
                "_|          _|    _|    _|_|    _|  \n" +
                "_|    _|    _|    _|    _|  _|  _|  \n" +
                "  _|  _|  _|      _|    _|    _|_|  \n" +
                "    _|  _|      _|_|_|  _|      _|  \n" +
                "                                    \n" + MyFont.ANSI_RESET);
        System.out.println(MyFont.ANSI_BACKGROUNDWHITE + "             BATTLE INFO            " + MyFont.ANSI_RESET);
        showMonsterInfo();
        int level = accumEnemyLevel();
        heroes.gainAfterBattle(level + 2,level * 100 );
    }

    // settlement after hero lose.
    private void monsterWins(){
        System.out.println(MyFont.ANSI_BACKGROUNDRED + "              BATTLE ENDS!              " + MyFont.ANSI_RESET);
        System.out.println(MyFont.ANSI_BOLD + MyFont.ANSI_RED +"_|          _|_|      _|_|_|  _|_|_|_|  \n" +
                "_|        _|    _|  _|        _|        \n" +
                "_|        _|    _|    _|_|    _|_|_|    \n" +
                "_|        _|    _|        _|  _|        \n" +
                "_|_|_|_|    _|_|    _|_|_|    _|_|_|_|  " + MyFont.ANSI_RESET);
        System.out.println(MyFont.ANSI_BACKGROUNDWHITE + "               BATTLE INFO              " + MyFont.ANSI_RESET);
        showHeroInfo();

        System.out.println();
    }

    private int accumEnemyLevel(){
        int level = 0;
        for (int i = 0; i < monsters.length; i++) {
            level += monsters[i].getLevel();
        }
        return level;
    }

}
