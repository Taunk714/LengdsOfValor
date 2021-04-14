public class PlainCell implements Cell, AccessibleCell<Character> {

    private Hero currentHero = null;
    private Monster currentMonster = null;

    private int[] pos = new int[2];
    public PlainCell(int row, int col) {
        pos[0] = row;
        pos[1] = col;
    }

    @Override
    public void enter(Character member) {
        if (member instanceof Hero){
            setHero((Hero) member);
            member.setPos(pos);
        }else{
            setMonster((Monster) member);
            member.setPos(pos);
        }
    }

    @Override
    public void setHeroNull() {
        this.currentHero = null;
    }

    @Override
    public void setMonsterNull() {
        this.currentMonster = null;
    }

    @Override
    public void setCharacter(Character character) {
        character.setPos(pos);
    }

    @Override
    public Hero getHero() {
        return currentHero;
    }

    @Override
    public Monster getMonster() {
        return currentMonster;
    }

    public void setHero(Hero currentHero) {
        assert this.currentHero == null;
        this.currentHero = currentHero;
    }

    public void setMonster(Monster currentMonster) {
        assert this.currentMonster == null;
        this.currentMonster = currentMonster;
    }

    @Override
    public String getColor() {
        return MyFont.ANSI_RESET;
    }

    @Override
    public String toString() {
        return "   ";
    }
}
