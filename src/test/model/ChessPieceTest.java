package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ChessPieceTest {

    Rook rook;
    Game game;
    Board bd;
    ArrayList<ChessPiece> white;
    ArrayList<ChessPiece> black;
    ArrayList<ChessPiece> whiteOff;
    ArrayList<ChessPiece> blackOff;

    @BeforeEach
    private void setup() {
        rook = new Rook("white");
        game = new Game();
        bd = new Board();
        white = new ArrayList<>();
        black = new ArrayList<>();
        whiteOff = new ArrayList<>();
        blackOff = new ArrayList<>();
    }

    private void placeOnBoard(ChessPiece cp, int x, int y) {
        bd.place(cp, x, y);
        cp.setPosX(x);
        cp.setPosY(y);
    }

    private void setGame() {
        game.setGameBoard(bd);
        game.setWhiteChessPiecesOnBoard(white);
        game.setBlackChessPiecesOnBoard(black);
        game.setWhiteChessPiecesOffBoard(whiteOff);
        game.setBlackChessPiecesOffBoard(blackOff);
    }

    @Test
    public void testHasMovedMethod() {
        placeOnBoard(rook, 1, 1);
        white.add(rook);
        setGame();
        assertFalse(rook.hasMoved());
        game.move(rook, 1, 5);
        assertTrue(rook.hasMoved());
    }

    @Test
    public void testChessEquals() {
        Bishop bishop = new Bishop("white");
        assertFalse(rook.equals(bishop));
        Rook rook1 = new Rook("white");
        assertTrue(rook.equals(rook1));
        alterOnBoardMoveXYCombinations(rook1);
        Rook rook2 = new Rook("black");
        assertFalse(rook.equals(rook2));
        alterOnBoardMoveXYCombinations(rook2);
    }

    private void alterOnBoardAndMoveCombinations(ChessPiece cp) {
        cp.setMove(true);
        assertFalse(rook.equals(cp));
        cp.setMove(false);
        cp.setOnBoard(true);
        assertFalse(rook.equals(cp));
        cp.setMove(true);
        assertFalse(rook.equals(cp));
    }

    private void alterOnBoardMoveXYCombinations(ChessPiece cp) {
        alterOnBoardAndMoveCombinations(cp);
        cp.setPosY(1);
        cp.setMove(false);
        cp.setOnBoard(false);
        assertFalse(rook.equals(cp));
        alterOnBoardAndMoveCombinations(cp);
        cp.setPosX(1);
        cp.setPosY(0);
        cp.setMove(false);
        cp.setOnBoard(false);
        assertFalse(rook.equals(cp));
        alterOnBoardAndMoveCombinations(cp);
        cp.setPosY(1);
        cp.setMove(false);
        cp.setOnBoard(false);
        assertFalse(rook.equals(cp));
        alterOnBoardAndMoveCombinations(cp);
    }
}
