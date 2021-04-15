public class CaveCell extends PlainCell<Character>{
    public CaveCell(int row, int col) {
        super(row, col);
    }

    @Override
    public void enter(Character member) {
        super.enter(member);
        if (member instanceof Hero){
            ((Hero) member).getAgility().setCellBonus(configUtil.getConfigDouble("specialBonus"));
        }
    }

    @Override
    public String getColor() {
        return MyFont.ANSI_BACKGROUNDBLUE;
    }
}
