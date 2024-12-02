
import java.util.HashMap;
import java.util.Map;

public class Environment {
    private Grid grid;
    private int rows;
    private int cols;
    private int boxRow; // Top-left corner of the box
    private int boxCol;
    private int boxHeight = 3; // Box height
    private int boxWidth = 2; // Box width

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
                if (!isBoxCell(i, j)) {
                    if (isPenaltyZone(i, j)) {
                        cell.setSymbol("X"); // Penalty zone (border)
                    } else {
                        cell.setSymbol("."); // Default for other cells
                    }
                }
            }
        }

        // Place the box (B)
        for (int i = boxRow; i < boxRow + boxHeight; i++) {
            for (int j = boxCol; j < boxCol + boxWidth; j++) {
                grid.getCell(i, j).setSymbol("B"); // Box occupies this cell
            }
        }

        // Place defective agents
        grid.setDefective(3, 4);
        grid.setDefective(4, 4);
        grid.setDefective(4, 5);
        grid.setDefective(6, 4);
        grid.setDefective(7, 4);
        grid.setDefective(7, 5);
        grid.setDefective(9, 4);
    }

    public void simulate() {
        // Print the initial environment
        displayInitialEnvironment();

        int stepCount = 1;

        // Continue moving the box down until it reaches the bottom edge
        while (stepCount < 10 && boxRow + boxHeight < grid.getRows() - 1) {
            System.out.println("\n=================");
            System.out.println("Step " + stepCount);
            System.out.println("=================");

            // Stage 1: Display the voting environment
            displayDecisionEnvironment();

            // Stage 2: Display the moved environment
            displayMovedEnvironment();
            stepCount++;
        }

        System.out.println("\nNumber of steps: " + (stepCount - 1));
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
        votes.put("*", 0);

        // Iterate through the grid and update decisions
        for (int i = 0; i < grid.getRows(); i++) {
            for (int j = 0; j < grid.getCols(); j++) {
                Cell cell = grid.getCell(i, j);

                if (cell.getSymbol().equals("B")) {
                    // Carrying agents (box) always vote to move down unless defective
                    String vote = cell.isDefective() ? "*" : "V";
                    cell.setSymbol(vote);
                    votes.put(vote, votes.get(vote) + 1);
                } else if (isSurrounding(i, j)) {
                    // Surrounding agents may vote in different directions based on their defectiveness
                    String vote = cell.isDefective() ? getRandomDefectiveVote() : "V";
                    cell.setSymbol(vote);
                    votes.put(vote, votes.get(vote) + 1);
                } else if (isPenaltyZone(i, j)) {
                    cell.setSymbol("X"); // Penalty zone
                } else {
                    cell.setSymbol("."); // No decision
                }
            }
        }

        System.out.println("\nVoting:");
        grid.displayGrid();

        System.out.println("\nVote Tally:");
        for (Map.Entry<String, Integer> entry : votes.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Move the box based on votes
        String decision = moveBox(votes);
        System.out.println("\nOverall decision: " + decision);
    }

    public void displayMovedEnvironment() {
        System.out.println("\nMoved:");
        grid.displayGrid();
    }

    private boolean isSurrounding(int row, int col) {
        // Check if the cell is directly adjacent to the box
        boolean isAboveOrBelowBox = (row == boxRow - 1 || row == boxRow + boxHeight) && (col >= boxCol && col < boxCol + boxWidth);
        boolean isLeftOrRightOfBox = (col == boxCol - 1 || col == boxCol + boxWidth) && (row >= boxRow && row < boxRow + boxHeight);

        return isAboveOrBelowBox || isLeftOrRightOfBox;
    }

    private boolean isBoxCell(int row, int col) {
        return row >= boxRow && row < boxRow + boxHeight && col >= boxCol && col < boxCol + boxWidth;
    }

    private boolean isPenaltyZone(int row, int col) {
        return row == 0 || row == grid.getRows() - 1 || col == 0 || col == grid.getCols() - 1;
    }

    private String moveBox(Map<String, Integer> votes) {
        // Calculate total votes for each direction
        int downVotes = votes.get("V");
        int upVotes = votes.get("^");
        int leftVotes = votes.get("<");
        int rightVotes = votes.get(">");
        int stayVotes = votes.get("*");

        // Determine the majority direction
        String majorityDirection = "V"; // Default to down
        int maxVotes = downVotes;

        if (upVotes > maxVotes) {
            majorityDirection = "^";
            maxVotes = upVotes;
        }
        if (leftVotes > maxVotes) {
            majorityDirection = "<";
            maxVotes = leftVotes;
        }
        if (rightVotes > maxVotes) {
            majorityDirection = ">";
            maxVotes = rightVotes;
        }
        if (stayVotes > maxVotes) {
            majorityDirection = "*";
            maxVotes = stayVotes;
        }

        // Move the box in the decided direction
        switch (majorityDirection) {
            case "V": // Move down
                if (boxRow + boxHeight < grid.getRows() - 1)
                    boxRow++;
                break;
            case "^": // Move up
                if (boxRow > 0)
                    boxRow--;
                break;
            case "<": // Move left
                if (boxCol > 0)
                    boxCol--;
                break;
            case ">": // Move right
                if (boxCol + boxWidth < grid.getCols() - 1)
                    boxCol++;
                break;
            case "*":
                break;
        }

        // Update the grid after moving the box
        placeBox();
        return majorityDirection;
    }

    private String getRandomDefectiveVote() {
        String[] votes = {"V", "<", ">", "^", "*"};
        return votes[(int) (Math.random() * votes.length)];
    }
}
