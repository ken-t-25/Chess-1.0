package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest extends JsonTest {

    @Test
    public void testReaderNonExistingFile() {
        JsonReader jr = new JsonReader("./data/nullfile.json");
        try {
            Game game = jr.readGame();
            fail("IOException should be thrown");
        } catch (IOException e) {
            // catch exception as expected
        }
    }

    @Test
    public void testReaderDefaultChessGame() {
        JsonReader jr = new JsonReader("./data/jsonReaderTestDefaultChessGame.json");
        try {
            Game game = jr.readGame();
            Game compare = new Game();
            checkGame(compare, game);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReaderModifiedChessGame() {
        JsonReader jr = new JsonReader("./data/jsonReaderTestModifiedChessGame.json");
        try {
            Game game = jr.readGame();
            Game compare = modifyCompareGame();
            checkGame(compare, game);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
}
