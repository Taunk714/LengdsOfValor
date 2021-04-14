import java.util.*;
// game.Hero class.
public class Hero extends Character implements Fight{
    private int exp;
    private int attack;
    private int mana;
    private final Bag bag = new Bag(this);
    private final Skill strength;
    private final Skill agility;
    private final Skill dexterity;
    private int money;
    private int maxHP;
    private int maxMana;
    private Armor armor;
    private ArrayList<Weapon> weapons = new ArrayList<>();
    private String type;

//    private int[] pos = new int[2];

    Hero(String[] heroData, String type){
        setName(heroData[1]);
        mana = maxMana = Integer.parseInt(heroData[2]);
        strength = new Skill("strength", Integer.parseInt(heroData[3]));
        agility = new Skill("agility", Integer.parseInt(heroData[4]));
        dexterity = new Skill("dexterity", Integer.parseInt(heroData[5]));
        money = Integer.parseInt(heroData[6]);
        exp = Integer.parseInt(heroData[7]);
        this.type = type;
        setLevel(1);
        maxHP = getLevel() * 100;
        setHP(maxHP);
    }

    // ask and get item of a specific type.
    public void useItem(String type){
        switch (type){
            case "Armory"->{
                bag.showArmor(0);
            }
            case "Spell"->{
                bag.showSpell(0);
            }
            case "Weaponry"->{
                bag.showWeapon(0);
            }
            case "Potions"->{
                bag.showPotion(0);
            }
        }
        int size = bag.getItemNum(type);
        int id;

        System.out.printf("Enter the %s you want to use\n", type);
        while (true){
            try{
                Scanner scanner = new Scanner(System.in);
                id = scanner.nextInt();
                if (id< 0 || id > size-1){
                    System.out.println("Please enter 0 to "+ (size-1)+":");
                }else {
                    break;
                }
            }catch (InputMismatchException e){
                System.out.println("Please enter 0 to "+ (size - 1)+":");
            }
        }
        Item item = bag.takeItem(type,id);
        item.use();
    }

    // ask and get an attacking item of a specific type to attack a specific monster.
    public void useItem(String type, Monster monster){
        bag.showSpell(0);
        int size = bag.getItemNum(type);
        String ans;
        int id;
        System.out.println("Enter the spell you want to use.");
        while (true){
            try{
                Scanner scanner = new Scanner(System.in);
                ans = scanner.next("[0-"+(size-1)+"mqi]");
                if (ans.equalsIgnoreCase("i")){
                    LOVGame.getInstance().showInfo();
                }else if (ans.equalsIgnoreCase("m")){
                    LOVGame.getInstance().printMap();
                }else if (ans.equalsIgnoreCase("q")){
                    LOVGame.getInstance().printEndGame();
                    System.exit(-1);
                }else {
                    id = Integer.parseInt(ans);
                    break;
                }
            }catch (InputMismatchException e){
                System.out.println("Please enter 0 to "+ (size - 1)+":");
            }
        }
        Item item = bag.takeItem(type,id);
        if (item instanceof Spell){
            ((Spell)item).setTarget(monster);
        }
        item.use();
    }

    // buy item. called by market cell.
    public void buyItem(Item item){
        if (item.getCost() > money){
            System.out.println(toString() + " doesn't have enough money! Transaction canceled!");
        }else if (item.getLevel() > getLevel()) {
            System.out.println(toString() + " doesn't reach required level! Transaction canceled!");
        }else{
            bag.addItem(item);
            item.setHero(this);
            money -= item.getCost();
            System.out.println(toString() +" bought the "+ item.getName() +
                    " and cost $" + item.getCost() +". Now has $" + money);
        }

    }


    // sell item. callled by market cell.
    public void sellItem(Item item){
        bag.removeItem(item);
        money += item.getCost()/2;
    }

    public Item getItem(int itemId){
        return bag.getItem(itemId);
    }

    // return the number of items hero have
    public int getItemNum(){
        return bag.getUsed();
    }

    /**
     * When all heroes died,they can choose to revive. After revive, hero will have 1/2 hp.
     */
    public void revive(boolean isDead){
        if (isDead){
            exp = 0;
        }
        setHP(maxHP/2);
        mana = maxMana/2;
        System.out.println(toString() + " is revived!");
    }

    public void recover(){
        setHP(getHP() + maxHP/10);
        setMana(mana + maxMana/10);
        System.out.printf("%s's HP&Mana recover. HP: %d, Mana: %d", toString(),getHP(), getMana());
    }

    @Override
    public void setHP(int HP) {
        super.setHP(Math.min(HP, maxHP));
    }

    public void setMana(int mana) {
        this.mana = Math.min(mana, maxMana);
    }

    // gain exp and money after winning the battle.
    public void gainAndLevelUp(int exp, int money){
        this.exp += exp;
        this.money += money;
//        System.out.printf("%s got $%d and %d EXP.\n", toString(), money, exp);
        while (this.exp >= calStdExp(getLevel())){
            this.exp = this.exp - calStdExp(getLevel());
            levelUp();
        }
        System.out.printf("Now %s is Lv.%d and has $%d. Current HP: %d, Mana: %d, EXP: %d/%d\n",
                toString(), getLevel(), this.money, getHP(), getMana(), getExp(), calStdExp(getLevel()));
    }

    // all attributes level up
    private void levelUp(){
        setLevel(getLevel()+1);
        maxHP = getLevel() * 100;
        maxMana *= 1.1;
        setHP(maxHP);
        agility.levelUp();
        dexterity.levelUp();
        strength.levelUp();
        System.out.printf("%s level up! Level: %d, Strength: %d, Agility: % d, Dexterity: %d\n",
                toString(), getLevel(), strength.getValue(), agility.getValue(), dexterity.getValue());
    }

    // calculate the exp upperbound of a certain level.
    private int calStdExp(int level){
        return level * 10;
    }

    public void showBag(){
        bag.printItems();
    }

    public int getMoney() {
        return money;
    }

    public int getExp() {
        return exp;
    }

    public int getMana() {
        return mana;
    }

    public Armor getArmor() {
        return armor;
    }

    // put on armor
    public void setArmor(Armor armor) {
        this.armor = armor;
    }

    // change weapon. check the weapon in the hand, if the hero can't hold all the weapon,
    // then put down the old and take the new one.
    public void setWeapons(Weapon weapon) {
        if (weapons.size() == 0){
            weapons.add(weapon);
            System.out.printf("%s holds %s now\n", toString(), weapon.getName());
        }else if (weapons.size() == 1){
            if (weapons.get(0).getHand() == 2){
                System.out.printf("%s puts down %s and holds %s now!\n",toString(),
                        weapons.get(0).getName(), weapon.getName());
                bag.addItem(weapons.get(0));
                weapons.set(0, weapon);
            }else if (weapon.getHand() > 2){
                System.out.printf("Weapon %s needs 2 hands. %s puts down %s and holds %s now!\n",
                        weapon.getName(),toString(), weapons.get(0).getName(), weapon.getName());
                bag.addItem(weapons.get(0));
                weapons.set(0, weapon);
            }else{
                weapons.add(weapon);
                System.out.printf("Take %s. %s now hold %s and %s\n",
                        weapon.getName(), toString(), weapons.get(0).getName(), weapons.get(1).getName());
            }
        }else{
            if (weapon.getHand() == 2){
                System.out.printf("Weapon %s needs 2 hands. %s puts down %s and %s, holds %s now!\n",
                        weapon.getName(), toString(), weapons.get(0).getName(),weapons.get(1).getName(), weapon.getName());
            }else{
                System.out.println("You already have two weapons in the hand. You must choose one to put down:");
                System.out.printf("0: %s, damage: %s\n",weapons.get(0).getName(),weapons.get(0).getDamage());
                System.out.printf("1: %s, damage: %s\n",weapons.get(1).getName(),weapons.get(1).getDamage());
                System.out.printf(MyFont.ANSI_GREY + "(Default is 0)"+ MyFont.ANSI_RESET);

                int id = 0;
                try {
                    Scanner scanner = new Scanner(System.in);
                    id = Integer.parseInt(scanner.next("[01]"));
                }catch (NoSuchElementException e){
                    System.out.println("Bad Input. Use the default value 0.");
                }
                System.out.printf("Put down %s and take %s! Now hold %s and %s\n",
                        weapons.get(id).getName(), weapon.getName(), weapons.get(1-id).getName(), weapon.getName());
                bag.addItem(weapons.get(id));
                weapons.set(id, weapon);
            }
        }
    }

    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    public String showWeapon(){
        if (weapons.size() == 1){
            return weapons.get(0).getName();
        }else if (weapons.size() == 2){
            return weapons.get(0).getName() + "&" +weapons.get(1).getName();
        }else {
            return "";
        }
    }

    public String showArmorWorn(){
        if (armor == null){
            return "";
        }else{
            return armor.getName();
        }
    }

    public void printHeroDeadInfo(){
        System.out.printf("%s is faint.%s HP: %d%s, Level: %d\n",
                toString(), MyFont.ANSI_RED, getHP(), MyFont.ANSI_RESET, getLevel());
    }

    public void attack(Monster monster, int attackWay){
        switch (attackWay){
            case 1 ->{ // regular attack
                int damage = strength.getValue();
                for (int i = 0; i < weapons.size(); i++) {
                    damage += weapons.get(i).getDamage();
                }
                System.out.printf("%s attack %s\n", toString(), monster.toString());
                System.out.println(MyFont.ANSI_BACKGROUNDWHITE + "(╬￣皿￣)=○ =======> щ(ﾟДﾟщ)" + MyFont.ANSI_RESET);
                monster.hurt(damage);
            }
            case 2->{ // cast a spell
                System.out.printf("%s cast a spell on %s\n", toString(), monster.toString());
                System.out.println(MyFont.ANSI_BACKGROUNDWHITE + "( *￣▽￣)o ─═≡※:☆( >﹏<。)" + MyFont.ANSI_RESET);
                useItem("Spell", monster);
            }
            case 3 ->{ // use a potion
                System.out.printf("%s use a potion\n", toString());
                System.out.println(MyFont.ANSI_BACKGROUNDWHITE + "ԅ( ¯་། ¯ԅ)" + MyFont.ANSI_RESET);
                useItem("Potions");
            }
            case 4 ->{ // change armor
                System.out.printf("%s wants to change the armor\n", toString());
                System.out.println(MyFont.ANSI_BACKGROUNDWHITE + "＼(＠＾０＾＠)/" + MyFont.ANSI_RESET);
                useItem("Armory");
            }
            case 5 ->{ // change weapon
                System.out.printf("%s wants to change the weapon\n", toString());
                System.out.print(MyFont.ANSI_BACKGROUNDWHITE + "(҂‾ ▵‾)︻デ═一" + MyFont.ANSI_RESET);
                useItem("Weaponry");
            }
        }
    }

    public void hurt(int damage){
//        Random rnd = new Random();
        if (randomUtil.nextInt(100000) < agility.getValue() * 2){
            System.out.println("Miss!ヽ(ﾟ∀ﾟ)ﾒ(ﾟ∀ﾟ)ﾉ\n");
        }else{
            int d = (damage - getArmorBuff()/2)/3;
            setHP(getHP()-Math.max(0, d));
            System.out.printf("%s's HP -%d. Current HP: %d\n\n" , toString(),Math.max(0, d), getHP());
            if (getHP() <= 0){
                System.out.println(MyFont.ANSI_RED + MyFont.ANSI_BOLD + getName() +  " died." + MyFont.ANSI_RESET);
            }
        }
    }

    private int getArmorBuff(){
        if (armor == null){
            return 0;
        }else {
            return armor.getDamageReduction();
        }
    }

    public String getType() {
        return type;
    }

    public Bag getBag() {
        return bag;
    }

    public int getStrengthVal() {
        return strength.getValue();
    }

    public int getAgilityVal() {
        return agility.getValue();
    }

    public int getDexterityVal() {
        return dexterity.getValue();
    }

    public Skill getStrength() {
        return strength;
    }

    public Skill getAgility() {
        return agility;
    }

    public Skill getDexterity() {
        return dexterity;
    }

    public void setStrength(int strength) {
        this.strength.setValue(strength);
    }

    public void setAgility(int agility) {
        this.agility.setValue(agility);
    }

    public void setDexterity(int dexterity) {
        this.dexterity.setValue(dexterity);
    }

    @Override
    public String[] getAttrString() {
        //"Type","Name","Lv","HP","Mana", "EXP","Money", "Strength", "Agility", "Dexterity"
        return new String[]{ getType(),getName(),
                String.valueOf(getLevel()), String.valueOf(getHP()), String.valueOf(getMana()), String.valueOf(exp), String.valueOf(getMoney()),
                String.valueOf(strength.getValue()), String.valueOf(agility.getValue()), String.valueOf(dexterity.getValue()),
                showWeapon(),showArmorWorn()};
    }

    @Override
    public String toString() {
        return MyFont.ANSI_BLUE + type + " " + getName() + MyFont.ANSI_RESET;
    }

    public String mark(){
        return "◢◣ ";
    }
}
