package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChessPieceTest {

    Rook rook;
    Board bd;

    @Test
    public void testHasMovedMethod() {
        rook = new Rook("white");
        bd = new Board();
        bd.move(rook, 1 ,1);
        assertFalse(rook.hasMoved());
        bd.move(rook, 1, 5);
        assertTrue(rook.hasMoved());
    }
}
