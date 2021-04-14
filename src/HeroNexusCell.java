import java.util.InputMismatchException;
import java.util.Scanner;

// market cell. Hero can buy or sell items here.
public class HeroNexusCell extends NexusCell{
    private static String[][] weapon;
    private static String[][] armor;
    private static String[][] potion;
    private static String[][] spell;
    private static String[][] marketGood;

    private static final String armorString = """
            Platinum_Shield       Armor  150   1   200
            Breastplate           Armor  350   3   600
            Full_Body_Armor       Armor  1000  8   1100
            Wizard_Shield         Armor  1200  10  1500
            Guardian_Angel        Armor  1000  10  1000""".replaceAll("\t"," ");

    private static final String spellString = """
            Flame_Tornado   FireSpell  700     4   850     300
            Breath_of_Fire  FireSpell  350     1   450     100
            Heat_Wave       FireSpell  450     2   600     150
            Lava_Comet      FireSpell  800     7   1000    550
            Hell_Storm      FireSpell  600     3   950     600
            Snow_Cannon     IceSpell  500     2   650     250
            Ice_Blade       IceSpell  250     1   450     100
            Frost_Blizzard  IceSpell  750     5   850     350
            Arctic_Storm    IceSpell  700     6   800     300
            Lightning_Dagger      LightningSpell  400        1       500     150
            Thunder_Blast         LightningSpell  4750        4       950     400
            Electric_Arrows       LightningSpell  4550        5       650     200
            Spark_Needles         LightningSpell  4500        2       600     200""".replaceAll("\t"," ");

    private static final String potionString = """
            Healing_Potion  Potion 250     1   100		HP
            Strength_Potion Potion 200     1   75		Strength
            Magic_Potion    Potion 350     2   100		Mana
            Luck_Elixir     Potion 500     4   65  	Agility
            Mermaid_Tears   Potion 850     5   100  	HP/Mana/Strength/Agility
            Ambrosia        Potion 1000    8   150		HP/Mana/Strength/Dexterity/Defense/Agility""".replaceAll("\t"," ");

    private static final String weaponString = """
            Sword           Weapon 500     1    800    1
            Bow             Weapon 300     2    500    2
            Scythe          Weapon 1000    6    1100   2
            Axe             Weapon 550     5    850    1
            TSwords     	Weapon 1400    8    1600   2
            Dagger          Weapon 200     1    250    1""".replaceAll("\t"," ");

    static {
        weapon = initItemList(weaponString);
        armor = initItemList(armorString);
        spell = initItemList(spellString);
        potion = initItemList(potionString);
        marketGood = new String[weapon.length + armor.length + potion.length + spell.length][];
        int index = 0;
        System.arraycopy(armor, 0, marketGood, 0, armor.length);

        index += armor.length;
        System.arraycopy(potion, 0, marketGood, index, potion.length);

        index += potion.length;
        System.arraycopy(spell, 0, marketGood, index, spell.length);

        index += spell.length;
        System.arraycopy(weapon, 0, marketGood, index, weapon.length);

    }
    private int[] pos = new int[2];
    public HeroNexusCell(int row, int col) {
        super(row, col);
    }

    @Override
    public void enter(Character member) {
        super.enter(member);
    }


    public void enter(Team<Hero> heroes){
        heroes.setPos(new int[]{pos[0], pos[1]});
        System.out.println("""
                Welcome to the SUPER LEGEND MARKET!
                \u001B[3mWhen one of the heroes want to sell/buy, first choose sell/buy, then choose the hero.\u001B[0m
                Enter q to leave the market! Enter any other key to enter the market...""");

        Scanner scanner = new Scanner(System.in);
        if ("q".equals(scanner.nextLine())){
            System.out.println("Entered q, leave the market!");
            return;
        }

        while(true){
            int buy = askBuyOrSell();
            switch (buy) {
                case 0->{
                    System.out.println("Leave the market.");
                    return;
                }
                case 1->{ // buy
                    System.out.println("Who want to buy items?");
                    for (int i = 0; i < heroes.getTeamSize(); i++) {
                        System.out.println(i+": "+heroes.getMember(i).getName());
                    }

                    while (true){
                        try {
                            Scanner scanner1 = new Scanner(System.in);
                            buy(heroes.getMember(scanner1.nextInt()));
                            break;
                        }catch (InputMismatchException e){
                            System.out.println("Incorrect format! You should enter a number. Please enter 0 to " + (heroes.getTeamSize()-1));
                        }catch (IndexOutOfBoundsException e){
                            System.out.println("Input out of bounds! Please enter 0 to " + (heroes.getTeamSize()-1));
                        }
                    }
                }
                case 2-> { // sell
                    System.out.println("Who want to sell items?");
                    boolean hasItem = false;
                    for (int i = 0; i < heroes.getTeamSize(); i++) {
                        if (heroes.getMember(i).getBag().getUsed() > 0){
                            System.out.println(i+": " + heroes.getMember(i).getName());
                            heroes.getMember(i).showBag();
                            System.out.println("\n");
                            hasItem = true;
                        }
                    }
                    if (!hasItem){
                        System.out.println("None of the heroes have any item. Nothing to sell.");
                        break;
                    }
                    System.out.println("Please enter the corresponding id:");

                    while (true) {
                        try {
                            Scanner scanner1 = new Scanner(System.in);
                            sell(heroes.getMember(scanner1.nextInt()));
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Incorrect format! You should enter a number. Please enter 1 to " + heroes.getTeamSize());
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("Input out of bounds! Please enter 1 to " + heroes.getTeamSize());
                        }
                    }
                }
            }
        }
    }

    // decide buy or sell
    private int askBuyOrSell(){

        String id;
        Scanner scanner = new Scanner(System.in);
        while (true){
            try {
                System.out.println("Want to buy or sell?");
                System.out.println("1: Buy");
                System.out.println("2: Sell");
                System.out.println("0: leave the market");
                System.out.println("m: show the map");
                System.out.println("q: quit the game");
                System.out.println("i: show information");
                id = scanner.next("[012qmi]");
                if (id.equalsIgnoreCase("q")){
                    Legends.getInstance().printEndGame();
                    System.exit(-1);
                }else if (id.equalsIgnoreCase("m")) {
                    Legends.getInstance().printMap();
                }else if (id.equalsIgnoreCase("i")){
                    Legends.getInstance().showInfo();
                }else {
                    return Integer.parseInt(id);
                }
            }catch (InputMismatchException e){
                System.out.println("Incorrect format! Please enter 1, 2 or 0.");
                scanner.nextLine();
            }
        }
    }

    // buy item
    public void buy(Hero hero){
        System.out.println("Here's the item list:");
        show();
        String id;
        String[] itemInfo;

        Scanner scanner = new Scanner(System.in);
        while(true){
            try {
                System.out.printf("%s has $%d, which item do you want? Enter the number:(Enter -1 to cancel)\n", hero.toString(), hero.getMoney());
                id = scanner.nextLine();
                if (id.equalsIgnoreCase("i")){
                    Legends.getInstance().showInfo();
                    continue;
                }else if (id.equalsIgnoreCase("m")){
                    Legends.getInstance().printMap();
                    continue;
                }else if (id.equalsIgnoreCase("q")){
                    Legends.getInstance().printEndGame();
                    System.exit(-1);
                }else if (id.equalsIgnoreCase("-1")){
                    break;
                }

                int val = Integer.parseInt(id);
                if (val >= 0 && val < marketGood.length){
                    itemInfo =  marketGood[val];
                }else{
                    System.out.println("Out of bounds.");
                    continue;
                }

                System.out.println("Item "+ itemInfo[0]+" is worth $"+itemInfo[2]+". Does Hero "+ hero.getName()+
                        " want to buy it? Enter y/Y to buy, enter other letter to cancel:");
                String ans = scanner.nextLine();
                if ("y".equalsIgnoreCase(ans)){
                    hero.buyItem(createItem(itemInfo));
                }else if (id.equalsIgnoreCase("i")){
                    Legends.getInstance().showInfo();
                    continue;
                }else if (id.equalsIgnoreCase("m")){
                    Legends.getInstance().printMap();
                    continue;
                }else if (id.equalsIgnoreCase("q")){
                    Legends.getInstance().printEndGame();
                    System.exit(-1);
                }else {
                    System.out.println(hero.toString()+" canceled the transaction!");
                }
                System.out.println("\n");
                break;
            }catch (InputMismatchException e){
                System.out.println("Incorrect format! Please enter a number:");
                scanner.nextLine();
            }catch (IndexOutOfBoundsException e) {
                System.out.println("Out of bounds. Please enter 0 to " + (marketGood.length - 1));
                scanner.nextLine();
            }catch (NumberFormatException e){
                System.out.println("Incorrect format! Please enter a number:");
            }
        }
    }

    // sell item
    public void sell(Hero hero){
        if (hero.getBag().getUsed() == 0){
            System.out.printf("Sorry %s doesn't have items to sell", hero.toString());
        }
        System.out.println("Here are the items "+ hero.getName() + " have:");
        hero.showBag();
        System.out.printf("Which item do %s want to sell?(The price is half of the buying price) \nEnter the number:\n", hero.toString());
        int id = 0;
        Item item;
        Scanner scanner = new Scanner(System.in);
        while(true){
            try {
                id = scanner.nextInt();
                item = hero.getItem(id);
                System.out.println("Item " + item.getName() + " is worth $" + (item.getCost()/2)+
                        ", do you want to sell it? Enter y/Y to sell, enter other letter to cancel:");
                String ans = scanner.nextLine();
                if ("y".equals(ans.toLowerCase())){
                    hero.sellItem(item);
                    System.out.println(hero.toString()+" sold the "+ item.getName() +
                            " and gain $" + item.getCost()/2 +". Now has $" + hero.getMoney());
                }else if (ans.equalsIgnoreCase("i")){
                    Legends.getInstance().showInfo();
                    continue;
                }else if (ans.equalsIgnoreCase("m")){
                    Legends.getInstance().printMap();
                    continue;
                }else if (ans.equalsIgnoreCase("q")){
                    Legends.getInstance().printEndGame();
                    System.exit(-1);
                }else {
                    System.out.println(hero.toString()+" canceled the transaction!");
                }
                System.out.println("\n");
                break;
            }catch (InputMismatchException e){
                System.out.println("Incorrect format! Please enter a number:");
                scanner.nextLine();
            }catch (IndexOutOfBoundsException e){
                System.out.println("Out of bounds. Please enter 0 to " + (hero.getItemNum()));
                scanner.nextLine();
            }
        }
    }

    // print the item list.
    private void show(){
        System.out.println(MyFont.ANSI_BOLD + "ITEM LIST:" + MyFont.ANSI_RESET);
        int start = 0;
        showArmor(start);
        start += armor.length;
        showPotion(start);
        start += potion.length;
        showSpell(start);
        start += spell.length;
        showWeapon(start);

    }

    // help function. show potion.
    private void showPotion(int start){
        printUtil.printInfoTableWithId("Potion", potion, start, "ItemName",  "Type","Cost","Lv","+++","Attribute");
    }

    // help function. show spell.
    private void showSpell(int start){
        printUtil.printInfoTableWithId("Spell", spell, start, "ItemName", "Type", "Cost","Lv","Damage","Mana");
    }

    // help function. show armor.
    private void showArmor(int start){
        printUtil.printInfoTableWithId("Armor", armor, start, "ItemName", "Type","Cost","Lv","damage--");
    }

    // help function. show weapon.
    private void showWeapon(int start){
        printUtil.printInfoTableWithId("Weapon", weapon, start, "ItemName", "Type", "Cost","Lv","Damage", "Hand");
    }

    public int[] getPos() {
        return pos;
    }

    private static String[][] initItemList(String infoString){
        String[][] info;
        String[] singleInfo = infoString.split("\n");
        info = new String[singleInfo.length][];
        for (int i = 0; i < info.length; i++) {
            info[i] = singleInfo[i].split(" +");
        }
        return info;
    }

    private static Item createItem(String[] info){
        switch (info[1]){
            case "Potion" ->{
                return new Potion(info);
            }
            case "Armor" ->{
                return new Armor(info);
            }
            case "Weapon" ->{
                return new Weapon(info);
            }
            case "IceSpell" ->{
                return new IceSpell(info);
            }
            case "FireSpell" ->{
                return new FireSpell(info);
            }
            case "LightningSpell" ->{
                return new LightningSpell(info);
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public String getColor() {
        return MyFont.ANSI_BACKGROUNDCYAN;
    }
}
