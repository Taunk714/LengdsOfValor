import java.io.Serializable;

// Abstract item class.
public abstract class Item implements Serializable, Buy, Sell, Tablefiable {
    private int cost;
    private int level;
    private String name;
    private String type;
    private int endurance ;
    private Hero hero;

    public Item(String name, String type,int cost, int level, int endurance) {
        this.cost = cost;
        this.level = level;
        this.name = name;
        this.endurance = endurance;
        this.type = type;
    }

    public Item(String name, String type, int cost, int level) {
        this(name, type, cost, level, 1);
    }

    public abstract void use();

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Hero getHero() {
        return hero;
    }

    public String getType() {
        return type;
    }

    public int getCost() {
        return cost;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public int getEndurance() {
        return endurance;
    }

    public int getPrice(){
        return cost/2;
    }

    public abstract String[] getAttrString();
}
