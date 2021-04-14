import java.util.Random;

/**
 * a util to generate random integer with or without given bound
 */
public class randomUtil{

    private static Random rand;

    static{
        rand = new Random();
    }

    public static int nextInt(){
        return rand.nextInt();
    }

    public static int nextInt(int bound){
        return rand.nextInt(bound);
    }

    public static double nextDouble(){
        return (rand.nextInt(10000)*0.01);
    }

}