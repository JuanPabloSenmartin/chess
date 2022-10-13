public class Position {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Position(String position){
        if (position.length() != 2) System.out.println("INVALID POSITION INFO");

        this.x = Character.getNumericValue(position.charAt(1)) -1;
        this.y = switch (position.charAt(0)) {
            case 'a' -> 0;
            case 'b' -> 1;
            case 'c' -> 2;
            case 'd' -> 3;
            case 'e' -> 4;
            case 'f' -> 5;
            case 'g' -> 6;
            case 'h' -> 7;
            default -> throw new ArrayIndexOutOfBoundsException();
        };
    }

}
