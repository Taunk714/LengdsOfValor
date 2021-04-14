public class FireSpell extends Spell{
    public FireSpell(String[] info) {
        super(info);
        setType("FireSpells");
    }

    protected void effect(Monster target){
        target.damageDebuff();
    }
}
