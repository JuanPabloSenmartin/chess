public class Main {
    public static void main(String[] args) {
        Player white = new Player("blanco");
        Player black = new Player("negro");
        Game game = new Game(white, black);
        //game.printBoard();
        game.move(new Move(Color.WHITE, new Position("e2"), new Position("e4")));
        game.move(new Move(Color.BLACK, new Position("e7"), new Position("e5")));
        game.move(new Move(Color.WHITE, new Position("f1"), new Position("c4")));
        game.move(new Move(Color.BLACK, new Position("b8"), new Position("c6")));
        game.move(new Move(Color.WHITE, new Position("d1"), new Position("f3")));
        game.move(new Move(Color.BLACK, new Position("a7"), new Position("a6")));
        game.move(new Move(Color.WHITE, new Position("f3"), new Position("f7")));
        game.move(new Move(Color.BLACK, new Position("d7"), new Position("d6")));
        game.move(new Move(Color.WHITE, new Position("f7"), new Position("e8")));
        game.printBoard();
    }
}
