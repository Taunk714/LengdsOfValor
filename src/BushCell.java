public class BushCell extends PlainCell{
    public BushCell(int row, int col) {
        super(row, col);
    }

    @Override
    public String getColor() {
        return MyFont.ANSI_BACKGROUNDGREEN;
    }
}
