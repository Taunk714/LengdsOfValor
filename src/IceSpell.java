public class IceSpell extends Spell{
    public IceSpell(String[] info) {
        super(info);
        setType("IceSpells");
    }

    protected void effect(Monster target){
        target.defenseDebuff();
    }
}
