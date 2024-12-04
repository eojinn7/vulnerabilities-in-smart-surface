
public class Main {
  public static void main(String[] args) {
    // Create a 25x10 environment, with the box starting at row 2, column 10
    Environment environment = new Environment(11, 10, 2, 4);
     // Simulate the environment
    try {
        java.io.FileWriter writer = new java.io.FileWriter("simulation_results.csv");
        writer.write("Trial,Steps\n"); // Header row
        
        for (int trial = 1; trial <= 1000; trial++) {
            // Create new environment for each trial
            Environment env = new Environment(11, 10, 0, 4);
            int steps = env.simulate();
            writer.write(trial + "," + steps + "\n");
            
            // Print progress every 100 trials
            if (trial % 100 == 0) {
                System.out.println("Completed " + trial + " trials");
            }
        }
        writer.close();
        System.out.println("Results saved to simulation_results.csv");
    } catch (java.io.IOException e) {
        System.err.println("Error writing to file: " + e.getMessage());
    }
    
    // Simulate the environment
    environment.simulate();
  }
}
