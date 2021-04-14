import java.io.Serializable;

// Abstract item class.
public abstract class Item implements Serializable, Buy, Sell, Tablefiable {
    private int cost;
    private int level;
    private String name;
    private String type;
    private int endurance ;
    private Hero hero;


    public Item(String type,String name, int cost, int level, int endurance) {
        this.cost = cost;
        this.level = level;
        this.name = name;
        this.endurance = endurance;
        this.type = type;
    }

    public Item(String type,String name,  int cost, int level) {
        this(type,name,  cost, level, 1);
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

    public void setType(String type) {
        this.type = type;
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
