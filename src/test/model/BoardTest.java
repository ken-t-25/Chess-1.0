package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BoardTest {

    Board board;

    @BeforeEach
    private void setup() {
        board = new Board();
    }

    @Test
    public void testPlacePiecePosition() {
        setup();
        Bishop bishop = new Bishop("black");
        board.place(bishop, 5, 6);
        Position posn = new Position(5, 6);
        int indexValue = posn.toSingleValue() - 1;
        assertEquals(bishop, board.getOnBoard().get(indexValue));
    }

    @Test
    public void testMovePiece() {
        setup();
        Rook rook = new Rook("white");
        board.place(rook, 3, 8);
        rook.setPosX(3);
        rook.setPosY(8);
        board.move(rook, 4, 8);
        Position posn = new Position(4, 8);
        int indexValue = posn.toSingleValue() - 1;
        assertEquals(rook, board.getOnBoard().get(indexValue));
        Position oldPosn = new Position(3, 8);
        int oldIndexValue = oldPosn.toSingleValue() - 1;
        assertNull(board.getOnBoard().get(oldIndexValue));
    }

    @Test
    public void testRemovePiece() {
        setup();
        Rook rook = new Rook("white");
        board.place(rook, 3, 8);
        rook.setPosX(3);
        rook.setPosY(8);
        board.remove(rook);
        Position posn = new Position(3, 8);
        int indexValue = posn.toSingleValue() - 1;
        assertNull(board.getOnBoard().get(indexValue));
    }
}
