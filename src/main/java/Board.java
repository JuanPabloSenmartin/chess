public class Board {
    Piece[][] positions;

    public Board(){
        //creates default board
        Color white = Color.WHITE;
        Color black = Color.BLACK;
        Piece[][] positions = {
                {new Rook(white),new Horse(white), new Bishop(white), new King(white), new Queen(white), new Bishop(white), new Horse(white),new Rook(white)},
                {new Pawn(white),new Pawn(white),new Pawn(white),new Pawn(white),new Pawn(white),new Pawn(white),new Pawn(white),new Pawn(white)},
                {null,null,null,null,null,null,null,null},
                {null,null,null,null,null,null,null,null},
                {null,null,null,null,null,null,null,null},
                {null,null,null,null,null,null,null,null},
                {new Pawn(black),new Pawn(black),new Pawn(black),new Pawn(black),new Pawn(black),new Pawn(black),new Pawn(black),new Pawn(black)},
                {new Rook(black),new Horse(black), new Bishop(black), new King(black), new Queen(black), new Bishop(black), new Horse(black),new Rook(black)}
        };
        this.positions = positions;
    }

    public Board(Piece[][] positions) {
        //for different types of boards
        this.positions = positions;
    }
}
