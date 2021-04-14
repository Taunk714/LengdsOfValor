import java.util.ArrayList;

public class LOVMonsterTeam extends Team<Monster>{
    public LOVMonsterTeam() {
        super();
    }

    public void removeDead(Monster monster){
        removeMember(monster);
    }

}
