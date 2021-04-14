public class IceSpell extends Spell{
    public IceSpell(String[] info) {
        super(info);
    }

    protected void effect(Monster target){
        target.defenseDebuff();
    }
}
