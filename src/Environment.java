import java.util.HashMap;
import java.util.Map;

public class Environment {
    private Grid grid;
    private int boxRow; // Top-left corner of the box
    private int boxCol;
    private int boxHeight = 4; // Box height
    private int boxWidth = 2;  // Box width

    public Environment(int rows, int cols, int boxRow, int boxCol) {
        this.grid = new Grid(rows, cols);
        this.boxRow = boxRow;
        this.boxCol = boxCol;
        placeBox();
    }

    private void placeBox() {
        // Reset the grid
        for (int i = 0; i < grid.getRows(); i++) {
            for (int j = 0; j < grid.getCols(); j++) {
                Cell cell = grid.getCell(i, j);
                if (isPenaltyZone(i, j)) {
                    cell.setSymbol("X"); // Penalty zone (border)
                } else {
                    cell.setSymbol("."); // Default for other cells
                }
            }
        }

        // Place the box (B)
        for (int i = boxRow; i < boxRow + boxHeight; i++) {
            for (int j = boxCol; j < boxCol + boxWidth; j++) {
                grid.getCell(i, j).setSymbol("B"); // Box occupies this cell
            }
        }
    }

    public void displayInitialEnvironment() {
        // Display the initial environment with the box and default symbols
        System.out.println("Initial Env:");
        grid.displayGrid();
    }

    public void displayDecisionEnvironment() {
        // Store weighted votes for each direction
        Map<String, Integer> votes = new HashMap<>();
        votes.put(">", 0);
        votes.put("<", 0);
        votes.put("^", 0);
        votes.put("V", 0);

        // Iterate through the grid and update decisions
        for (int i = 0; i < grid.getRows(); i++) {
            for (int j = 0; j < grid.getCols(); j++) {
                Cell cell = grid.getCell(i, j);

                if (cell.getSymbol().equals("B")) {
                    // Carrying agents (box) always vote to move left
                    String vote = "<"; // Force carrying agents to vote left
                    cell.setSymbol(vote);
                    votes.put(vote, votes.get(vote) + 3); // Weighted vote
                } else if (isSurrounding(i, j)) {
                    // Surrounding agents decide direction
                    String vote = decideMovement(i, j);
                    cell.setSymbol(vote);
                    votes.put(vote, votes.get(vote) + 1); // Lower influence
                } else if (isPenaltyZone(i, j)) {
                    // Penalty zone remains as "X"
                    cell.setSymbol("X");
                } else {
                    // Agents not carrying, not surrounding, and no decision
                    cell.setSymbol(".");
                }
            }
        }

        // Display the environment with decisions
        System.out.println("\nVoting:");
        grid.displayGrid();

        // Move the box based on votes
        moveBox(votes);
    }

    public void displayMovedEnvironment() {
        // Display the environment after the box has moved
        System.out.println("\nMoved:");
        grid.displayGrid();
    }

    private boolean isSurrounding(int row, int col) {
        return row >= boxRow - 1 && row <= boxRow + boxHeight &&
               col >= boxCol - 1 && col <= boxCol + boxWidth &&
               !grid.getCell(row, col).getSymbol().equals("B");
    }

    private boolean isPenaltyZone(int row, int col) {
        return row == 0 || row == grid.getRows() - 1 || col == 0 || col == grid.getCols() - 1;
    }

    private String decideMovement(int row, int col) {
        // Surrounding agents decide direction based on relative position to the box
        if (row < boxRow) return "^"; // Vote to move up
        if (row >= boxRow + boxHeight) return "V"; // Vote to move down
        if (col < boxCol) return "<"; // Vote to move left
        if (col >= boxCol + boxWidth) return ">"; // Vote to move right
        return "."; // Default for no movement
    }

    private void moveBox(Map<String, Integer> votes) {
        int up = votes.get("^");
        int down = votes.get("V");
        int left = votes.get("<");
        int right = votes.get(">");

        // move the box in the direction with the highest vote 
        if (left > up && left > down && left > right && boxCol > 0) {
            boxCol--; // left
        } else if (up > down && up > left && up > right && boxRow > 0) {
            boxRow--; // up
        } else if (down > up && down > left && down > right && boxRow + boxHeight < grid.getRows()) {
            boxRow++; // down
        } else if (right > up && right > down && right > left && boxCol + boxWidth < grid.getCols()) {
            boxCol++; // right
        }

        // update the grid after moving the box
        placeBox();
    }
}
