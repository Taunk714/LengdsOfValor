public class Spirit extends Monster{
    public Spirit(String[] monsterData) {
        super(monsterData);
        setType("Spirit");
    }

    @Override
    public void playHurt() {
        SoundPlayUtil.playSpiritHurt();
    }
}
