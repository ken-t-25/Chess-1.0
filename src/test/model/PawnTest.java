package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class PawnTest {

    Pawn pawn;
    Game game;
    Board bd;
    ArrayList<ChessPiece> white;
    ArrayList<ChessPiece> black;

    @BeforeEach
    private void setup() {
        pawn = new Pawn("white");
        game = new Game();
        bd = new Board();
        white = new ArrayList<ChessPiece>();
        black = new ArrayList<ChessPiece>();
    }

    private void arrayListEquals(ArrayList<Position> list1, ArrayList<Position> list2) {
        for (int i = 0; i < list2.size(); i++) {
            Position expectedMove = list2.get(i);
            Position actualMove = list1.get(i);
            assertEquals(expectedMove.getPosX(), actualMove.getPosX());
            assertEquals(expectedMove.getPosY(), actualMove.getPosY());
        }
    }

    private void setGame() {
        game.setGameBoard(bd);
        game.setWhiteChessPiecesOnBoard(white);
        game.setBlackChessPiecesOnBoard(black);
    }

    private void placeOnBoard(ChessPiece cp, int x, int y) {
        bd.place(cp, x, y);
        cp.setPosX(x);
        cp.setPosY(y);
    }

    @Test
    public void testPossibleMovesNoBlockHasNotMoved() {
        setup();
        placeOnBoard(pawn, 6, 7);
        white.add(pawn);
        setGame();
        ArrayList<Position> pm = pawn.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<Position>();
        expected.add(new Position(6, 6));
        expected.add(new Position(6, 5));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesNoBlockHasMoved() {
        setup();
        placeOnBoard(pawn, 6, 7);
        white.add(pawn);
        setGame();
        bd.move(pawn, 6, 5);
        ArrayList<Position> pm = pawn.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<Position>();
        expected.add(new Position(6, 4));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesBlockByTeammate() {
        setup();
        placeOnBoard(pawn, 6, 7);
        Bishop bishop = new Bishop("white");
        placeOnBoard(bishop, 6,6);
        white.add(pawn);
        white.add(bishop);
        setGame();
        ArrayList<Position> pm = pawn.possibleMoves(game);
        assertEquals(0, pm.size());
    }

    @Test
    public void testPossibleMovesBlockByEnemy() {
        setup();
        placeOnBoard(pawn, 6, 7);
        Bishop bishop = new Bishop("black");
        placeOnBoard(bishop, 6,6);
        white.add(pawn);
        black.add(bishop);
        setGame();
        ArrayList<Position> pm = pawn.possibleMoves(game);
        assertEquals(0, pm.size());
    }

    @Test
    public void testPossibleMovesCanAttack() {
        setup();
        placeOnBoard(pawn, 4, 5);
        Bishop bishop = new Bishop("black");
        placeOnBoard(bishop, 3,4);
        Rook rook = new Rook("black");
        placeOnBoard(rook,5,4);
        white.add(pawn);
        black.add(bishop);
        black.add(rook);
        setGame();
        ArrayList<Position> pm = pawn.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<Position>();
        expected.add(new Position(4, 4));
        expected.add(new Position(3, 4));
        expected.add(new Position(5, 4));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesKingUnderAttack() {
        setup();
        placeOnBoard(pawn, 4, 5);
        King king = new King("white");
        placeOnBoard(king, 5, 5);
        Bishop bishop = new Bishop("black");
        placeOnBoard(bishop, 7,3);
        white.add(pawn);
        white.add(king);
        black.add(bishop);
        setGame();
        ArrayList<Position> pm = pawn.possibleMoves(game);
        assertEquals(0, pm.size());
    }

    @Test
    public void testPossibleMovesKingWillBeUnderAttack() {
        setup();
        placeOnBoard(pawn, 5, 4);
        King king = new King("white");
        placeOnBoard(king, 5, 5);
        Bishop bishop = new Bishop("black");
        placeOnBoard(bishop, 6,3);
        Rook rook = new Rook("black");
        placeOnBoard(rook, 5,3);
        white.add(pawn);
        white.add(king);
        black.add(bishop);
        black.add(rook);
        setGame();
        ArrayList<Position> pm = pawn.possibleMoves(game);
        assertEquals(0, pm.size());
    }

}
