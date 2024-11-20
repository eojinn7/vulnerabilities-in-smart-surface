
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

    public void simulate() {
        // Print the initial environment
        displayInitialEnvironment();
        int count = 0;

        // Continue moving the box down until it reaches the bottom edge
        while (boxRow + boxHeight < grid.getRows() - 1) {
            // Stage 1: Display the voting environment
            displayDecisionEnvironment();

            // Stage 2: Display the moved environment
            displayMovedEnvironment();

            count++;
        }
        System.out.println("\nNumber of steps: " + count);
        int numCarrying = boxHeight * boxWidth;
        System.out.println("Number of carrying agents: " + numCarrying);
    }

    public void displayInitialEnvironment() {
        System.out.println("Initial Env:");
        grid.displayGrid();
    }

    public void displayDecisionEnvironment() {
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
                    // Carrying agents (box) always vote to move down
                    String vote = "V";
                    cell.setSymbol(vote);
                    votes.put(vote, votes.get(vote) + 3); // Weighted vote
                } else if (isSurrounding(i, j)) {
                    // Surrounding agents always vote to move down
                    String vote = "V";
                    cell.setSymbol(vote);
                    votes.put(vote, votes.get(vote) + 1); // Lower influence
                } else if (isPenaltyZone(i, j)) {
                    cell.setSymbol("X"); // Penalty zone
                } else {
                    cell.setSymbol("."); // No decision
                }
            }
        }

        System.out.println("\nVoting:");
        grid.displayGrid();

        // Move the box based on votes
        moveBox(votes);
    }

    public void displayMovedEnvironment() {
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

    private void moveBox(Map<String, Integer> votes) {
        // The direction "down" (V) is always chosen
        if (boxRow + boxHeight < grid.getRows() - 1) {
            boxRow++; // Move down
        }

        // Update the grid after moving the box
        placeBox();
    }
}
