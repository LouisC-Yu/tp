package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT_OR_INDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FavCommand;

public class FavCommandParserTest {

    private FavCommandParser parser = new FavCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteCommand() {
        assertParseSuccess(parser, "1", new FavCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // not integer
        assertParseFailure(parser, "a", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT_OR_INDEX, FavCommand.MESSAGE_USAGE));

        // 0
        assertParseFailure(parser, "0", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT_OR_INDEX, FavCommand.MESSAGE_USAGE));

        // negative
        assertParseFailure(parser, "-1", String.format(
                MESSAGE_INVALID_COMMAND_FORMAT_OR_INDEX, FavCommand.MESSAGE_USAGE));
    }
}
