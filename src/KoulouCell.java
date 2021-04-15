public class KoulouCell extends PlainCell<Character>{
    public KoulouCell(int row, int col) {
        super(row, col);
    }


    @Override
    public void enter(Character member) {
        super.enter(member);
        if (member instanceof Hero){
            ((Hero) member).getStrength().setCellBonus(configUtil.getConfigDouble("specialBonus"));
        }
    }

    @Override
    public String getColor() {
        return MyFont.ANSI_BACKGROUNDYELLOW;
    }
}
