package persistence;

import model.*;

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public abstract class JsonTest {

    // constructs a specific modified game for comparison
    protected Game modifyCompareGame() {
        Game compare = new Game();
        ArrayList<ChessPiece> wcOnBoardCopy = new ArrayList<>(compare.getWhiteChessPiecesOnBoard());
        for (ChessPiece cp : wcOnBoardCopy) {
            if (cp.getPosX() == 5 && cp.getPosY() == 7) {
                compare.move(cp, 5, 5);
                Moves moves = new Moves();
                moves.addMove(new Move(5, 7, 5, 5, cp, false));
                compare.updateHistory(moves);
            }
        }
        ArrayList<ChessPiece> bcOnBoardCopy = new ArrayList<>(compare.getBlackChessPiecesOnBoard());
        for (ChessPiece cp : bcOnBoardCopy) {
            if (cp.getPosX() == 1 && cp.getPosY() == 2) {
                compare.move(cp, 1, 4);
                Moves moves = new Moves();
                moves.addMove(new Move(1, 2, 1, 4, cp, false));
                compare.updateHistory(moves);
            }
        }
        ArrayList<ChessPiece> wcOnBoardCopy1 = new ArrayList<>(compare.getWhiteChessPiecesOnBoard());
        for (ChessPiece cp : wcOnBoardCopy1) {
            if (cp.getPosX() == 5 && cp.getPosY() == 5) {
                compare.move(cp, 5, 4);
                Moves moves = new Moves();
                moves.addMove(new Move(5, 5, 5, 4, cp, true));
                compare.updateHistory(moves);
            }
        }
        compare.reverseTurn();
        return compare;
    }


    // EFFECTS: compares whether the given two chess lists contains the same chess pieces
    protected void checkChessList(ArrayList<ChessPiece> l1, ArrayList<ChessPiece> l2) {
        assertEquals(l1.size(), l2.size());
        int len = l1.size();
        for (int i = 0; i < len; i++) {
            ChessPiece cp1 = l1.get(i);
            ChessPiece cp2 = l2.get(i);
            if (Objects.isNull(cp1)) {
                assertNull(cp2);
            } else {
                checkChess(cp1, cp2);
            }
        }
    }

    // EFFECTS: compares whether the given two moves lists contains the same moves
    protected void checkMovesList(ArrayList<Moves> l1, ArrayList<Moves> l2) {
        assertEquals(l1.size(), l2.size());
        int len = l1.size();
        for (int i = 0; i < len; i++) {
            checkMoveList(l1.get(i).getMoves(), l2.get(i).getMoves());
        }
    }

    // EFFECTS: compares whether the given two move lists contains the same move
    protected void checkMoveList(ArrayList<Move> l1, ArrayList<Move> l2) {
        assertEquals(l1.size(), l2.size());
        int len = l1.size();
        for (int i = 0; i < len; i++) {
            Move m1 = l1.get(i);
            Move m2 = l2.get(i);
            assertEquals(m1.getBeginX(), m2.getBeginX());
            assertEquals(m1.getBeginY(), m2.getBeginY());
            assertEquals(m1.getEndX(), m2.getEndX());
            assertEquals(m1.getEndY(), m2.getEndY());
            checkChess(m1.getChessPiece(), m2.getChessPiece());
            assertEquals(m1.getMoveStatus(), m2.getMoveStatus());
        }
    }

    // EFFECTS: compares whether the given two chess pieces have the same information
    protected void checkChess(ChessPiece cp1, ChessPiece cp2) {
        assertEquals(cp1.getPosX(), cp2.getPosX());
        assertEquals(cp1.getPosY(), cp2.getPosY());
        assertEquals(cp1.getOnBoard(), cp2.getOnBoard());
        assertEquals(cp1.getColour(), cp2.getColour());
        assertEquals(cp1.hasMoved(), cp2.hasMoved());
    }

    // EFFECTS: compares whether the given two games contain the same elements
    protected void checkGame(Game compare, Game game) {
        assertEquals(compare.getWhiteChessPiecesOnBoard().size(), game.getWhiteChessPiecesOnBoard().size());
        assertEquals(compare.getBlackChessPiecesOnBoard().size(), game.getBlackChessPiecesOnBoard().size());
        assertEquals(compare.getWhiteChessPiecesOffBoard().size(), game.getWhiteChessPiecesOffBoard().size());
        assertEquals(compare.getBlackChessPiecesOffBoard().size(), game.getBlackChessPiecesOffBoard().size());
        checkChessList(compare.getBoard().getOnBoard(), game.getBoard().getOnBoard());
        checkMovesList(compare.getHistory(), game.getHistory());
        assertEquals(compare.getTurn(), game.getTurn());
        assertEquals(compare.getDrawn(), game.getDrawn());
    }
}
