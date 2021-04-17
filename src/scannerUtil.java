import java.util.Scanner;

/**
 * a util to parse in user input
 */
public class scannerUtil {

    private static Scanner sc;

    static{
        sc = new Scanner(System.in);
    }

    public static int readInt(){
//        SoundPlayUtil.playInput();
        int num;
        while(true){
            String input = sc.nextLine();
            try{
                num = Integer.parseInt(input);
            }catch(NumberFormatException e){
                SoundPlayUtil.playError();
                printUtil.printRed("Please enter a valid integer!");
                continue;
            }
            break;
        }
        return num;
    }

    public static String readLine(){
//        SoundPlayUtil.playInput();
        return sc.nextLine().trim();
    }

    public static String readLine(String pattern){
//        SoundPlayUtil.playInput();
        String s = sc.next(pattern);
        sc.nextLine();
        return s.trim();
    }
    
}
