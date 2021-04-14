import java.util.Scanner;

/**
 * a util to parse in user input
 */
public class scannerUtil {

    private static Scanner sc;

    static{
        sc = new Scanner(System.in);
    }

    // public static String readLine(){
    //     return sc.nextLine();
    // }

    /**
     * @param instruction
     * @return user's integer input 
     */
    public static int readInt(String instruction){
        Integer num;
        while(true){
            printUtil.printYellow(instruction);
            String input = sc.nextLine();
            try{
                num = Integer.parseInt(input);
            }catch(NumberFormatException e){
                printUtil.printRed("Please enter a valid integer!");
                continue;
            }
            break;
        }
        return num;
    }

    public static String readLine(){
        return sc.nextLine().trim();
    }

    public static String readLine(String s){
        printUtil.printYellowSign(s);
        return sc.nextLine().trim();
    }
    
}
