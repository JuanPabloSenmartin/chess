import java.util.ArrayList;
import java.util.List;

public class Game {
    private final Board board;
    private final Player player1;
    private final Player player2;
    private List<Move> recordedMoves;
    private boolean hasFinished;
    Result result;
    private boolean whiteInCheck;
    private boolean blackInCheck;

    public Game(Board board, Player player1, Player player2) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        recordedMoves = new ArrayList<>();
        hasFinished = false;
        result = null;

    }
    public Game(Player player1, Player player2) {
        this.board = new Board();
        this.player1 = player1;
        this.player2 = player2;
        recordedMoves = new ArrayList<>();
        hasFinished = false;
        result = null;
    }

    public void move(Move move) throws InvalidMoveException {
        if (isMoveValid(move)){
            recordedMoves.add(move);
        }
        else{
            throw new InvalidMoveException();
        }

    }
    private boolean isMoveValid(Move move){
        return switch (move.initialPosition.getPiece().getName()) {
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
        return false;
    }
    private boolean isHorseMoveValid(Move move){
        return false;
    }
    private boolean isKingMoveValid(Move move){
        return false;
    }
    private boolean isPawnMoveValid(Move move){
        return false;
    }
    private boolean isQueenMoveValid(Move move){
        return false;
    }
    private boolean isRookMoveValid(Move move){
        return false;
    }

    private void congratulateWinner(Player player){
        System.out.println(player.color.name() + "wins, " + "congratulations " + player.name);
    }
}
