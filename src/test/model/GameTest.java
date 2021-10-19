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
        white = new ArrayList<ChessPiece>();
        black = new ArrayList<ChessPiece>();
        whiteOff = new ArrayList<ChessPiece>();
        blackOff = new ArrayList<ChessPiece>();
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
        assertEquals(16,game.getWhiteChessPiecesOnBoard().size());
        assertEquals(16,game.getBlackChessPiecesOnBoard().size());
        assertEquals(32,game.getWhiteChessPiecesOffBoard().size());
        assertEquals(32,game.getBlackChessPiecesOffBoard().size());
        assertEquals(0,game.getHistory().size());
    }

    @Test
    public void testStalemateMethodAndHasEnded() {
        setupRest();
        King whiteKing = new King("white");
        King blackKing = new King("black");
        Bishop blackBishop = new Bishop("black");
        Pawn whitePawn = new Pawn("white");
        placeOnBoard(whiteKing,1,1);
        placeOnBoard(blackKing, 2,3);
        placeOnBoard(blackBishop,4,3);
        white.add(whiteKing);
        black.add(blackKing);
        black.add(blackBishop);
        whiteOff.add(whitePawn);
        setGame();
        assertTrue(game.stalemate("white"));
        assertFalse(game.stalemate("black"));
        assertTrue(game.hasEnded());
        game.placeFromOffBoard(whitePawn,8,6);
        assertFalse(game.stalemate("white"));
    }

    @Test
    public void testCheckAndCheckmateAndHasEnded() {
        setupRest();
        King whiteKing = new King("white");
        King blackKing = new King("black");
        Bishop blackBishop = new Bishop("black");
        Queen blackQueen = new Queen("black");
        placeOnBoard(whiteKing,1,1);
        placeOnBoard(blackBishop,6,6);
        placeOnBoard(blackKing, 8,8);
        white.add(whiteKing);
        black.add(blackBishop);
        black.add(blackKing);
        blackOff.add(blackQueen);
        setGame();
        assertTrue(game.check("white"));
        assertFalse(game.checkmate("white"));
        assertFalse(game.hasEnded());
        game.placeFromOffBoard(blackQueen,3,2);
        assertTrue(game.checkmate("white"));
        assertTrue(game.hasEnded());
    }

    @Test
    public void testMovesMethodsInGame() {
        Bishop bishop = new Bishop("white");
        Rook rook = new Rook("black");
        Pawn pawn = new Pawn("black");
        Move m1 = new Move(5,5,1,1, bishop,true);
        Move m2 = new Move(1,1,0,0, rook,false);
        Move m3 = new Move(1,5,1,6, pawn,true);
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

    @Test
    public void testMoveRemovePlaceMethods() {
        setupRest();
        King whiteKing = new King("white");
        King blackKing = new King("black");
        Bishop blackBishop = new Bishop("black",4,3);
        Pawn whitePawn = new Pawn("white",8,6);
        whiteOff.add(whiteKing);
        blackOff.add(blackKing);
        setGame();
        game.placeFromOffBoard(whiteKing,1,1);
        game.placeFromOffBoard(blackKing,2,3);
        game.placeNew(blackBishop);
        game.placeNew(whitePawn);
        assertEquals(2,game.getWhiteChessPiecesOnBoard().size());
        assertEquals(2,game.getBlackChessPiecesOnBoard().size());
        Position whiteKingPosn = new Position(1,1);
        assertEquals(whiteKing, game.getBoard().getOnBoard().get(whiteKingPosn.toSingleValue() - 1));
        Position blackBishopPosn = new Position(4,3);
        assertEquals(blackBishop, game.getBoard().getOnBoard().get(blackBishopPosn.toSingleValue() - 1));
        game.remove(whiteKing);
        game.remove(blackBishop);
        assertEquals(1,game.getWhiteChessPiecesOnBoard().size());
        assertEquals(1,game.getBlackChessPiecesOnBoard().size());
        assertEquals(1,game.getWhiteChessPiecesOffBoard().size());
        assertEquals(1,game.getBlackChessPiecesOffBoard().size());
        assertNull(game.getBoard().getOnBoard().get(whiteKingPosn.toSingleValue() - 1));
        assertNull(game.getBoard().getOnBoard().get(blackBishopPosn.toSingleValue() - 1));
    }

}
