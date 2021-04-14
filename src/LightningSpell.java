public class LightningSpell extends Spell{
    public LightningSpell(String[] info) {
        super(info);
    }

    protected void effect(Monster target){
        target.dcDebuff();
    }
}
