import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * a util to print with colors in the console
 */
public class printUtil {
    private static HashMap<String, Integer> occId = new HashMap<>();
    static {
        occId.put("Warrior", 0);
        occId.put("Sorcerer", 1);
        occId.put("Paladin", 2);
    }
    
    public static void printRed(String s){
        System.out.println("\u001b[31m"+s+"\u001b[0m");
    }

    public static void printYellow(String s){
        System.out.println("\u001b[33m"+s+"\u001b[0m");
    }

    public static void printYellowSign(String s){
        System.out.print("\u001b[33m"+s+"\u001b[0m");
    }


    public static void printGreen(String s){
        System.out.println("\u001b[32m"+s+"\u001b[0m");
    }

    public static void printBlue(String s){
        System.out.println("\u001b[34m"+s+"\u001b[0m");
    }

    public static void printWhiteBackground(String s){
        System.out.print("|  "+s+"  |");
    }

    public static void printGreenBackground(String s){
        System.out.print("|\u001b[42m  "+s+"  \u001b[0m|");
    }

    public static void printBlueBackground(String s){
        System.out.print("|\u001b[44m  "+s+"  \u001b[0m|");
    }

    // public static void printBackground(String s){
    //     System.out.print("|\u001b[44m  "+s+"  \u001b[0m|");
    // }

    public static void printYellowBackground(String s){
        System.out.print("|\u001b[43m  "+s+"  \u001b[0m|");
    }

    public static void printBlackBackground(String s){
        System.out.print("|\u001b[40m  "+s+"  \u001b[0m|");
    }

    public static void printRedBackground(String s){
        System.out.println("\u001b[41m"+s+"\u001b[0m");
    }

    public static void print(String s){
        System.out.println(s);
    }

    public static void printBoundary(){
        System.out.println("--------------------------------------------------------");
    }

    public static void printDotBoundary(String s){
        System.out.println("+ + + + + + + + + + + + " + s+" + + + + + + + + + + + +");
    }

    public static void printWithBoundary(String s){
        System.out.println();
        System.out.println("------------------------"+s+"---------------------");
    }

    public static void print(){
        System.out.println();

    }

    public static void println(String s){
        System.out.println(s);

    }

    public static void printSingle(String s){
        System.out.print(s);

    }

    

    public static void printOpt(String s){
        System.out.print("("+s+") ");
    }

    public static void printError(String s){
        printRed("ERROR INPUT: "+s);

    }

    public static void printHeroDamage(String heroName, String monsterName,int damage){
        printGreenBackground("Hero");
        printSingle(String.format(" %s  -----%d damage---->  ",heroName, damage));
        printBlueBackground("Monster ");
        printSingle(monsterName);
        print();
        print();

    }

    public static void printMonsterDamage(String monsterName, String heroName,int damage){
        printBlueBackground("Monster");
        printSingle(String.format(" %s  -----%d damage---->  ",monsterName, damage));
        printBlueBackground("Hero ");
        printSingle(heroName);
        print();
        print();

    }

    public static void printOccInfoTable(String infoName,String[][] info, int start, String... colName){
        StringBuilder s = new StringBuilder();
        StringBuilder sLine = new StringBuilder();
        int[] tableSize = new int[colName.length+1];
        tableSize[0] = configUtil.getConfigInt("id");
        for (int i = 1; i < tableSize.length; i++) {
            tableSize[i] = configUtil.getConfigInt(colName[i-1]);
        }
        s.append("|%") ;
        sLine.append("+");
        for (int j : tableSize) {
            s.append(j).append("s|%");
            sLine.append("-".repeat(j)).append("+");
        }
        s.deleteCharAt(s.length()-1);
        s.append("\n");
        System.out.println(MyFont.ANSI_BOLD+ MyFont.ANSI_BACKGROUNDWHITE+" ".repeat((sLine.length()-infoName.length())/2)+
                infoName+ " ".repeat(sLine.length()-infoName.length() - (sLine.length()-infoName.length())/2) + MyFont.ANSI_RESET);
        System.out.println(sLine.toString());
//        Name/cost/required level/attribute increase/attribute affected
        System.out.printf(s.substring(0, 4),"ID");
        System.out.printf(s.substring(4),colName);
        System.out.println(sLine);
        HashSet<Integer> chosenHero = HeroCreator.getInstance().getChosenHero();
        int id = occId.get(infoName);
        int targetLen = info[0].length;
        String[] input = new String[targetLen+1];
        for (int i = 0; i < info.length; i++) {
            String[] target = info[i];
            input[0] = (start+i)+"";
            if (chosenHero.contains(id*10 + i)){
                System.out.print(MyFont.ANSI_DELETE);
            }
            System.arraycopy(target, 0, input, 1, input.length - 1);
            System.out.printf(s.toString(),input);
            System.out.print(MyFont.ANSI_RESET);
        }
        System.out.println(sLine.toString());
    }

    public static void printInfoTableWithId(String infoName,String[][] info, int start, String... colName){
        int isLive = -1;
        for (int i = 0; i < colName.length; i++) {
            if (colName[i].equalsIgnoreCase("hp")){
                isLive = i;
            }
        }
        StringBuilder s = new StringBuilder();
        StringBuilder sLine = new StringBuilder();
        int[] tableSize = new int[colName.length+1];
        tableSize[0] = configUtil.getConfigInt("id");
        for (int i = 1; i < tableSize.length; i++) {
            tableSize[i] = configUtil.getConfigInt(colName[i-1]);
        }
        s.append("|%") ;
        sLine.append("+");
        for (int j : tableSize) {
            s.append(j).append("s|%");
            sLine.append("-".repeat(j)).append("+");
        }
        s.deleteCharAt(s.length()-1);
        s.append("\n");
        System.out.println(MyFont.ANSI_BOLD+ MyFont.ANSI_BACKGROUNDWHITE+" ".repeat((sLine.length()-infoName.length())/2)+
                infoName+ " ".repeat(sLine.length()-infoName.length() - (sLine.length()-infoName.length())/2) + MyFont.ANSI_RESET);
        System.out.println(sLine.toString());
//        Name/cost/required level/attribute increase/attribute affected
        System.out.printf(s.substring(0, 4),"ID");
        System.out.printf(s.substring(4),colName);
        System.out.println(sLine);
        int targetLen = info[0].length;
        String[] input = new String[targetLen+1];
        for (int i = 0; i < info.length; i++) {
            String[] target = info[i];
            input[0] = (start+i)+"";
            if (isLive >= 0 && Integer.parseInt(target[isLive]) <= 0){
                System.out.print(MyFont.ANSI_DELETE+MyFont.ANSI_GREY);
            }
            System.arraycopy(target, 0, input, 1, input.length - 1);
            System.out.printf(s.toString(),input);
            System.out.print(MyFont.ANSI_RESET);
        }
        System.out.println(sLine.toString());
    }

    public static void printObjectInfoTableWithId(String infoName, List<? extends Tablefiable> info, int start, String... colName){
        String[][] sinfo = new String[info.size()][];
        for (int i = 0; i < sinfo.length; i++) {
            sinfo[i] = info.get(i).getAttrString();
        }
        printInfoTableWithId(infoName, sinfo, start, colName);
    }
}
