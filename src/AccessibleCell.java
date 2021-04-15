public interface AccessibleCell<T> {
    void setCharacter(Character character);
    Hero getHero();
    Monster getMonster();
    void enter(T member);

    void setHero(Hero hero);
    void setMonster(Monster monster);

    void setHeroNull();
    void setMonsterNull();

    boolean isExplored();
}
