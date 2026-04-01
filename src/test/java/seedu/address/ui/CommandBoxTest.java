package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CommandBoxTest {

    @Test
    public void computeInsertedText_emptyExisting_replaces() {
        assertEquals("open", CommandBox.computeInsertedText("", "open"));
    }

    @Test
    public void computeInsertedText_lessThanSevenWords_replaces() {
        // 3 words
        assertEquals("adds n/Ah Seng", CommandBox.computeInsertedText("tag 3 t/veg", "adds n/Ah Seng"));
    }

    @Test
    public void computeInsertedText_exactlySevenWords_appends() {
        // 7 words
        assertEquals("a b c d e f g X",
                CommandBox.computeInsertedText("a b c d e f g", " X"));
    }

    @Test
    public void computeInsertedText_moreThanSevenWords_appends() {
        // 8 words
        assertEquals("a b c d e f g h Y",
                CommandBox.computeInsertedText("a b c d e f g h", " Y"));
    }

    @Test
    public void computeInsertedText_trimsBeforeCountingWords() {
        // After trim/split -> 3 words, so replace
        assertEquals("tag 1 t/vegetable",
                CommandBox.computeInsertedText("   a    b   c   ", "tag 1 t/vegetable"));
    }
}