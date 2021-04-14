// Inaccessible cell. will print reject info and go back to the previous cell.
public class InaccessibleCell implements Cell{

    private int[] pos = new int[2];
    public InaccessibleCell(int row, int col) {
        pos[0] = row;
        pos[1] = col;
    }

//    @Override
//    public void enter(T heroes) {
//        System.out.println("You can't go here. It's inaccessible.");
//    }

    public int[] getPos() {
        return pos;
    }

    @Override
    public String toString() {
        return MyFont.ANSI_GREY + "▓▓▓" + MyFont.ANSI_RESET;
    }

    @Override
    public String getColor() {
        return MyFont.ANSI_BACKGROUNDGREY;
    }
}
