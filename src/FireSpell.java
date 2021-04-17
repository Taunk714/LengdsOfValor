public class FireSpell extends Spell{
    public FireSpell(String[] info) {
        super(info);
        setType("FireSpells");
    }

    @Override
    public void use() {
        super.use();
        SoundPlayUtil.playFireSpell();
    }

    protected void effect(Monster target){
        target.damageDebuff();
    }
}
