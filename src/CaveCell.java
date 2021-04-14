public class CaveCell extends PlainCell{
    public CaveCell(int row, int col) {
        super(row, col);
    }

    @Override
    public String getColor() {
        return MyFont.ANSI_BACKGROUNDBLUE;
    }
}
