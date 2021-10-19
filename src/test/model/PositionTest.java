package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;

public class PositionTest {

    Position posn;

    private void placeOnBoard(ChessPiece cp, int x, int y, Board bd) {
        bd.place(cp, x, y);
        cp.setPosX(x);
        cp.setPosY(y);
    }

    @Test
    public void testPositionAttacked() {
        Game game = new Game();
        Board bd = new Board();
        ChessPiece rook = new Rook("white");
        ChessPiece bishop = new Bishop("white");
        placeOnBoard(rook,4,5,bd);
        placeOnBoard(bishop,5,5,bd);
        ArrayList<ChessPiece> white = new ArrayList<>();
        ArrayList<ChessPiece> black = new ArrayList<>();
        white.add(rook);
        white.add(bishop);
        game.setBlackChessPiecesOnBoard(black);
        game.setWhiteChessPiecesOnBoard(white);
        game.setGameBoard(bd);
        posn = new Position(4,1);
        assertTrue(posn.attacked(white, game));
        posn = new Position(8,8);
        assertTrue(posn.attacked(white, game));
        posn = new Position(5,1);
        assertFalse(posn.attacked(white, game));
    }

    @Test
    public void testToSingleValue() {
        posn = new Position(4,4);
        int single1 = posn.toSingleValue();
        assertEquals(28,single1);
        posn = new Position(7,1);
        int single2 = posn.toSingleValue();
        assertEquals(7,single2);
    }

    @Test
    public void testPositionEqualsMethod() {
        posn = new Position(4,1);
        Position posn1 = new Position(4,1);
        Position posn2 = new Position(1,4);
        Position posn3 = new Position(4,2);
        Position posn4 = new Position(6,1);
        Position posn5 = new Position(5,6);
        assertTrue(posn.positionEquals(posn1));
        assertFalse(posn.positionEquals(posn2));
        assertFalse(posn.positionEquals(posn3));
        assertFalse(posn.positionEquals(posn4));
        assertFalse(posn.positionEquals(posn5));
    }
}
