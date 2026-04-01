package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CommandBoxTest {

    @Test
    public void getUpdatedCommandText_fewerThanSevenWords_replaces() {
        String existing = "tag 1"; // 2 words (< 7)
        String inserted = "open";

        assertEquals("open", CommandBox.getUpdatedCommandText(existing, inserted));
    }

    @Test
    public void getUpdatedCommandText_sevenOrMoreWords_appends() {
        String existing = "one two three four five six seven"; // 7 words (>= 7)
        String inserted = " eight"; // include leading space if you want spacing

        assertEquals("one two three four five six seven eight",
                CommandBox.getUpdatedCommandText(existing, inserted));
    }

    @Test
    public void getUpdatedCommandText_emptyExisting_replaces() {
        String existing = "";
        String inserted = "list";

        assertEquals("list", CommandBox.getUpdatedCommandText(existing, inserted));
    }
}
