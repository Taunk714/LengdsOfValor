// Armor class.
public class Armor extends Item {
    private int damageReduction;
    public Armor(String[] info) {
        super(info[0],info[1],Integer.parseInt(info[2]), Integer.parseInt(info[3]));
        this.damageReduction = Integer.parseInt(info[4]);
        setType("Armory");

    }

    @Override
    public void use() {
        if (getHero().getArmor() != null){
            System.out.printf("%s put off %s and put on %s.\n",
                    getHero().toString(), getHero().showArmorWorn(), getName());
        }else{
            System.out.printf("%s put on %s.",
                    getHero().toString(), getName());
        }
        getHero().setArmor(this);
    }

    @Override
    public String[] getAttrString() {
        return new String[]{getName(), getType(),
                String.valueOf(getCost()), String.valueOf(getLevel()), String.valueOf(getDamageReduction())};
    }

    public int getDamageReduction() {
        return damageReduction;
    }
}
