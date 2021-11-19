package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PawnTest {

    Pawn pawn;
    King king;
    Game game;
    Board bd;
    ArrayList<ChessPiece> white;
    ArrayList<ChessPiece> black;
    ArrayList<ChessPiece> whiteOff;
    ArrayList<ChessPiece> blackOff;

    @BeforeEach
    private void setup() {
        pawn = new Pawn("white");
        king = new King("white");
        game = new Game();
        bd = new Board();
        white = new ArrayList<>();
        black = new ArrayList<>();
        whiteOff = new ArrayList<>();
        blackOff = new ArrayList<>();
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
        game.setWhiteChessPiecesOffBoard(whiteOff);
        game.setBlackChessPiecesOffBoard(blackOff);
    }

    private void placeOnBoard(ChessPiece cp, int x, int y) {
        bd.place(cp, x, y);
        cp.setPosX(x);
        cp.setPosY(y);
    }

    @Test
    public void testPossibleMovesNoBlockHasNotMoved() {
        placeOnBoard(pawn, 6, 7);
        placeOnBoard(king, 6, 8);
        white.add(pawn);
        white.add(king);
        setGame();
        ArrayList<Position> pm = pawn.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(6, 5));
        expected.add(new Position(6, 6));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesNoBlockHasMoved() {
        placeOnBoard(pawn, 8, 7);
        placeOnBoard(king, 6, 8);
        white.add(pawn);
        white.add(king);
        setGame();
        game.move(pawn, 8, 5);
        ArrayList<Position> pm = pawn.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(8, 4));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesBlockByTeammate() {
        placeOnBoard(pawn, 1, 7);
        placeOnBoard(king, 6, 8);
        Bishop bishop = new Bishop("white");
        placeOnBoard(bishop, 1, 6);
        white.add(pawn);
        white.add(king);
        white.add(bishop);
        setGame();
        ArrayList<Position> pm = pawn.possibleMoves(game);
        assertEquals(0, pm.size());
    }

    @Test
    public void testPossibleMovesDiagonalToTeammateCannotAttack() {
        placeOnBoard(pawn, 6, 7);
        placeOnBoard(king, 6, 8);
        Bishop bishop = new Bishop("white");
        placeOnBoard(bishop, 5, 6);
        white.add(pawn);
        white.add(king);
        white.add(bishop);
        setGame();
        ArrayList<Position> pm = pawn.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(6, 5));
        expected.add(new Position(6, 6));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesNoSpaceInFront() {
        Pawn pawn1 = new Pawn("white");
        Pawn pawn2 = new Pawn("white");
        Pawn pawn3 = new Pawn("black");
        Pawn pawn4 = new Pawn("black");
        Pawn pawn5 = new Pawn("black");
        King blackKing = new King("black");
        placeOnBoard(pawn, 6, 1);
        placeOnBoard(pawn1, 1, 1);
        placeOnBoard(pawn2, 8, 1);
        placeOnBoard(pawn3, 6, 8);
        placeOnBoard(pawn4, 1, 8);
        placeOnBoard(pawn5, 8, 8);
        placeOnBoard(king, 8, 5);
        placeOnBoard(blackKing, 1, 5);
        white.add(pawn);
        white.add(pawn1);
        white.add(pawn2);
        white.add(king);
        black.add(pawn3);
        black.add(pawn4);
        black.add(pawn5);
        black.add(blackKing);
        setGame();
        assertEquals(0, pawn.possibleMoves(game).size());
        assertEquals(0, pawn1.possibleMoves(game).size());
        assertEquals(0, pawn2.possibleMoves(game).size());
        assertEquals(0, pawn3.possibleMoves(game).size());
        assertEquals(0, pawn4.possibleMoves(game).size());
        assertEquals(0, pawn5.possibleMoves(game).size());
    }

    @Test
    public void testPossibleMovesBlockByEnemy() {
        placeOnBoard(pawn, 6, 7);
        placeOnBoard(king, 6, 8);
        Bishop bishop = new Bishop("black");
        placeOnBoard(bishop, 6, 6);
        white.add(pawn);
        white.add(king);
        black.add(bishop);
        setGame();
        ArrayList<Position> pm = pawn.possibleMoves(game);
        assertEquals(0, pm.size());
    }

    @Test
    public void testPossibleMovesCanAttack() {
        placeOnBoard(pawn, 4, 5);
        placeOnBoard(king, 4, 6);
        Bishop bishop = new Bishop("black");
        placeOnBoard(bishop, 3, 4);
        Rook rook = new Rook("black");
        placeOnBoard(rook, 5, 4);
        white.add(pawn);
        white.add(king);
        black.add(bishop);
        black.add(rook);
        setGame();
        ArrayList<Position> pm = pawn.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(4, 3));
        expected.add(new Position(4, 4));
        expected.add(new Position(3, 4));
        expected.add(new Position(5, 4));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesKingUnderAttack() {
        placeOnBoard(pawn, 4, 5);
        placeOnBoard(king, 5, 5);
        Bishop bishop = new Bishop("black");
        placeOnBoard(bishop, 7, 3);
        white.add(pawn);
        white.add(king);
        black.add(bishop);
        setGame();
        ArrayList<Position> pm = pawn.possibleMoves(game);
        assertEquals(0, pm.size());
    }

    @Test
    public void testPossibleMovesKingWillBeUnderAttack() {
        placeOnBoard(pawn, 5, 4);
        placeOnBoard(king, 5, 5);
        Bishop bishop = new Bishop("black");
        placeOnBoard(bishop, 6, 3);
        Rook rook = new Rook("black");
        placeOnBoard(rook, 5, 3);
        white.add(pawn);
        white.add(king);
        black.add(bishop);
        black.add(rook);
        setGame();
        ArrayList<Position> pm = pawn.possibleMoves(game);
        assertEquals(0, pm.size());
    }

    @Test
    public void testCheckingEnemy() {
        placeOnBoard(pawn, 6, 7);
        placeOnBoard(king, 6, 8);
        white.add(pawn);
        white.add(king);
        setGame();
        Position pos1 = new Position(4, 6);
        Position pos2 = new Position(5, 6);
        Position pos3 = new Position(6, 6);
        Position pos4 = new Position(6, 5);
        assertFalse(pawn.checkEnemy(game, pos1));
        assertTrue(pawn.checkEnemy(game, pos2));
        assertFalse(pawn.checkEnemy(game, pos3));
        assertFalse(pawn.checkEnemy(game, pos4));
    }
}
