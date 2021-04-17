// Skill class. Hero of different occupations are all Hero objects.
// Their favored skills are shown in Skill object, which will have a larger levelBonus
public class Skill {
    private String skillName;
    private double levelBonus;
    private int value;
    private int level;

    private double cellBonus = 1;

    public Skill(String skillName, int value, boolean isFavor) {
        this.skillName = skillName;
        this.value = value;
        this.levelBonus = isFavor?1.1:1.05;
        this.level = 1;
    }

    public Skill(String skillName, int value) {
        this.skillName = skillName;
        this.value = value;
        this.levelBonus = 1.05;
        this.level = 1;
    }

    public void setFavor(){
        levelBonus = 1.1;
    }
    public void levelUp(){
        value *= levelBonus;
        level++;
    }

    public void setCellBonus(double bonusFactor){
        cellBonus = bonusFactor;
    }

    public void resetCellBonus(){
        cellBonus = 1;
    }

    public int getValue() {
        return (int) (value * cellBonus);
    }

    public int getPlainValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
