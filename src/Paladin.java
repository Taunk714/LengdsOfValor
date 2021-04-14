public class Paladin extends Hero{
    Paladin(String[] heroData) {
        super(heroData, "Paladin");
        getStrength().setFavor();
        getDexterity().setFavor();
    }
}
