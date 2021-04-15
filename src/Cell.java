// cell interface. form the grid.
public interface Cell {
    String getColor();
    String toString();

    int getRow();
    int getCol();
    int[] getPos();
}
