public class Sorcerer extends Hero{
    Sorcerer(String[] heroData) {
        super(heroData, "Sorcerer");
        getAgility().setFavor();
        getDexterity().setFavor();
    }
}
