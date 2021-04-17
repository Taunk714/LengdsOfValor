// Abstract game.Character class.
public abstract class Character implements Tablefiable, MovingUnit {
    private int level = 1;
    private String name;
    private int HP;
    private int[] pos = new int[2];

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public int getHP() {
        return HP;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHP(int HP) {
        this.HP = Math.max(HP, 0);
    }

    public int[] getPos() {
        return pos;
    }

    public void setPos(int[] pos) {
        this.pos = pos;
    }

    public void setPos(int row, int col) {
        this.pos[0] = row;
        this.pos[1] = col;
    }

    public abstract void revive(boolean b);

    public abstract void gainAndLevelUp(int exp, int money);

    public abstract String mark();
}
