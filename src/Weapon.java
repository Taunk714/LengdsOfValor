// game.Weapon class.
public class Weapon extends Item {
    private int damage;
    private int hand;
    public Weapon(String[] info) {
        super(info[0],info[1],Integer.parseInt(info[2]), Integer.parseInt(info[3]));
        this.damage = Integer.parseInt(info[4]);
        this.hand = Integer.parseInt(info[5]);
    }

    @Override
    public void use() {
        getHero().setWeapons(this);
    }

    public int getDamage() {
        return damage;
    }

    public int getHand() {
        return hand;
    }

    @Override
    public String[] getAttrString() {
        return new String[]{getName(), getType(),
                String.valueOf(getCost()), String.valueOf(getLevel()),
                String.valueOf(getDamage()), String.valueOf(getHand())};
    }
}
