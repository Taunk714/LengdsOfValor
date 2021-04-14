public class FireSpell extends Spell{
    public FireSpell(String[] info) {
        super(info);
    }

    protected void effect(Monster target){
        target.damageDebuff();
    }
}
