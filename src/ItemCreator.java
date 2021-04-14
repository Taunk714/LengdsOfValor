import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

public class ItemCreator {
    private static final String parent = "/Item";
    private static final String[] itemTypeName = new String[]{"Armory","Potions","Weaponry","FireSpells", "IceSpells","LightningSpells"};

    private static final HashMap<String, ArrayList<String[]>> itemList = new HashMap<>();

    private static int totalSize = 0;

    static {
        initItemList();
    }

    // print the item list.
    public static void show(){
        System.out.println(MyFont.ANSI_BOLD + "ITEM LIST:" + MyFont.ANSI_RESET);
        int start = 0;
        showArmor(start);
        start += itemList.get("Armory").size();
        showPotion(start);
        start += itemList.get("Potions").size();
        showWeapon(start);
        start += itemList.get("Weaponry").size();
        showFireSpell(start);
        start += itemList.get("FireSpells").size();
        showIceSpell(start);
        start += itemList.get("IceSpells").size();
        showLightningSpell(start);


    }

    // help function. show potion.
    public static void showPotion(int start){
        printUtil.printInfoTableWithId("Potions", itemList.get("Potions").toArray(new String[0][]), start, "ItemName",  "Type","Cost","Lv","+++","Attribute");
    }

    // help function. show spell.
    public static void showIceSpell(int start){
        printUtil.printInfoTableWithId("IceSpell", itemList.get("IceSpells").toArray(new String[0][]), start, "Type","ItemName",  "Cost","Lv","Damage","Mana");
    }

    public static void showFireSpell(int start){
        printUtil.printInfoTableWithId("FireSpell", itemList.get("FireSpells").toArray(new String[0][]), start, "Type","ItemName",  "Cost","Lv","Damage","Mana");
    }

    public static void showLightningSpell(int start){
        printUtil.printInfoTableWithId("LightningSpell", itemList.get("LightningSpells").toArray(new String[0][]), start, "Type","ItemName",  "Cost","Lv","Damage","Mana");
    }

    // help function. show armor.
    public static void showArmor(int start){
        printUtil.printInfoTableWithId("Armory", itemList.get("Armory").toArray(new String[0][]), start, "Type","ItemName", "Cost","Lv","damage--");
    }

    // help function. show weapon.
    public static void showWeapon(int start){
        printUtil.printInfoTableWithId("Weaponry", itemList.get("Weaponry").toArray(new String[0][]), start, "Type", "ItemName", "Cost","Lv","Damage", "Hand");
    }


    private static void initItemList(){
        for (String s : itemTypeName) {
            String path = configUtil.getConfig("dataSrc") +parent+ "/" +s+".txt";
            try (RandomAccessFile raf = new RandomAccessFile(
                    path, "r")){
                raf.readLine();
                String line;
                while((line = raf.readLine()) != null){
                    String[] info = (s + " " + line).replaceAll("\t"," ").split(" +");
                    if (itemList.containsKey(s)) {
                        itemList.get(s).add(info);
                        totalSize++;
                    } else {
                        itemList.put(s, new ArrayList<>());
                        itemList.get(s).add(info);
                        totalSize++;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Item createItem(String type, int id){
        String[] info = itemList.get(type).get(id);
        switch (type){
            case "Potions" ->{
                return new Potion(info);
            }
            case "Armory" ->{
                return new Armor(info);
            }
            case "Weaponry" ->{
                return new Weapon(info);
            }
            case "IceSpells" ->{
                return new IceSpell(info);
            }
            case "FireSpells" ->{
                return new FireSpell(info);
            }
            case "LightningSpells" ->{
                return new LightningSpell(info);
            }
            default -> {
                return null;
            }
        }
    }

    public static int getTotalSize(){
        return totalSize;
    }

    public static String[] getItemInfo(int id) {
        for (String s : itemTypeName) {
            if(id < itemList.get(s).size()){
                return itemList.get(s).get(id);
            }
            id = id - itemList.get(s).size();
        }
        return null;
    }

    public static Item createItem(int id){
        for (String s : itemTypeName) {
            if(id < itemList.get(s).size()){
                return createItem(s,id);
            }
            id = id - itemList.get(s).size();
        }
        return null;
    }

}
