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
    ArrayList<ChessPiece> whiteOff;
    ArrayList<ChessPiece> blackOff;

    @BeforeEach
    private void setup() {
        game = new Game();
    }

    private void setupRest() {
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
    public void testConstructor() {
        assertEquals(16, game.getWhiteChessPiecesOnBoard().size());
        assertEquals(16, game.getBlackChessPiecesOnBoard().size());
        assertEquals(32, game.getWhiteChessPiecesOffBoard().size());
        assertEquals(32, game.getBlackChessPiecesOffBoard().size());
        assertEquals(0, game.getHistory().size());
    }

    @Test
    public void testStalemateMethodAndHasEnded() {
        setupRest();
        King whiteKing = new King("white");
        King blackKing = new King("black");
        Bishop blackBishop = new Bishop("black");
        Pawn whitePawn = new Pawn("white");
        Queen blackQueen = new Queen("black");
        placeOnBoard(whiteKing, 1, 1);
        placeOnBoard(blackKing, 2, 3);
        placeOnBoard(blackBishop, 4, 3);
        white.add(whiteKing);
        black.add(blackKing);
        black.add(blackBishop);
        whiteOff.add(whitePawn);
        blackOff.add(blackQueen);
        setGame();
        assertTrue(game.stalemate("white"));
        assertFalse(game.stalemate("black"));
        assertTrue(game.hasEnded());
        game.reverseTurn();
        assertFalse(game.hasEnded());
        game.reverseTurn();
        game.setDrawn(true);
        assertTrue(game.hasEnded());
        game.setDrawn(false);
        game.placeFromOffBoard(blackQueen, 3,3);
        assertFalse(game.stalemate("white"));
        assertTrue(game.hasEnded());
        game.placeFromOffBoard(whitePawn, 8, 6);
        assertFalse(game.stalemate("white"));
        assertTrue(game.hasEnded());
        game.setDrawn(true);
        assertTrue(game.hasEnded());
        game.setDrawn(false);
        game.remove(blackQueen);
        assertFalse(game.stalemate("white"));
        assertFalse(game.hasEnded());
        game.setDrawn(true);
        assertTrue(game.hasEnded());
    }

    @Test
    public void testCheckAndCheckmateAndHasEnded() {
        setupRest();
        King blackKing = new King("black");
        King whiteKing = new King("white");
        Bishop whiteBishop = new Bishop("white");
        Queen whiteQueen = new Queen("white");
        placeOnBoard(blackKing, 1, 1);
        placeOnBoard(whiteBishop, 6, 6);
        placeOnBoard(whiteKing, 8, 8);
        black.add(blackKing);
        white.add(whiteBishop);
        white.add(whiteKing);
        whiteOff.add(whiteQueen);
        setGame();
        assertTrue(game.check("black"));
        assertFalse(game.checkmate("black"));
        assertFalse(game.hasEnded());
        game.setDrawn(true);
        assertTrue(game.hasEnded());
        game.setDrawn(false);
        game.placeFromOffBoard(whiteQueen, 3, 2);
        assertTrue(game.checkmate("black"));
        assertTrue(game.hasEnded());
    }

    @Test
    public void testMovesMethodsInGame() {
        Bishop bishop = new Bishop("white");
        Rook rook = new Rook("black");
        Pawn pawn = new Pawn("black");
        Move m1 = new Move(5, 5, 1, 1, bishop, true);
        Move m2 = new Move(1, 1, 0, 0, rook, false);
        Move m3 = new Move(1, 5, 1, 6, pawn, true);
        Moves ms1 = new Moves();
        Moves ms2 = new Moves();
        ms1.addMove(m1);
        ms1.addMove(m2);
        ms2.addMove(m3);
        ArrayList<Moves> history = new ArrayList<>();
        game.setHistory(history);
        game.updateHistory(ms1);
        game.updateHistory(ms2);
        assertEquals(2, game.getHistory().size());
        assertEquals(ms1, game.getHistory().get(0));
        assertEquals(ms2, game.getHistory().get(1));
        assertEquals(ms2, game.getMostRecentMoves());
        game.removeMostRecentMoves();
        assertEquals(1, game.getHistory().size());
        assertEquals(ms1, game.getMostRecentMoves());
    }

    @Test
    public void testMoveRemovePlaceMethods() {
        setupRest();
        King whiteKing = new King("white");
        King blackKing = new King("black");
        Bishop blackBishop = new Bishop("black", 4, 3);
        Pawn whitePawn = new Pawn("white", 8, 6);
        whiteOff.add(whiteKing);
        blackOff.add(blackKing);
        setGame();
        game.placeFromOffBoard(whiteKing, 1, 1);
        game.placeFromOffBoard(blackKing, 2, 3);
        game.placeNew(blackBishop);
        game.placeNew(whitePawn);
        assertEquals(2, game.getWhiteChessPiecesOnBoard().size());
        assertEquals(2, game.getBlackChessPiecesOnBoard().size());
        Position whiteKingPosn = new Position(1, 1);
        assertEquals(whiteKing, game.getBoard().getOnBoard().get(whiteKingPosn.toSingleValue() - 1));
        Position blackBishopPosn = new Position(4, 3);
        assertEquals(blackBishop, game.getBoard().getOnBoard().get(blackBishopPosn.toSingleValue() - 1));
        game.remove(whiteKing);
        game.remove(blackBishop);
        assertEquals(1, game.getWhiteChessPiecesOnBoard().size());
        assertEquals(1, game.getBlackChessPiecesOnBoard().size());
        assertEquals(1, game.getWhiteChessPiecesOffBoard().size());
        assertEquals(1, game.getBlackChessPiecesOffBoard().size());
        assertNull(game.getBoard().getOnBoard().get(whiteKingPosn.toSingleValue() - 1));
        assertNull(game.getBoard().getOnBoard().get(blackBishopPosn.toSingleValue() - 1));
    }

    @Test
    public void testSetDrawn() {
        assertFalse(game.getDrawn());
        game.setDrawn(true);
        assertTrue(game.getDrawn());
        game.setDrawn(false);
        assertFalse(game.getDrawn());
    }

    @Test
    public void testReverseTurn() {
        assertEquals("white", game.getTurn());
        game.reverseTurn();
        assertEquals("black", game.getTurn());
        game.reverseTurn();
        assertEquals("white", game.getTurn());
    }
}
