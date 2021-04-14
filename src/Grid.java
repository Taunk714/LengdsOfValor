public interface Grid<T> {
    int getSize();
    Cell getCell(int row, int col);
    Cell getCell(int[] pos);
    void enter(T member, int[] pos);
}
