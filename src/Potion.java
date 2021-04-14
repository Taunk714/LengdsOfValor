// Potion class.
public class Potion extends Item {
    private int increase;
    private String effect;
    public Potion(String[] info) {
        super(info[0],info[1],Integer.parseInt(info[2]), Integer.parseInt(info[3]));
        increase = Integer.parseInt(info[4]);
        effect = info[5];
        setType("Potions");
    }

    public int getIncrease() {
        return increase;
    }

    public String getEffect() {
        return effect;
    }

    @Override
    public void use() {

        if (effect.contains("HP")){
            getHero().setHP(getHero().getHP() + increase);
            System.out.printf("HP increases %d, now %s's HP is %d\n", increase, getHero().toString(), getHero().getHP());
        }

        if (effect.contains("Mana")){
            getHero().setMana(getHero().getMana() + increase);
            System.out.printf("Mana increases %d, now %s's Mana is %d", increase, getHero().toString(), getHero().getMana());
        }

        if (effect.contains("Strength")){
            getHero().setStrength(getHero().getStrengthVal() + increase);
            System.out.printf("Strength increases %d, now %s's Strength is %d", increase, getHero().toString(), getHero().getStrengthVal());
        }

        if (effect.contains("Agility")){
            getHero().setAgility(getHero().getAgilityVal() + increase);
            System.out.printf("Agility increases %d, now %s's Agility is %d", increase, getHero().toString(), getHero().getAgilityVal());
        }

        if (effect.contains("Dexterity")){
            getHero().setDexterity(getHero().getDexterityVal() + increase);
            System.out.printf("Dexterity increases %d, now %s's Dexterity is %d", increase, getHero().toString(), getHero().getDexterityVal());
        }
    }

    @Override
    public String[] getAttrString() {
        return new String[]{getName(), getType(),
                String.valueOf(getCost()), String.valueOf(getLevel()),
                String.valueOf(getIncrease()), String.valueOf(getEffect())};
    }
}
