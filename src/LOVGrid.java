import java.util.Random;

public class LOVGrid implements Grid<Character>{
    private final Cell[][] gridArea;
    private int size;
    private static final double specialPct = configUtil.getConfigInt("specialPct")/100.0;
    private static final double plainPct = configUtil.getConfigInt("plainPct")/100.0;



    LOVGrid(){
        System.out.println("Initializing grid......");
        size = configUtil.getConfigInt("boardSize");
        gridArea = new Cell[size][size];
        instantiateCell();
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Cell getCell(int row, int col) {
        return gridArea[row][col];
    }

    @Override
    public Cell getCell(int[] pos) {
        return gridArea[pos[0]][pos[1]];
    }

    @Override
    public void enter(Character member, int[] pos) {
        Cell cell = gridArea[pos[0]][pos[1]];
        if (cell instanceof AccessibleCell){
            int[] prePos = member.getPos();
            Cell preCell = gridArea[prePos[0]][prePos[1]];
            if (member instanceof Monster){
                ((AccessibleCell<Character>)preCell).setMonsterNull();
            }else if(member instanceof Hero){
                ((AccessibleCell<Character>)preCell).setHeroNull();
            }
            ((AccessibleCell<Character>)cell).enter(member);
        }else{
            System.out.println(MyFont.ANSI_RED + MyFont.ANSI_BOLD + "You can't go here. It's inaccessible." + MyFont.ANSI_RESET);
        }
    }

    private void instantiateCell(){
        int total = size * size - size * 4 + 4;
        for (int i = 0; i < size; i++) {
            gridArea[i][2] = new InaccessibleCell(i,2);
            gridArea[i][5] = new InaccessibleCell(i,5);
        }

        for (int i = 0; i < size; i++) {
            if (i != 2 && i != 5){
                gridArea[0][i] = new NexusCell(0, i);
                gridArea[size-1][i] = new HeroNexusCell(size-1, i);
            }
        }

        Random rnd = new Random(10);
        int size = this.size - 2;
        for (int i = 0; i < total * specialPct;i++) {
            int row, col;
            int rndNum = rnd.nextInt(total);
            row = rndNum / size + 1;
            col = rndNum % size;
            if (col < 2){
                col = col;
            }else if (col < 4){
                col = col + 1;
            }else{
                col = col + 2;
            }
            if (gridArea[row][col] == null){
                gridArea[row][col] = new BushCell(row, col);
                i++;
            }
        }

        for (int i = 0; i < total * specialPct;i++) {
            int row, col;
            int rndNum = rnd.nextInt(total);
            row = rndNum / size+ 1;
            col = rndNum % size;
            if (col < 2){
                col = col;
            }else if (col < 4){
                col = col + 1;
            }else{
                col = col + 2;
            }
            if (gridArea[row][col] == null){
                gridArea[row][col] = new CaveCell(row, col);
                i++;
            }
        }

        for (int i = 0; i < total * specialPct;i++) {
            int row, col;
            int rndNum = rnd.nextInt(total);
            row = rndNum / size + 1;
            col = rndNum % size;
            if (col < 2){
                col = col;
            }else if (col < 4){
                col = col + 1;
            }else{
                col = col + 2;
            }
            if (gridArea[row][col] == null){
                gridArea[row][col] = new KoulouCell(row, col);
                i++;
            }
        }

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (gridArea[i][j] == null){
                    gridArea[i][j] = new PlainCell(i, j);
                }
            }
        }
    }
}
