public class NexusCell<T extends Character> extends PlainCell<T>{

    private final int[] pos = new int[2];
    public NexusCell(int row, int col) {
        super(row, col);
    }

    @Override
    public String getColor() {
        return MyFont.ANSI_BACKGROUNDRED;
    }
}
