public class KoulouCell extends PlainCell{
    public KoulouCell(int row, int col) {
        super(row, col);
    }

    @Override
    public String getColor() {
        return MyFont.ANSI_BACKGROUNDYELLOW;
    }
}
