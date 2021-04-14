// game.Spell class. game.IceSpell, game.FireSpell and game.LightningSpell are all in this class.
public class Spell extends Item {
    private int damage;
    private int manaCost;
    private Monster target;
    public Spell(String[] info) {
        super(info[0],info[1],Integer.parseInt(info[2]), Integer.parseInt(info[3]));
        damage = Integer.parseInt(info[4]);
        manaCost = Integer.parseInt(info[5]);
    }


    @Override
    public void use() {
        int finalDamage = getHero().getDexterityVal()/10000* damage + damage;
        target.hurt(finalDamage);
        effect(target);
        target = null;
    }

    protected void effect(Monster target){}

    public void setTarget(Monster target) {
        this.target = target;
    }

    public int getDamage() {
        return damage;
    }

    public int getManaCost() {
        return manaCost;
    }

    @Override
    public String[] getAttrString() {
        return new String[]{getName(), getType(),
                String.valueOf(getCost()), String.valueOf(getLevel()),
                String.valueOf(getDamage()), String.valueOf(getManaCost())};
    }
}
