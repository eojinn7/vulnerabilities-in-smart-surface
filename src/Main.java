public class Main {
    public static void main(String[] args) {
        Environment environment = new Environment(10, 25, 2, 10);

        // initial environment
        environment.displayInitialEnvironment();

        // decision environment
        environment.displayDecisionEnvironment();

        // environment after moving the box
        environment.displayMovedEnvironment();
    }
}
