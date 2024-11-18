// Cell.java
public class Cell {
    private int row;
    private int col;
    private String symbol;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.symbol = "."; // default symbol, has no decision
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
