public interface RpgGame extends Game {
    void showInfo();
    static RpgGame getInstance(){
        return null;
    }

    void printMap();
}
