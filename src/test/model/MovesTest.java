package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class MovesTest {

    Moves moves;

    @BeforeEach
    private void setup() {
        moves = new Moves();
    }

    @Test
    public void testAddMoveThenGetMoves() {
        setup();
        ArrayList<Move> initial = moves.getMoves();
        assertEquals(0,initial.size());
        Bishop bishop = new Bishop("white");
        King king = new King("black");
        Move m1 = new Move(1,1,5,5,bishop,true);
        Move m2 = new Move(4,1,4,2,king,false);
        moves.addMove(m1);
        moves.addMove(m2);
        ArrayList<Move> after = moves.getMoves();
        assertEquals(2, after.size());
        assertEquals(m1,after.get(0));
        assertEquals(m2,after.get(1));
    }

}
