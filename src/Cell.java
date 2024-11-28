// Cell.java
public class Cell {
  private int row;
  private int col;
  private String symbol;
  private boolean defective;

  public Cell(int row, int col) {
    this.row = row;
    this.col = col;
    this.symbol = "."; // default symbol, has no decision
    this.defective = false; // default state is false
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public boolean isDefective() {
    return defective;
  }

  public void setDefective(boolean defective) {
    this.defective = defective;
  }
}
