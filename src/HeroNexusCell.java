import java.util.InputMismatchException;
import java.util.Scanner;

// market cell. Hero can buy or sell items here.
public class HeroNexusCell extends NexusCell<Hero>{

    private int[] pos = new int[2];
    public HeroNexusCell(int row, int col) {
        super(row, col);
    }

    @Override
    public void enter(Hero member) {
        super.enter(member);
        System.out.printf("%s enter the HeroNexusCell!\n",member);
        System.out.println("""
                Welcome to the SUPER LEGEND MARKET!
                Enter q to leave the market! Enter any other key to enter the market...""");

//        Scanner scanner = new Scanner(System.in);
        if ("q".equals(scannerUtil.readLine())){
            System.out.println("Entered q, leave the market!");
            return;
        }

        while(true){
            int buy = askBuyOrSell();
            switch (buy) {
                case 0->{
                    System.out.println("Leave the market.");
                    return;
                }
                case 1->{ // buy
                    buy(member);

                }
                case 2-> { // sell
                    if (member.getBag().getUsed() > 0){
                        System.out.println(member.getName());
                        member.showBag();
                        System.out.println("\n");
                        sell(member);
                    }else{
                        System.out.println("Nothing to sell.");
                        break;
                    }
                }
            }
        }
    }


    public void enter(Team<Hero> heroes){
        heroes.setPos(new int[]{pos[0], pos[1]});
        System.out.println("""
                Welcome to the SUPER LEGEND MARKET!
                \u001B[3mWhen one of the heroes want to sell/buy, first choose sell/buy, then choose the hero.\u001B[0m
                Enter q to leave the market! Enter any other key to enter the market...""");

//        Scanner scanner = new Scanner(System.in);
        if ("q".equals(scannerUtil.readLine())){
            System.out.println("Entered q, leave the market!");
            return;
        }

        while(true){
            int buy = askBuyOrSell();
            switch (buy) {
                case 0->{
                    System.out.println("Leave the market.");
                    return;
                }
                case 1->{ // buy
                    System.out.println("Who want to buy items?");
                    for (int i = 0; i < heroes.getTeamSize(); i++) {
                        System.out.println(i+": "+heroes.getMember(i).getName());
                    }

                    while (true){
                        try {
//                            Scanner scanner1 = new Scanner(System.in);
//                            buy(heroes.getMember(scanner1.nextInt()));
                            int id = scannerUtil.readInt();
                            buy(heroes.getMember(id));
                            break;
                        }catch (InputMismatchException e){
                            SoundPlayUtil.playError();
                            System.out.println("Incorrect format! You should enter a number. Please enter 0 to " + (heroes.getTeamSize()-1));
                        }catch (IndexOutOfBoundsException e){
                            SoundPlayUtil.playError();
                            System.out.println("Input out of bounds! Please enter 0 to " + (heroes.getTeamSize()-1));
                        }
                    }
                }
                case 2-> { // sell
                    System.out.println("Who want to sell items?");
                    boolean hasItem = false;
                    for (int i = 0; i < heroes.getTeamSize(); i++) {
                        if (heroes.getMember(i).getBag().getUsed() > 0){
                            System.out.println(i+": " + heroes.getMember(i).getName());
                            heroes.getMember(i).showBag();
                            System.out.println("\n");
                            hasItem = true;
                        }
                    }
                    if (!hasItem){
                        System.out.println("None of the heroes have any item. Nothing to sell.");
                        break;
                    }
                    System.out.println("Please enter the corresponding id:");

                    while (true) {
                        try {
//                            Scanner scanner1 = new Scanner(System.in);
//                            sell(heroes.getMember(scanner1.nextInt()));
                            int id = scannerUtil.readInt();
                            sell(heroes.getMember(id));
                            break;
                        } catch (InputMismatchException e) {
                            SoundPlayUtil.playError();
                            System.out.println("Incorrect format! You should enter a number. Please enter 1 to " + heroes.getTeamSize());
                        } catch (IndexOutOfBoundsException e) {
                            SoundPlayUtil.playError();
                            System.out.println("Input out of bounds! Please enter 1 to " + heroes.getTeamSize());
                        }
                    }
                }
            }
        }
    }

    // decide buy or sell
    private int askBuyOrSell(){

        String id;
//        Scanner scanner = new Scanner(System.in);
        while (true){
            try {
                System.out.println("Want to buy or sell?");
                System.out.println("1: Buy");
                System.out.println("2: Sell");
                System.out.println("0: leave the market");
                System.out.println("m: show the map");
                System.out.println("q: quit the game");
                System.out.println("i: show information");
                Scanner scanner = new Scanner(System.in);
                id = scanner.next("[012qmi]");
                if (id.equalsIgnoreCase("q")){
                    LOVGame.getInstance().printEndGame();
                    System.exit(-1);
                }else if (id.equalsIgnoreCase("m")) {
                    LOVGame.getInstance().printMap();
                }else if (id.equalsIgnoreCase("i")){
                    LOVGame.getInstance().showInfo();
                }else {
                    return Integer.parseInt(id);
                }
            }catch (InputMismatchException e){
                SoundPlayUtil.playError();
                System.out.println("Incorrect format! Please enter 1, 2 or 0.");
//                scanner.nextLine();
            }
        }
    }

    // buy item
    public void buy(Hero hero){
        System.out.println("Here's the item list:");
        ItemCreator.show();
        String id;
        String[] itemInfo;

        Scanner scanner = new Scanner(System.in);
        while(true){
            try {
                System.out.printf("%s has $%d, which item do you want? Enter the number:(Enter -1 to cancel)\n", hero.toString(), hero.getMoney());
                id = scanner.nextLine();
                if (id.equalsIgnoreCase("i")){
                    LOVGame.getInstance().showInfo();
                    continue;
                }else if (id.equalsIgnoreCase("m")){
                    LOVGame.getInstance().printMap();
                    continue;
                }else if (id.equalsIgnoreCase("q")){
                    LOVGame.getInstance().printEndGame();
                    System.exit(-1);
                }else if (id.equalsIgnoreCase("-1")){
                    break;
                }

                int val = Integer.parseInt(id);
                if (val >= 0 && val < ItemCreator.getTotalSize()){
                    itemInfo = ItemCreator.getItemInfo(val);
                }else{
                    SoundPlayUtil.playError();
                    System.out.println("Out of bounds.");
                    continue;
                }

                assert itemInfo != null;
                System.out.println("Item "+ itemInfo[1]+" is worth $"+itemInfo[2]+". Does Hero "+ hero.getName()+
                        " want to buy it? Enter y/Y to buy, enter other letter to cancel:");
                String ans = scanner.nextLine();
                if ("y".equalsIgnoreCase(ans)){
                    SoundPlayUtil.playBuyOrSell();
                    hero.buyItem(ItemCreator.createItem(val));
                }else if (id.equalsIgnoreCase("i")){
                    Legends.getInstance().showInfo();
                    continue;
                }else if (id.equalsIgnoreCase("m")){
                    Legends.getInstance().printMap();
                    continue;
                }else if (id.equalsIgnoreCase("q")){
                    Legends.getInstance().printEndGame();
                    System.exit(-1);
                }else {
                    System.out.println(hero.toString()+" canceled the transaction!");
                }
                System.out.println("\n");
                break;
            }catch (InputMismatchException e){
                SoundPlayUtil.playError();
                System.out.println("Incorrect format! Please enter a number:");
                scanner.nextLine();
            }catch (IndexOutOfBoundsException e) {
                SoundPlayUtil.playError();
                System.out.println("Out of bounds. Please enter 0 to " + (ItemCreator.getTotalSize() - 1));
                scanner.nextLine();
            }catch (NumberFormatException e){
                SoundPlayUtil.playError();
                System.out.println("Incorrect format! Please enter a number:");
            }
        }
    }

    // sell item
    public void sell(Hero hero){
        if (hero.getBag().getUsed() == 0){
            SoundPlayUtil.playError();
            System.out.printf("Sorry %s doesn't have items to sell", hero.toString());
        }
        System.out.println("Here are the items "+ hero.getName() + " have:");
        hero.showBag();
        System.out.printf("Which item do %s want to sell?(The price is half of the buying price) \nEnter the number:\n", hero.toString());
        int id = 0;
        Item item;
        Scanner scanner = new Scanner(System.in);
        while(true){
            try {
                id = scanner.nextInt();
                item = hero.getItem(id);
                System.out.println("Item " + item.getName() + " is worth $" + (item.getCost()/2)+
                        ", do you want to sell it? Enter y/Y to sell, enter other letter to cancel:");
                String ans = scanner.nextLine();
                if ("y".equals(ans.toLowerCase())){
                    SoundPlayUtil.playBuyOrSell();
                    hero.sellItem(item);
                    System.out.println(hero.toString()+" sold the "+ item.getName() +
                            " and gain $" + item.getCost()/2 +". Now has $" + hero.getMoney());
                }else if (ans.equalsIgnoreCase("i")){
                    Legends.getInstance().showInfo();
                    continue;
                }else if (ans.equalsIgnoreCase("m")){
                    Legends.getInstance().printMap();
                    continue;
                }else if (ans.equalsIgnoreCase("q")){
                    Legends.getInstance().printEndGame();
                    System.exit(-1);
                }else {
                    System.out.println(hero.toString()+" canceled the transaction!");
                }
                System.out.println("\n");
                break;
            }catch (InputMismatchException e){
                SoundPlayUtil.playError();
                System.out.println("Incorrect format! Please enter a number:");
                scanner.nextLine();
            }catch (IndexOutOfBoundsException e){
                SoundPlayUtil.playError();
                System.out.println("Out of bounds. Please enter 0 to " + (hero.getItemNum()));
                scanner.nextLine();
            }
        }
    }

    public int[] getPos() {
        return pos;
    }


    @Override
    public String getColor() {
        return MyFont.ANSI_BACKGROUNDCYAN;
    }
}
