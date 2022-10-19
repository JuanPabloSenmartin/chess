public class Board {
    Piece[][] positions;

    public Board(){
        //creates default board
        Color white = Color.WHITE;
        Color black = Color.BLACK;
        Piece[][] positions = {
                {new Rook(white),new Horse(white), new Bishop(white),new Queen(white),new King(white), new Bishop(white), new Horse(white),new Rook(white)},
                {new Pawn(white),new Pawn(white),new Pawn(white),new Pawn(white),new Pawn(white),new Pawn(white),new Pawn(white),new Pawn(white)},
                {null,null,null,null,null,null,null,null},
                {null,null,null,null,null,null,null,null},
                {null,null,null,null,null,null,null,null},
                {null,null,null,null,null,null,null,null},
                {new Pawn(black),new Pawn(black),new Pawn(black),new Pawn(black),new Pawn(black),new Pawn(black),new Pawn(black),new Pawn(black)},
                {new Rook(black),new Horse(black), new Bishop(black), new Queen(black), new King(black), new Bishop(black), new Horse(black),new Rook(black)}

        };
        this.positions = positions;
    }

    public Board(Piece[][] positions) {
        //for different types of boards
        this.positions = positions;
    }
    public void deletePieceInPosition(int x, int y){
        this.positions[x][y] = null;
    }
    public void addPieceInPosition(int x, int y, Piece piece){
        this.positions[x][y] = piece;
    }
}
