public class PlainCell<T extends Character> implements Cell, AccessibleCell<T> {

    private Hero currentHero = null;
    private Monster currentMonster = null;

    private boolean isExplored = false;

    private int[] pos = new int[2];
    public PlainCell(int row, int col) {
        pos[0] = row;
        pos[1] = col;
    }

    @Override
    public void enter(T member) {
        if (member instanceof Hero){
            ((Hero) member).resetBonus();
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
    public boolean isExplored() {
        return isExplored;
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
        isExplored = true;
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

    @Override
    public int getRow() {
        return pos[0];
    }

    @Override
    public int getCol() {
        return pos[1];
    }

    @Override
    public int[] getPos() {
        return pos;
    }
}
