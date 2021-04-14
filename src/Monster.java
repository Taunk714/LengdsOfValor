import java.util.Random;
// monster class. Attack hero automatically.
public class Monster extends Character implements Fight{

    private int defense;
    private int damage;
    private int dodgeChance;
    private String type;

    private int defenseDebuff = 1;
    private int damageDebuff = 1;
    private int dcDebuff = 1;

    public Monster(String[] monsterData) {
        //Name/level/damage/defense/dodge chance

        type = monsterData[0];
        setName(monsterData[1]);
        setLevel(Integer.parseInt(monsterData[2]));
        damage = Integer.parseInt(monsterData[3]);
        defense = Integer.parseInt(monsterData[4]);
        dodgeChance = Integer.parseInt(monsterData[5]);
        setHP(getLevel()*100);
    }

    public int getDefense() {
        return defense;
    }

    public int getDamage() {
        return damage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDodgeChance() {
        return dodgeChance;
    }

    public void attack(Hero target) {
        int finalDamage = damage * damageDebuff;
        System.out.printf("%s attacks %s! \n%s╰（‵□′）╯ =======> (눈_눈)%s\n",
                toString(), target.toString(), MyFont.ANSI_BACKGROUNDWHITE, MyFont.ANSI_RESET);
        target.hurt(finalDamage);
    }

    // get hurt by others.
    public void hurt(int damage){
//        Random rnd = new Random();
        if (randomUtil.nextInt(100) < dodgeChance * dcDebuff){
            System.out.println("Miss!ヽ(ﾟ∀ﾟ)ﾒ(ﾟ∀ﾟ)ﾉ");
        }else{
            int d = (int) ((damage - defense*defenseDebuff)*0.5);
            setHP(getHP()-d);
            System.out.printf("%s 's HP -%s. Current HP: %d\n\n", toString(),d, getHP());
            if (getHP() <= 0){
                System.out.println(MyFont.ANSI_RED + MyFont.ANSI_BOLD + toString() +  " died."+MyFont.ANSI_RESET);
            }
        }
    }

    public void damageDebuff(){
        damageDebuff *= 0.9;
    }

    public void defenseDebuff(){
        defenseDebuff *= 0.9;
    }


    public void dcDebuff(){
        dcDebuff *= 0.9;
    }


//    public void effect(String type){
//        switch (type){
//            case "FireSpell"->{
//                damageDebuff *= 0.9;
//            }
//            case "IceSpell"->{
//                defenseDebuff *= 0.9;
//            }
//            case "LightningSpell"->{
//                dcDebuff *= 0.9;
//            }
//        }
//    }

    @Override
    public String toString() {
        return MyFont.ANSI_PURPLE + type + " " + getName() + MyFont.ANSI_RESET;
    }

    @Override
    public void revive(boolean b) {

    }

    @Override
    public String[] getAttrString() {

        //"Type", "Name","Lv","HP","Defense", "Damage"
        return new String[]{getType(), getName(),
                String.valueOf(getLevel()), String.valueOf(getHP()),
                String.valueOf(getDefense()), String.valueOf(getDamage())};
    }

    @Override
    public void gainAndLevelUp(int exp, int money) {

    }

    @Override
    public String mark() {
        return " ◥◤";
    }
}
