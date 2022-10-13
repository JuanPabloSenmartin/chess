public class Move {
    public Color color;
    public Position initialPosition;
    public Position finalPosition;

    public Move(Color color, Position initialPosition, Position finalPosition) {
        this.color = color;
        this.initialPosition = initialPosition;
        this.finalPosition = finalPosition;
    }
}
