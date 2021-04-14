import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.*;
// game.Monster creator. Store all the monster info and create monster based on level.
public class MonsterCreator{
    private static final String parent = "/Monster";
    private static final String[] monsterTypeName = new String[]{"Dragon","Exoskeleton","Spirit"};

//    private static final String[][] monsterList = initMonsterList();
    private static final HashMap<Integer, ArrayList<String[]>> monsterListByLevel = new HashMap<>();

    static {
        initMonsterList();
    }

    public static Monster createMonsterByLevel(int level){
        Random rnd = new Random();
        ArrayList<String[]> monsters = monsterListByLevel.get(level);
        String[] target = monsters.get(rnd.nextInt(monsters.size()));
        switch (target[0]){
            case "Dragon"->{
                return new Dragon(target);
            }
            case "Exoskeleton"->{
                return new Exoskeleton(target);
            }
            case "Spirit"->{
                return new Spirit(target);
            }
        }
        return null;
    }


    private static void initMonsterList(){
        for (String s : monsterTypeName) {
            String path = configUtil.getConfig("dataSrc") +parent+ "/" +s+"s.txt";
            try (RandomAccessFile raf = new RandomAccessFile(
                    path, "r")){
                raf.readLine();
                String line;
                while((line = raf.readLine()) != null){
                    String[] info = (s + " " + line).replaceAll("\t"," ").split(" +");
                    int level = Integer.parseInt(info[2]);
                    if (monsterListByLevel.containsKey(level)) {
                        monsterListByLevel.get(level).add(info);
                    } else {
                        monsterListByLevel.put(level, new ArrayList<>());
                        monsterListByLevel.get(level).add(info);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
