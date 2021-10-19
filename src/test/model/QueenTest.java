package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class QueenTest {

    Queen queen;
    King king;
    Game game;
    Board bd;
    ArrayList<ChessPiece> white;
    ArrayList<ChessPiece> black;
    ArrayList<ChessPiece> whiteOff;
    ArrayList<ChessPiece> blackOff;

    @BeforeEach
    private void setup() {
        queen = new Queen("white");
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
    public void testPossibleMovesNoBlockMiddle() {
        placeOnBoard(queen, 5, 4);
        placeOnBoard(king,3,3);
        white.add(queen);
        white.add(king);
        setGame();
        ArrayList<Position> pm = queen.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(4, 3));
        expected.add(new Position(3, 2));
        expected.add(new Position(2, 1));
        expected.add(new Position(4, 5));
        expected.add(new Position(3, 6));
        expected.add(new Position(2, 7));
        expected.add(new Position(1, 8));
        expected.add(new Position(6, 3));
        expected.add(new Position(7, 2));
        expected.add(new Position(8, 1));
        expected.add(new Position(6, 5));
        expected.add(new Position(7, 6));
        expected.add(new Position(8, 7));
        expected.add(new Position(5, 3));
        expected.add(new Position(5, 2));
        expected.add(new Position(5, 1));
        expected.add(new Position(5, 5));
        expected.add(new Position(5, 6));
        expected.add(new Position(5, 7));
        expected.add(new Position(5, 8));
        expected.add(new Position(4, 4));
        expected.add(new Position(3, 4));
        expected.add(new Position(2, 4));
        expected.add(new Position(1, 4));
        expected.add(new Position(6, 4));
        expected.add(new Position(7, 4));
        expected.add(new Position(8, 4));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesNoBlockSide() {
        placeOnBoard(queen, 1, 6);
        placeOnBoard(king,2,4);
        white.add(queen);
        white.add(king);
        setGame();
        ArrayList<Position> pm = queen.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(2, 5));
        expected.add(new Position(3, 4));
        expected.add(new Position(4, 3));
        expected.add(new Position(5, 2));
        expected.add(new Position(6, 1));
        expected.add(new Position(2, 7));
        expected.add(new Position(3, 8));
        expected.add(new Position(1, 5));
        expected.add(new Position(1, 4));
        expected.add(new Position(1, 3));
        expected.add(new Position(1, 2));
        expected.add(new Position(1, 1));
        expected.add(new Position(1, 7));
        expected.add(new Position(1, 8));
        expected.add(new Position(2, 6));
        expected.add(new Position(3, 6));
        expected.add(new Position(4, 6));
        expected.add(new Position(5, 6));
        expected.add(new Position(6, 6));
        expected.add(new Position(7, 6));
        expected.add(new Position(8, 6));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesNoBlockCorner() {
        placeOnBoard(queen, 1, 1);
        placeOnBoard(king,2,3);
        white.add(queen);
        white.add(king);
        setGame();
        ArrayList<Position> pm = queen.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(2, 2));
        expected.add(new Position(3, 3));
        expected.add(new Position(4, 4));
        expected.add(new Position(5, 5));
        expected.add(new Position(6, 6));
        expected.add(new Position(7, 7));
        expected.add(new Position(8, 8));
        expected.add(new Position(1, 2));
        expected.add(new Position(1, 3));
        expected.add(new Position(1, 4));
        expected.add(new Position(1, 5));
        expected.add(new Position(1, 6));
        expected.add(new Position(1, 7));
        expected.add(new Position(1, 8));
        expected.add(new Position(2, 1));
        expected.add(new Position(3, 1));
        expected.add(new Position(4, 1));
        expected.add(new Position(5, 1));
        expected.add(new Position(6, 1));
        expected.add(new Position(7, 1));
        expected.add(new Position(8, 1));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesBlockByTeammate() {
        placeOnBoard(queen, 5, 4);
        placeOnBoard(king,4,2);
        Rook rook1 = new Rook("white");
        Rook rook2 = new Rook("white");
        Bishop bishop = new Bishop("white");
        placeOnBoard(rook1,5,2);
        placeOnBoard(rook2,8,4);
        placeOnBoard(bishop,3,6);
        white.add(queen);
        white.add(king);
        white.add(rook1);
        white.add(rook2);
        white.add(bishop);
        setGame();
        ArrayList<Position> pm = queen.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(4, 3));
        expected.add(new Position(3, 2));
        expected.add(new Position(2, 1));
        expected.add(new Position(4, 5));
        expected.add(new Position(6, 3));
        expected.add(new Position(7, 2));
        expected.add(new Position(8, 1));
        expected.add(new Position(6, 5));
        expected.add(new Position(7, 6));
        expected.add(new Position(8, 7));
        expected.add(new Position(5, 3));
        expected.add(new Position(5, 5));
        expected.add(new Position(5, 6));
        expected.add(new Position(5, 7));
        expected.add(new Position(5, 8));
        expected.add(new Position(4, 4));
        expected.add(new Position(3, 4));
        expected.add(new Position(2, 4));
        expected.add(new Position(1, 4));
        expected.add(new Position(6, 4));
        expected.add(new Position(7, 4));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesBlockByEnemy() {
        placeOnBoard(queen, 5, 4);
        placeOnBoard(king,1,1);
        Rook rook1 = new Rook("black");
        Rook rook2 = new Rook("black");
        Bishop bishop = new Bishop("black");
        placeOnBoard(rook1,5,2);
        placeOnBoard(rook2,8,4);
        placeOnBoard(bishop,3,6);
        white.add(queen);
        white.add(king);
        black.add(rook1);
        black.add(rook2);
        black.add(bishop);
        setGame();
        ArrayList<Position> pm = queen.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(4, 3));
        expected.add(new Position(3, 2));
        expected.add(new Position(2, 1));
        expected.add(new Position(4, 5));
        expected.add(new Position(3, 6));
        expected.add(new Position(6, 3));
        expected.add(new Position(7, 2));
        expected.add(new Position(8, 1));
        expected.add(new Position(6, 5));
        expected.add(new Position(7, 6));
        expected.add(new Position(8, 7));
        expected.add(new Position(5, 3));
        expected.add(new Position(5, 2));
        expected.add(new Position(5, 5));
        expected.add(new Position(5, 6));
        expected.add(new Position(5, 7));
        expected.add(new Position(5, 8));
        expected.add(new Position(4, 4));
        expected.add(new Position(3, 4));
        expected.add(new Position(2, 4));
        expected.add(new Position(1, 4));
        expected.add(new Position(6, 4));
        expected.add(new Position(7, 4));
        expected.add(new Position(8, 4));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesKingUnderAttack() {
        placeOnBoard(queen, 5, 4);
        Rook rook = new Rook("black");
        placeOnBoard(king,3,6);
        placeOnBoard(rook,3,2);
        white.add(queen);
        white.add(king);
        black.add(rook);
        setGame();
        ArrayList<Position> pm = queen.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(3, 2));
        expected.add(new Position(3, 4));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesKingWillBeUnderAttack() {
        placeOnBoard(queen, 3, 5);
        Rook rook = new Rook("black");
        placeOnBoard(king,3,6);
        placeOnBoard(rook,3,2);
        white.add(queen);
        white.add(king);
        black.add(rook);
        setGame();
        ArrayList<Position> pm = queen.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(3, 4));
        expected.add(new Position(3, 3));
        expected.add(new Position(3, 2));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }
}
