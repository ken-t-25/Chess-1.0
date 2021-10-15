package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    Game game;
    Board bd;
    ArrayList<ChessPiece> white;
    ArrayList<ChessPiece> black;

    @BeforeEach
    private void setup() {
        game = new Game();
    }

    private void setupRest() {
        bd = new Board();
        white = new ArrayList<ChessPiece>();
        black = new ArrayList<ChessPiece>();
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
    }

    @Test
    public void testStalemateMethodAndHasEnded() {
        setup();
        setupRest();
        King whiteKing = new King("white");
        King blackKing = new King("black");
        Bishop blackBishop = new Bishop("black");
        placeOnBoard(whiteKing,1,1);
        placeOnBoard(blackKing, 2,3);
        placeOnBoard(blackBishop,4,3);
        white.add(whiteKing);
        black.add(blackKing);
        black.add(blackBishop);
        setGame();
        assertTrue(game.stalemate("white"));
        assertFalse(game.stalemate("black"));
        assertTrue(game.hasEnded());
        Pawn whitePawn = new Pawn("white");
        bd.place(whitePawn,8,6);
        game.setGameBoard(bd);
        assertFalse(game.stalemate("white"));
        assertFalse(game.hasEnded());
    }

    @Test
    public void testCheckAndCheckmateAndHasEnded() {
        setup();
        setupRest();
        King whiteKing = new King("white");
        King blackKing = new King("black");
        Bishop blackBishop = new Bishop("black");
        placeOnBoard(whiteKing,1,1);
        placeOnBoard(blackBishop,6,6);
        placeOnBoard(blackKing, 8,8);
        white.add(whiteKing);
        black.add(blackBishop);
        black.add(blackKing);
        setGame();
        assertTrue(game.check("white"));
        assertFalse(game.checkmate("white"));
        assertFalse(game.hasEnded());
        Queen blackQueen = new Queen("black");
        placeOnBoard(blackQueen, 3,2);
        black.add(blackQueen);
        setGame();
        assertTrue(game.check("white"));
        assertTrue(game.checkmate("white"));
        assertTrue(game.hasEnded());
    }

    @Test
    public void testMovesMethodsInGame() {
        setup();
        Bishop bishop = new Bishop("white");
        Rook rook = new Rook("black");
        Pawn pawn = new Pawn("black");
        Move m1 = new Move(5,5,1,1, bishop);
        Move m2 = new Move(1,1,0,0, rook);
        Move m3 = new Move(1,5,1,6, pawn);
        Moves ms1 = new Moves();
        Moves ms2 = new Moves();
        ms1.addMove(m1);
        ms1.addMove(m2);
        ms2.addMove(m3);
        ArrayList<Moves> history = new ArrayList<Moves>();
        game.setHistory(history);
        game.updateHistory(ms1);
        game.updateHistory(ms2);
        assertEquals(2, game.getHistory().size());
        assertEquals(ms1,game.getHistory().get(0));
        assertEquals(ms2,game.getHistory().get(1));
        assertEquals(ms2,game.getMostRecentMoves());
        game.removeMostRecentMoves();
        assertEquals(1, game.getHistory().size());
        assertEquals(ms1,game.getMostRecentMoves());
    }

}
