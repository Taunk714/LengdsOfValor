public class LightningSpell extends Spell{
    public LightningSpell(String[] info) {
        super(info);
        setType("LightningSpells");
    }

    protected void effect(Monster target){
        target.dcDebuff();
    }
}
