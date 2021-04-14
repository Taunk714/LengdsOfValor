import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

// game.HeroCreator class. It's a singleton. One game only have one game.HeroCreator. If the hero was chosen, he can't be chosen anymore.
public class HeroCreator {
    private static final String parent = "/Hero";
    private static final String[] heroTypeName = new String[]{"Paladin","Sorcerer","Warrior"};

    private static final HashMap<String, ArrayList<String[]>> heroList = new HashMap<>();

    static {
        initHeroList();
    }

    private static HeroCreator instance = new HeroCreator();
    private HeroCreator(){}

    public static HeroCreator getInstance(){
        return instance;
    }

    private HashSet<Integer> chosenHero = new HashSet<>();

    public Hero createHero(String occupation, int id){
        if (occupation.equalsIgnoreCase("warrior")){
            return new Warrior(heroList.get(occupation).get(id));
        }else if (occupation.equalsIgnoreCase("sorcerer")){
            return new Sorcerer(heroList.get(occupation).get(id));
        }else if (occupation.equalsIgnoreCase("paladin")){
            return new Paladin(heroList.get(occupation).get(id));
        }
        return null;
    }

    // show all the heroes of specific occupation.
    public void showOccInfo(String occupation){
        String[][] target = heroList.get(occupation).toArray(new String[0][]);
        System.out.println("Here are all the "+ occupation.toLowerCase() +"s you can choose");
        printUtil.printOccInfoTable(occupation, target,0, "Type","Name","Mana","Strength","Agility","Dexterity", "Money","EXP");
    }

    // return the number of heroes of a specific type.
    public int getHeroNum(String type){
        return heroList.get(type).size();
    }


    // Solve the text, and split into string[]. Objects are created using String[] data.
    private static void initHeroList(){
        for (String s : heroTypeName) {
            String path = configUtil.getConfig("dataSrc") +parent+ "/" +s+"s.txt";
            try (RandomAccessFile raf = new RandomAccessFile(
                    path, "r")){
                raf.readLine();
                String line;
                while((line = raf.readLine()) != null){
                    String[] info = (s + " " + line).replaceAll("\t"," ").split(" +");
                    if (heroList.containsKey(s)) {
                        heroList.get(s).add(info);
                    } else {
                        heroList.put(s, new ArrayList<>());
                        heroList.get(s).add(info);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void markUsed(int id){
        chosenHero.add(id);
    }

    public HashSet<Integer> getChosenHero() {
        return chosenHero;
    }
}
