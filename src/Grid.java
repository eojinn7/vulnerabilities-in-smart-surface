// Grid.java
public class Grid {
  private int rows;
  private int cols;
  private Cell[][] grid;

  public Grid(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
    this.grid = new Cell[rows][cols];
    initializeGrid();
  }

  private void initializeGrid() {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        grid[i][j] = new Cell(i, j); // Initialize each cell
      }
    }
  }

  public void displayGrid() {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        System.out.print(grid[i][j].getSymbol() + " ");
      }
      System.out.println();
    }
  }

  public Cell getCell(int row, int col) {
    if (row >= 0 && row < rows && col >= 0 && col < cols) {
      return grid[row][col];
    }
    return null;
  }

  public boolean setDetective(int row, int col) {
    Cell cell = getCell(row, col);
    if (cell != null) {
      cell.setDefective(true);
      return true;
    }
    return false;
  }

  public int getRows() {
    return rows;
  }

  public int getCols() {
    return cols;
  }
}
