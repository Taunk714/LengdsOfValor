import java.util.Random;
import java.util.Scanner;

// for Legends
public class CommonCell implements Cell{
    private static final int battleProb;
    static {
        battleProb = configUtil.getConfigInt("fightProb");
    }

    private int[] pos = new int[2];
    public CommonCell(int row, int col) {
        pos[0] = row;
        pos[1] = col;
    }

    public void enter(Team<Hero> heroes){
        heroes.setPos(new int[]{pos[0],pos[1]});
        int rnd = rollDice();
        if (rnd < battleProb){
            new Battle(heroes);
        }else{
            System.out.println("A Safe Cell.");

            while(true){
                String ans;
                try {
                    System.out.println("""
                    a: change armor
                    w: change weapon
                    m: show map
                    i: show information
                    q: quit the game
                    else: Leave the cell""");
                    Scanner scanner = new Scanner(System.in);
                    ans = scanner.next("[imqaw]");
                    if (ans.equalsIgnoreCase("i")){
                        Legends.getInstance().showInfo();
                    }else if (ans.equalsIgnoreCase("m")){
                        Legends.getInstance().printMap();
                    }else if (ans.equalsIgnoreCase("q")){
                        Legends.getInstance().printEndGame();
                        System.exit(-1);
                    }else if (ans.equalsIgnoreCase("a")){
                        System.out.println("Who wants to change?");
                        boolean hasEquip = false;
                        int id = 0;
                        for (int i = 0; i < heroes.getTeamSize(); i++) {
                            if (heroes.getMember(i).getBag().hasItem("Armory")){
                                System.out.println(i+": "+heroes.getMember(i).getName());
                                id = id * 10 + i;
                                hasEquip = true;
                            }
                        }
                        if (!hasEquip){
                            System.out.println("No one have any armor.");
                            return;
                        }
                        String get;
                        int heroId;
                        while (true){
                            try {
                                Scanner scanner1 = new Scanner(System.in);
                                get = scanner1.next("["+id+"q]");
                                if (get.equalsIgnoreCase("q")){
                                    break;
                                }
                                heroId = Integer.parseInt(get);
                                heroes.getMember(heroId).attack(null, 4);
                            } catch (NumberFormatException e) {
                                System.out.println("Please enter correct number.");
                            }
                        }

                    }else if (ans.equalsIgnoreCase("w")){
                        System.out.println("Who wants to change?");
                        boolean hasEquip = false;
                        int id = 0;
                        for (int i = 0; i < heroes.getTeamSize(); i++) {
                            if (heroes.getMember(i).getBag().hasItem("Weaponry")){
                                System.out.println(i+": "+heroes.getMember(i).getName());
                                id = id * 10 + i;
                                hasEquip = true;
                            }
                        }
                        if (!hasEquip){
                            System.out.println("No one have any weapon.");
                            return;
                        }
                        String get;
                        int heroId;
                        try {
                            Scanner scanner1 = new Scanner(System.in);
                            get = scanner1.next("["+id+"q]");
                            if (get.equalsIgnoreCase("q")){
                                break;
                            }
                            heroId = Integer.parseInt(get);
                            heroes.getMember(heroId).attack(null, 5);
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter correct number");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Leaving the cell");
                    return;
                }
            }
        }
    }


    /**
     * Help Function.
     * @return the random number.
     */
    private int rollDice(){
        return randomUtil.nextInt(100);
    }

    public int[] getPos() {
        return pos;
    }



    @Override
    public String toString() {
        return "   ";
    }

    @Override
    public int getRow() {
        return pos[0];
    }

    @Override
    public int getCol() {
        return pos[1];
    }

    @Override
    public String getColor() {
        return MyFont.ANSI_RESET;
    }
}
