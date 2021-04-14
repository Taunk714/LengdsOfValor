import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

// The unit during the game.
public class Team<T extends Character> implements Iterable<T>{
    private List<T> members;
    private int teamSize;
    private int currSize = 0;
    private boolean dead = true;
    private int[] pos = new int[2];

    public Team(T[] members, int posRow, int posCol) {
        this.members = Arrays.asList(members);
        teamSize = members.length;
        currSize = teamSize;
        this.pos[0] = posRow;
        this.pos[1] = posCol;
    }

    public Team(int size, int posRow, int posCol) {
        this.members = new ArrayList<>(size);
        teamSize = size;
        this.pos[0] = posRow;
        this.pos[1] = posCol;
    }

    public Team(T[] members) {
        this(members, 0, 0);
    }

    public Team(int size) {
        this(size, 0, 0);
    }

    public Team(){
        this.members = new ArrayList<>();
    }

    public T getMember(int i) {
        return members.get(i);
    }

    public List<T> getMembers() {
        return members;
    }

    public int[] getPos() {
        return new int[]{pos[0],pos[1]};
    }

    public void setPos(int[] pos) {
        this.pos = pos;
    }

    public void setMember(int index, T hero) {
        this.members.add(index,hero);
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setMembers(List<T> members) {
        this.members = members;
    }

    public void addMember(T members){
        this.members.add(members);
    }

    // return whether all the heroes died.
    public boolean isDead(){
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getHP() > 0){
                return false;
            }
        }
        return true;
    }

    // revive if the heroes lose but still want to take the adventure.
    public void revive(){
        for (int i = 0; i < teamSize; i++) {
            assert members.get(i).getHP() <= 0;
        }

        for (int i = 0; i < teamSize; i++) {
            members.get(i).revive(true);
        }

        dead = false;
    }

    // Settlement after winning the battle. If hero is faint, he will get half the hp and mana
    public void gainAfterBattle(int exp, int money){
        for (T hero : members) {
            System.out.println(hero.toString());
            if (hero.getHP() > 0) {
                System.out.printf("%s gains %d exp and $%d\n", hero.toString(), exp, money);
                hero.gainAndLevelUp(exp, money);
            } else {
                System.out.printf("%s is faint. Revive!\n", hero.toString());
                hero.revive(false);
            }
            System.out.print("\n");
        }
    }

    @Override
    public String toString() {
        return MyFont.ANSI_CYAN + "◢◣" + MyFont.ANSI_RESET;
    }

    @Override
    public Iterator<T> iterator() {
        return members.iterator();
    }

    public int size() {
        return members.size();
    }
}
