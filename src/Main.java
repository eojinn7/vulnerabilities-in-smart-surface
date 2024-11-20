
public class Main {
    public static void main(String[] args) {
        // Create a 25x10 environment, with the box starting at row 2, column 10
        Environment environment = new Environment(25, 10, 2, 5);

        // Simulate the environment
        environment.simulate();
    }
}
