public class LightningSpell extends Spell{
    public LightningSpell(String[] info) {
        super(info);
        setType("LightningSpells");
    }

    @Override
    public void use() {
        super.use();
        SoundPlayUtil.playLightningSpell();
    }

    protected void effect(Monster target){
        target.dcDebuff();
    }
}
