public class Warrior extends Hero{
    Warrior(String[] heroData) {
        super(heroData, "Warrior");
        getStrength().setFavor();
        getAgility().setFavor();
    }
}
