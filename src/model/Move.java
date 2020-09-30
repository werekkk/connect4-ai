package model;

public class Move {

    public int column;
    public Color color;
    public int score;

    public Move(int column, Color color) {
        this.column = column;
        this.color = color;
    }

    @Override
    public String toString() {
        return "(" + column + ", " + color.toString() + ")";
    }
}
