// Agent.java
public abstract class Agent {
    protected int row;
    protected int col;
    protected int influence;

    public Agent(int row, int col, int influence) {
        this.row = row;
        this.col = col;
        this.influence = influence;
    }

    public abstract void act(Environment environment);

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getInfluence() {
        return influence;
    }
}
