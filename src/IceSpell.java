public class IceSpell extends Spell{
    public IceSpell(String[] info) {
        super(info);
        setType("IceSpells");
    }

    @Override
    public void use() {
        super.use();
        SoundPlayUtil.playIceSpell();
    }

    protected void effect(Monster target){
        target.defenseDebuff();
    }
}
