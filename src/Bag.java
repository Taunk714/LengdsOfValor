import java.util.ArrayList;
import java.util.HashMap;


// Bag class. Every Hero has a Bag. Bag stores the items hero have.
public class Bag {
    private final ArrayList<Item>[] items = new ArrayList[4];
    private static HashMap<String, Integer> itemId = new HashMap<>();
    private static String[] reverseId = new String[]{"Armory", "Potions", "Spell", "Weaponry"};
    private static int DEFAULT_SIZE = 20;
    private static int MAX_SIZE = 40;
    private int volume = DEFAULT_SIZE;
    private int used = 0;
    private Hero hero;

    static {
        itemId.put("Armory", 0);
        itemId.put("Potions", 1);
        itemId.put("Spell", 2);
        itemId.put("Weaponry", 3);
        itemId.put("IceSpells", 2);
        itemId.put("LightningSpells", 2);
        itemId.put("FireSpells", 2);
    }

    public Bag(Hero hero) {
        for (int i = 0; i < items.length; i++) {
            items[i] = new ArrayList<>();
        }
        this.hero = hero;
    }

    public int getVolume() {
        return volume;
    }

    public int getUsed(){
        return used;
    }

    public int getAvailable(){
        return volume - used;
    }

    public Item takeItem(String type, int index){
        Item item = getItem(type, index);
//        item.use();
        removeItem(item);
        used--;
        return item;
    }

//    public void takeItem(int index){
//        Item item = getItem(index);
//        item.use();
//        removeItem(item);
//        used--;
//    }

    public void addItem(Item item){
        used++;
        items[itemId.get(item.getType())].add(item);
    }

    public void removeItem(Item item){
        items[itemId.get(item.getType())].remove(item);
    }

    public void removeItem(int itemId){
        if (itemId >= used){
            throw new IndexOutOfBoundsException();
        }else{
            for (ArrayList<Item> item : items) {
                if (itemId < item.size()) {
                    item.remove(itemId);
                    used--;
                } else {
                    itemId -= item.size();
                }
            }
        }
    }

    public Item getItem(int id){
        if (id >= used){
            throw new IndexOutOfBoundsException();
        }else{
            for (ArrayList<Item> item : items) {
                if (id < item.size()) {
                    return item.get(id);
                } else {
                    id -= item.size();
                }
            }
        }
        return null;
    }

    public Item getItem(String type, int id){
        if (id >= used){
            throw new IndexOutOfBoundsException();
        }else{
            return items[itemId.get(type)].get(id);
        }
    }


    public void printItems(){
        System.out.printf("%s%s's bag. Volume: %d/%d%s\n",
                MyFont.ANSI_BACKGROUNDWHITE, hero.toString(), used, volume, MyFont.ANSI_RESET);
        int start = 0;
        if (items[itemId.get("Armory")].size() > 0){
            showArmor(start);
        }
        start += items[itemId.get("Armory")].size();
        if (items[itemId.get("Potions")].size() > 0) {
            showPotion(start);
        }
        start += items[itemId.get("Potions")].size();
        if (items[itemId.get("Spell")].size() > 0) {
            showSpell(start);
        }

        start += items[itemId.get("Spell")].size();
        if (items[itemId.get("Weaponry")].size() > 0) {
            showWeapon(start);
        }
    }

    public int getItemNum(String type){
        return items[itemId.get(type)].size();
    }

    public boolean hasItem(String type){
        return items[itemId.get(type)].size() > 0;
    }

    public void showSpell(int start){
        printUtil.printObjectInfoTableWithId("Spell",items[itemId.get("Spell")], start, "ItemName", "Type", "Cost","Lv","Damage","Mana");
    }

    public void showPotion(int start){
        printUtil.printObjectInfoTableWithId("Potions", items[itemId.get("Potions")], start, "ItemName",  "Type","Cost","Lv","+++","Attribute");

    }

    public void showArmor(int start){
        printUtil.printObjectInfoTableWithId("Armory", items[itemId.get("Armory")], start, "ItemName", "Type","Cost","Lv","damage--");
    }

    public void showWeapon(int start){
        printUtil.printObjectInfoTableWithId("Weaponry", items[itemId.get("Weaponry")], start, "ItemName", "Type", "Cost","Lv","Damage", "Hand");
    }

    public boolean canUseSpell(){
        for (Item spell:items[itemId.get("Spell")]) {
            if (((Spell)spell).getManaCost() <= hero.getMana()){
                return true;
            }
        }
        return false;
    }

}
