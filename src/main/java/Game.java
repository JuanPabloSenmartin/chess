import java.util.ArrayList;
import java.util.List;

public class Game {
    private final Board board;
    private final Player playerWhite;
    private final Player playerBlack;
    private final List<Move> recordedMoves;
    Result result;

    Position whiteKingPosition;
    Position blackKingPosition;

    private boolean turn; //true means white turn, false means black turn

    public Game(Board board, Player playerWhite, Player playerBlack) {
        this.board = board;
        this.playerWhite = playerWhite;
        this.playerBlack = playerBlack;
        this.recordedMoves = new ArrayList<>();
        this.result = null;
        this.turn = true;
        this.blackKingPosition = new Position(7, 4);
        this.whiteKingPosition = new Position(0, 4);
    }
    public Game(Player playerWhite, Player playerBlack) {
        this.board = new Board();
        this.playerWhite = playerWhite;
        this.playerBlack = playerBlack;
        recordedMoves = new ArrayList<>();
        result = null;
        this.turn = true;
        this.blackKingPosition = new Position(7, 4);
        this.whiteKingPosition = new Position(0, 4);
    }

    public void move(Move move) {
        if ((turn && move.color == Color.BLACK) || (!turn && move.color == Color.WHITE)){
            System.out.println("Not " + move.color.name() + "s turn");
            return;
        }
        //checkmate

        if (isMoveValid(move)){
            board.addPieceInPosition(move.finalPosition.x, move.finalPosition.y, board.positions[move.initialPosition.x][move.initialPosition.y]);
            board.deletePieceInPosition(move.initialPosition.x, move.initialPosition.y);
            recordedMoves.add(move);
            manageCheckingKing(move.finalPosition);
            turn = !turn;
        }
        else{
            System.out.println("INVALID MOVE, TRY AGAIN");
        }

    }
    private void endGame(){
        Player winner;
        Color color;
        if (turn) {
            winner = playerWhite;
            color = Color.WHITE;
        }
        else {
            winner = playerBlack;
            color = Color.BLACK;
        }
        congratulateWinner(winner, color);
        System.exit(0);
    }
    private boolean isMoveValid(Move move){
        Piece initialPiece = board.positions[move.initialPosition.x][move.initialPosition.y];
        if (initialPiece == null || initialPiece.getColor() != move.color) return false;
        return switch (initialPiece.getName()) {
            case "bishop" -> isBishopMoveValid(move);
            case "horse" -> isHorseMoveValid(move);
            case "king" -> isKingMoveValid(move);
            case "pawn" -> isPawnMoveValid(move);
            case "queen" -> isQueenMoveValid(move);
            case "rook" -> isRookMoveValid(move);
            default -> false;
        };
    }
    private boolean isBishopMoveValid(Move move){
        Piece[][] pieces = board.positions;
        int initialX = move.initialPosition.x;
        int initialY = move.initialPosition.y;
        int finalX = move.finalPosition.x;
        int finalY = move.finalPosition.y;

        //checks boundaries
        if (!checkBoundaries(initialX, initialY, finalX, finalY)) return false;

        //checks if movement is diagonal
        if (Math.abs(initialX-finalX) != Math.abs(initialY-finalY)) return  false;


        String direction = "";
        if (initialX - finalX > 0 && initialY - finalY > 0) direction = "down_left";
        if (initialX - finalX < 0 && initialY - finalY > 0) direction = "up_left";
        if (initialX - finalX > 0 && initialY - finalY < 0) direction = "down_right";
        if (initialX - finalX < 0 && initialY - finalY < 0) direction = "up_right";

        //checks there is not a piece from his color in position
        Piece pieceInFinalPosition = board.positions[move.finalPosition.x][move.finalPosition.y];
        if (pieceInFinalPosition != null && pieceInFinalPosition.getColor() == move.color) return false;

        //checks if there are pieces in front of target
        switch (direction){
            case "down_left":
                for (int i = initialX-1, j = initialY-1; i > finalX; i--, j--) {
                    if (pieces[i][j] != null) return false;
                }
                break;
            case "down_right":
                for (int i = initialX-1, j = initialY+1; i > finalX; i--, j++) {
                    if (pieces[i][j] != null) return false;
                }
                break;
            case "up_left":
                for (int i = initialX+1, j = initialY-1; i < finalX; i++, j--) {
                    if (pieces[i][j] != null) return false;
                }
                break;
            case "up_right":
                for (int i = initialX+1, j = initialY+1; i < finalX; i++, j++) {
                    if (pieces[i][j] != null) return false;
                }
                break;
            default:
                break;
        }
        return true;
    }


    private boolean isHorseMoveValid(Move move){
        int initialX = move.initialPosition.x;
        int initialY = move.initialPosition.y;
        int finalX = move.finalPosition.x;
        int finalY = move.finalPosition.y;

        //checks boundaries
        if (!checkBoundaries(initialX, initialY, finalX, finalY)) return false;

        if (!((Math.abs(initialX-finalX) == 1 && Math.abs(initialY-finalY) == 2) || (Math.abs(initialX-finalX) == 2 && Math.abs(initialY-finalY) == 1))) return false;

        //checks there is not a piece from his color in position
        Piece pieceInFinalPosition = board.positions[move.finalPosition.x][move.finalPosition.y];
        if (pieceInFinalPosition != null && pieceInFinalPosition.getColor() == move.color) return false;
        return true;
    }
    private boolean isKingMoveValid(Move move){
        Piece[][] pieces = board.positions;
        int initialX = move.initialPosition.x;
        int initialY = move.initialPosition.y;
        int finalX = move.finalPosition.x;
        int finalY = move.finalPosition.y;

        //checks boundaries
        if (!checkBoundaries(initialX, initialY, finalX, finalY)) return false;

        String direction = "";
        if (initialX == 7 && finalX == 7 && initialY == 4 && finalY == 6) direction = "shortCastleTop";
        else if (initialX == 0 && finalX == 0 && initialY == 4 && finalY == 6) direction = "shortCastleBottom";
        else if (initialX == 7 && finalX == 7 && initialY == 4 && finalY == 1) direction = "longCastleTop";
        else if (initialX == 0 && finalX == 0 && initialY == 4 && finalY == 1) direction = "longCastleBottom";
        else if (Math.abs(initialX-finalX) <= 1 && Math.abs(initialY-finalY) <= 1) direction = "move_1";

        if (direction.equals("")) return false;


        //checks if there are pieces in front of target
        switch (direction) {
            case "shortCastleTop" -> {
                if (pieces[7][5] != null || pieces[7][6] != null || !pieces[7][7].getName().equals("rook"))
                    return false;
                board.addPieceInPosition(7, 5, pieces[7][7]);
                board.deletePieceInPosition(7, 7);
            }
            case "shortCastleBottom" -> {
                if (pieces[0][5] != null || pieces[0][6] != null || !pieces[0][7].getName().equals("rook"))
                    return false;
                board.addPieceInPosition(0, 5, pieces[0][7]);
                board.deletePieceInPosition(0, 7);
            }
            case "longCastleTop" -> {
                if (pieces[7][1] != null || pieces[7][2] != null || pieces[7][3] != null || !pieces[7][0].getName().equals("rook"))
                    return false;
                board.addPieceInPosition(7, 3, pieces[7][0]);
                board.deletePieceInPosition(7, 0);
            }
            case "longCastleBottom" -> {
                if (pieces[0][1] != null || pieces[0][2] != null || pieces[0][3] != null || !pieces[0][0].getName().equals("rook"))
                    return false;
                board.addPieceInPosition(0, 3, pieces[0][0]);
                board.deletePieceInPosition(0, 0);
            }
            case "move_1" -> {
                Piece pieceInFinalPosition = board.positions[move.finalPosition.x][move.finalPosition.y];
                if (pieceInFinalPosition != null && pieceInFinalPosition.getColor() == move.color) return false;
            }
            default -> {
            }
        }

        //check is no king is getting targeted by an an opposite piece
        if (!willKingGetTargeted(move.finalPosition, move.color)) return false;

        if (turn) this.whiteKingPosition = new Position(move.finalPosition.x, move.finalPosition.y);
        else this.blackKingPosition = new Position(move.finalPosition.x, move.finalPosition.y);
        return true;
    }

    private boolean willKingGetTargeted(Position position, Color color) {
        int x = position.x;
        int y = position.y;
        Piece[][] pieces = board.positions;
        Color oppositeColor;
        if (color == Color.WHITE) oppositeColor = Color.BLACK;
        else oppositeColor = Color.WHITE;

        //check for opposite king
        if (color == Color.WHITE){
            if (Math.abs(x - blackKingPosition.x) == 1 || Math.abs(y - blackKingPosition.y) == 1) return false;
        }
        else {
            if (Math.abs(x - whiteKingPosition.x) == 1 || Math.abs(y - whiteKingPosition.y) == 1) return false;
        }
        //check for threats in diagonal
        for (int i = x-1, j = y+1; i >= 0 && j < 8; i--, j++) {
            Piece piece = pieces[i][j];
            if (piece == null) continue;
            if (piece.getColor() == oppositeColor && (piece.getName().equals("queen") || piece.getName().equals("bishop"))) return false;
            break;
        }
        for (int i = x+1, j = y+1; i < 8 && j < 8; i++, j++) {
            Piece piece = pieces[i][j];
            if (piece == null) continue;
            if (piece.getColor() == oppositeColor && (piece.getName().equals("queen") || piece.getName().equals("bishop"))) return false;
            break;
        }
        for (int i = x+1, j = y-1; i < 8 && j >= 0; i++, j--) {
            Piece piece = pieces[i][j];
            if (piece == null) continue;
            if (piece.getColor() == oppositeColor && (piece.getName().equals("queen") || piece.getName().equals("bishop"))) return false;
            break;
        }
        for (int i = x-1, j = y-1; i >= 0 && j >= 0; i--, j--) {
            Piece piece = pieces[i][j];
            if (piece == null) continue;
            if (piece.getColor() == oppositeColor && (piece.getName().equals("queen") || piece.getName().equals("bishop"))) return false;
            break;
        }
        //check for threats in rows
        for (int i = x+1; i < 8; i++) {
            Piece piece = pieces[i][y];
            if (piece == null) continue;
            if (piece.getColor() == oppositeColor && (piece.getName().equals("queen") || piece.getName().equals("rook"))) return false;
            break;
        }
        for (int i = x-1; i >= 0 ; i--) {
            Piece piece = pieces[i][y];
            if (piece == null) continue;
            if (piece.getColor() == oppositeColor && (piece.getName().equals("queen") || piece.getName().equals("rook"))) return false;
            break;
        }
        for (int j = y+1; j < 8; j++) {
            Piece piece = pieces[x][j];
            if (piece == null) continue;
            if (piece.getColor() == oppositeColor && (piece.getName().equals("queen") || piece.getName().equals("rook"))) return false;
            break;
        }
        for (int j = y-1; j >= 0; j--) {
            Piece piece = pieces[x][j];
            if (piece == null) continue;
            if (piece.getColor() == oppositeColor && (piece.getName().equals("queen") || piece.getName().equals("rook"))) return false;
            break;
        }
        //checks pawn threats
        if (color == Color.WHITE) {
            if (!checkThreat(pieces, x+1, y+1, "pawn", oppositeColor)) return false;
            if (!checkThreat(pieces, x+1, y-1, "pawn", oppositeColor)) return false;
        }
        else{
            if (!checkThreat(pieces, x-1, y+1, "pawn", oppositeColor)) return false;
            if (!checkThreat(pieces, x-1, y-1, "pawn", oppositeColor)) return false;
        }
        //check horse threats
        if (!checkThreat(pieces, x+2, y-1, "horse", oppositeColor)) return false;
        if (!checkThreat(pieces, x+2, y+1, "horse", oppositeColor)) return false;
        if (!checkThreat(pieces, x+1, y-2, "horse", oppositeColor)) return false;
        if (!checkThreat(pieces, x-1, y-2, "horse", oppositeColor)) return false;
        if (!checkThreat(pieces, x-2, y-1, "horse", oppositeColor)) return false;
        if (!checkThreat(pieces, x-2, y+1, "horse", oppositeColor)) return false;
        if (!checkThreat(pieces, x-1, y+2, "horse", oppositeColor)) return false;
        if (!checkThreat(pieces, x+1, y+2, "horse", oppositeColor)) return false;

        return true;
    }
    private boolean checkThreat(Piece[][] pieces, int x, int y, String pieceName, Color oppositeColor){
        return pieces[x][y] == null || !checkBoundaries(x, y) || !pieces[x][y].getName().equals(pieceName) || !(pieces[x][y].getColor() == oppositeColor);
    }

    private boolean isPawnMoveValid(Move move){
        Piece[][] pieces = board.positions;
        int initialX = move.initialPosition.x;
        int initialY = move.initialPosition.y;
        int finalX = move.finalPosition.x;
        int finalY = move.finalPosition.y;

        //checks boundaries
        if (!checkBoundaries(initialX, initialY, finalX, finalY)) return false;

        String action = "";
        if (finalX == 0 && initialX == 1 && initialY == finalY && !turn) action = "promoteBottomMove";
        else if (finalX == 7 && initialX == 6 && initialY == finalY && turn) action = "promoteTopMove";
        else if (finalX == 0 && initialX == 1 && (Math.abs(initialY-finalY) == 1) && !turn) action = "promoteBottomEat";
        else if (finalX == 7 && initialX == 6 && (Math.abs(initialY-finalY) == 1) && turn) action = "promoteTopEat";
        else if ((initialX-finalX == -1) && initialY == finalY && turn) action = "move_1_up";
        else if ((initialX-finalX == 1) && initialY == finalY && !turn) action = "move_1_down";
        else if (initialX == 1 && finalX == 3 && initialY == finalY && turn) action = "move_2_up";
        else if (initialX == 6 && finalX == 4 && initialY == finalY && !turn) action = "move_2_down";
        else if ((initialX-finalX == -1) && (Math.abs(initialY-finalY) == 1)) action = "eats_up";
        else if ((initialX-finalX == 1) && (Math.abs(initialY-finalY) == 1)) action = "eats_down";


        if (action.equals("")) return false;

        //checks there is not a piece from his color in position
        Piece pieceInFinalPosition = board.positions[move.finalPosition.x][move.finalPosition.y];
        if (pieceInFinalPosition != null && pieceInFinalPosition.getColor() == move.color) return false;

        //checks if its valid
        switch (action){
            case "promoteBottomMove":
            case "promoteTopMove":
            case "move_1_up":
            case "move_1_down":
                if (pieces[finalX][finalY] != null) return false;
                break;
            case "promoteBottomEat":
            case "promoteTopEat":
            case "eats_up":
            case "eats_down":
                if (pieces[finalX][finalY] == null) return false;
                break;
            case "move_2_up":
                if (pieces[finalX][finalY] != null || pieces[finalX-1][finalY] != null) return false;
                break;
            case "move_2_down":
                if (pieces[finalX][finalY] != null || pieces[finalX+1][finalY] != null) return false;
                break;
            default:
                break;
        }
        return true;
    }
    private boolean isQueenMoveValid(Move move){
        Piece[][] pieces = board.positions;
        int initialX = move.initialPosition.x;
        int initialY = move.initialPosition.y;
        int finalX = move.finalPosition.x;
        int finalY = move.finalPosition.y;

        //checks boundaries
        if (!checkBoundaries(initialX, initialY, finalX, finalY)) return false;

        String direction = "";
        if (initialX - finalX > 0 && initialY == finalY) direction = "down";
        if (initialX - finalX < 0 && initialY == finalY) direction = "up";
        if (initialY - finalY > 0 && initialX == finalX) direction = "left";
        if (initialY - finalY < 0 && initialX == finalX) direction = "right";

        if (initialX - finalX > 0 && initialY - finalY > 0) direction = "down_left";
        if (initialX - finalX < 0 && initialY - finalY > 0) direction = "up_left";
        if (initialX - finalX > 0 && initialY - finalY < 0) direction = "down_right";
        if (initialX - finalX < 0 && initialY - finalY < 0) direction = "up_right";


        //checks if movement is diagonal or straight
        if (!((Math.abs(initialX-finalX) == Math.abs(initialY-finalY)) || (initialX == finalX || initialY == finalY))) return  false;

        //checks there is not a piece from his color in position
        Piece pieceInFinalPosition = board.positions[move.finalPosition.x][move.finalPosition.y];
        if (pieceInFinalPosition != null && pieceInFinalPosition.getColor() == move.color) return false;

        //checks if there are pieces in front of target
        switch (direction){
            case "up":
                for (int i = initialX+1; i < finalX; i++) {
                    if (pieces[i][initialY] != null) return false;
                }
                break;
            case "down":
                for (int i = initialX-1; i > finalX; i--) {
                    if (pieces[i][initialY] != null) return false;
                }
                break;
            case "left":
                for (int j = initialY-1; j > finalY; j--) {
                    if (pieces[initialX][j] != null) return false;
                }
                break;
            case "right":
                for (int j = initialY+1; j < finalY; j++) {
                    if (pieces[initialX][j] != null) return false;
                }
                break;
            case "down_left":
                for (int i = initialX-1, j = initialY-1; i > finalX; i--, j--) {
                    if (pieces[i][j] != null) return false;
                }
                break;
            case "down_right":
                for (int i = initialX-1, j = initialY+1; i > finalX; i--, j++) {
                    if (pieces[i][j] != null) return false;
                }
                break;
            case "up_left":
                for (int i = initialX+1, j = initialY-1; i < finalX; i++, j--) {
                    if (pieces[i][j] != null) return false;
                }
                break;
            case "up_right":
                for (int i = initialX+1, j = initialY+1; i < finalX; i++, j++) {
                    if (pieces[i][j] != null) return false;
                }
                break;
            default:
                break;
        }
        return true;
    }
    private boolean isRookMoveValid(Move move){
        Piece[][] pieces = board.positions;
        int initialX = move.initialPosition.x;
        int initialY = move.initialPosition.y;
        int finalX = move.finalPosition.x;
        int finalY = move.finalPosition.y;

        //checks boundaries
        if (!checkBoundaries(initialX, initialY, finalX, finalY)) return false;

        String direction = "";
        if (initialX - finalX > 0) direction = "down";
        if (initialX - finalX < 0) direction = "up";
        if (initialY - finalY > 0) direction = "left";
        if (initialY - finalY < 0) direction = "right";


        //checks if movement is not straight
        if (!(initialX == finalX || initialY == finalY)) return  false;

        //checks there is not a piece from his color in position
        Piece pieceInFinalPosition = board.positions[move.finalPosition.x][move.finalPosition.y];
        if (pieceInFinalPosition != null && pieceInFinalPosition.getColor() == move.color) return false;

        //checks if there are pieces in front of target
        switch (direction){
            case "up":
                for (int i = initialX+1; i < finalX; i++) {
                    if (pieces[i][initialY] != null) return false;
                }
                break;
            case "down":
                for (int i = initialX-1; i > finalX; i--) {
                    if (pieces[i][initialY] != null) return false;
                }
                break;
            case "left":
                for (int j = initialY-1; j > finalY; j--) {
                    if (pieces[initialX][j] != null) return false;
                }
                break;
            case "right":
                for (int j = initialY+1; j < finalY; j++) {
                    if (pieces[initialX][j] != null) return false;
                }
                break;
            default:
                break;
        }
        return true;
    }
    private boolean checkBoundaries(int initialX, int initialY, int finalX, int finalY) {
        return initialX <= 7 && initialX >= 0 && initialY <= 7 && initialY >= 0 && finalX <= 7 && finalX >= 0 && finalY <= 7 && finalY >= 0;
    }
    private boolean checkBoundaries(int x, int y) {
        return x <= 7 && x >= 0 && y <= 7 && y >= 0;
    }

    private void manageCheckingKing(Position position){
        if (turn) {
            if (willKingGetTargeted(blackKingPosition, Color.BLACK)){
                if (doesKingHasAValidMove(Color.BLACK)){
                    System.out.println("Black king in check!");
                }
                else{
                    endGame();
                }
            }
        }
        else{
            if (willKingGetTargeted(whiteKingPosition, Color.WHITE)){
                if (doesKingHasAValidMove(Color.WHITE)){
                    System.out.println("White king in check!");
                }
                else{
                    endGame();
                }
            }
        }
    }

    private boolean doesKingHasAValidMove(Color color) {
        Position kingPosition;
        if (color == Color.BLACK){
            kingPosition = blackKingPosition;
        }
        else{
            kingPosition = whiteKingPosition;
        }
        if (isMoveValid(new Move(color, kingPosition, new Position(kingPosition.x + 1, kingPosition.y)))) return true;
        if (isMoveValid(new Move(color, kingPosition, new Position(kingPosition.x + 1, kingPosition.y + 1)))) return true;
        if (isMoveValid(new Move(color, kingPosition, new Position(kingPosition.x + 1, kingPosition.y - 1)))) return true;
        if (isMoveValid(new Move(color, kingPosition, new Position(kingPosition.x, kingPosition.y + 1)))) return true;
        if (isMoveValid(new Move(color, kingPosition, new Position(kingPosition.x, kingPosition.y - 1)))) return true;
        if (isMoveValid(new Move(color, kingPosition, new Position(kingPosition.x - 1, kingPosition.y)))) return true;
        if (isMoveValid(new Move(color, kingPosition, new Position(kingPosition.x - 1, kingPosition.y + 1)))) return true;
        if (isMoveValid(new Move(color, kingPosition, new Position(kingPosition.x - 1, kingPosition.y - 1)))) return true;
        return false;
    }

    private void congratulateWinner(Player player, Color color){
        System.out.println(color.name() + " wins, " + "congratulations " + player.name);
    }
    public void printBoard(){
        Piece[][] pieces = board.positions;
        for (int i = pieces.length-1; i >= 0; i--) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < pieces[i].length; j++) {
                if (pieces[i][j] != null) row.append(pieces[i][j].getName());
                else row.append("null");
                row.append("                  ");
            }
            System.out.println(row.toString());
        }
    }
}
