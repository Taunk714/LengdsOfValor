public class BushCell extends PlainCell<Character>{
    public BushCell(int row, int col) {
        super(row, col);
    }

    @Override
    public void enter(Character member) {
        super.enter(member);
        if (member instanceof Hero){
            ((Hero) member).getDexterity().setCellBonus(configUtil.getConfigDouble("specialBonus"));
        }
    }

    @Override
    public String getColor() {
        return MyFont.ANSI_BACKGROUNDGREEN;
    }
}
