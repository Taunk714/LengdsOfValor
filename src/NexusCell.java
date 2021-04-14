public class NexusCell extends PlainCell{

    private final int[] pos = new int[2];
    public NexusCell(int row, int col) {
        super(row, col);
    }

    @Override
    public String getColor() {
        return MyFont.ANSI_BACKGROUNDRED;
    }
}
