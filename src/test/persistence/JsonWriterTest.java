package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest {

    @Test
    public void testWriterInvalidFile() {
        try {
            Game gm = new Game();
            JsonWriter jsonWriter = new JsonWriter("./data/my\0illegal:fileName.json");
            jsonWriter.open();
            fail("IOException should be thrown");
        } catch (IOException e) {
            // IOException is thrown as expected
        }
    }

    @Test
    public void testWriterDefaultChessGame() {
        try {
            Game gm = new Game();
            JsonWriter jsonWriter = new JsonWriter("./data/jsonWriterTestDefaultChessGame.json");
            jsonWriter.open();
            ;
            jsonWriter.writeGame(gm);
            jsonWriter.close();
            JsonReader jsonReader = new JsonReader("./data/jsonWriterTestDefaultChessGame.json");
            Game compare = jsonReader.readGame();
            checkGame(compare, gm);
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testWriterModifiedChessGame() {
        try {
            Game gm = modifyCompareGame();
            JsonWriter jsonWriter = new JsonWriter("./data/jsonWriterTestModifiedChessGame.json");
            jsonWriter.open();
            ;
            jsonWriter.writeGame(gm);
            jsonWriter.close();
            JsonReader jsonReader = new JsonReader("./data/jsonWriterTestModifiedChessGame.json");
            Game compare = jsonReader.readGame();
            checkGame(compare, gm);
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
}


