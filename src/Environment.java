
import java.util.HashMap;
import java.util.Map;

public class Environment {
    private Grid grid;
    private int rows;
    private int cols;
    private int boxRow; // Top-left corner of the box
    private int boxCol;
    private int boxHeight = 4; // Box height
    private int boxWidth = 2;  // Box width

    public Environment(int rows, int cols, int boxRow, int boxCol) {
        this.grid = new Grid(rows, cols);
        this.rows = rows;
        this.cols = cols;
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

        /**  Place defective agents
        grid.getCell(3, 4).setSymbol("-");
        grid.getCell(4, 4).setSymbol("-"); 
        grid.getCell(4, 5).setSymbol("-"); 
        grid.getCell(6, 4).setSymbol("-"); 
        grid.getCell(7, 4).setSymbol("-"); 
        grid.getCell(7, 5).setSymbol("-"); 
        grid.getCell(8, 4).setSymbol("-"); */
        
    }

    public void simulate() {
        // Print the initial environment
        displayInitialEnvironment();
        int count = 0;

        int numSurrounding = 0;

        // Continue moving the box down until it reaches the bottom edge
        while (boxRow + boxHeight < grid.getRows() - 1) {
            // Stage 1: Display the voting environment
            numSurrounding = displayDecisionEnvironment();

            int numCarrying = boxHeight * boxWidth;
            int numOther = rows * cols - (numCarrying + numSurrounding);

            System.out.println("\nNumber of steps: " + count);
            System.out.println("Number of carrying agents: " + numCarrying);
            System.out.println("Number of surrounding agents: " + numSurrounding);
            System.out.println("Number of other agents: " + numOther);

            // Stage 2: Display the moved environment
            displayMovedEnvironment();

            count++;
        }

        System.out.println("\nFinal destination reached!");
        System.out.println("\nNumber of steps taken: " + count + "\n");
    }

    public void displayInitialEnvironment() {
        System.out.println("Initial Env:");
        grid.displayGrid();
    }

    public int displayDecisionEnvironment() {
        Map<String, Integer> votes = new HashMap<>();
        votes.put(">", 0);
        votes.put("<", 0);
        votes.put("^", 0);
        votes.put("V", 0);

        int numSurrounding = 0; 

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
                    numSurrounding += 1;
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

        return numSurrounding;
    }

    public void displayMovedEnvironment() {
        System.out.println("\nMoved:");
        grid.displayGrid();
    }

    private boolean isSurrounding(int row, int col) {
        return row >= boxRow - 1 && row <= boxRow + boxHeight &&
               col >= boxCol - 1 && col <= boxCol + boxWidth &&
               !grid.getCell(row, col).getSymbol().equals("B"); //|| 
             //  !grid.getCell(row, col).getSymbol().equals("-");
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
