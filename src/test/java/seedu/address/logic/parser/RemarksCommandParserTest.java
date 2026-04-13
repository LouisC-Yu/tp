package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT_OR_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.EMPTY_REMARKS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.REMARKS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARKS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARKS_EMPTY;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.RemarksCommand;

public class RemarksCommandParserTest {

    private final RemarksCommandParser parser = new RemarksCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_REMARKS,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT_OR_INDEX, RemarksCommand.MESSAGE_USAGE));

        // no field specified
        assertParseFailure(parser, "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarksCommand.MESSAGE_USAGE));

        // no index and no field specified
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT_OR_INDEX, RemarksCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + REMARKS_DESC,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT_OR_INDEX, RemarksCommand.MESSAGE_USAGE));

        // zero index
        assertParseFailure(parser, "0" + REMARKS_DESC,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT_OR_INDEX, RemarksCommand.MESSAGE_USAGE));

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT_OR_INDEX, RemarksCommand.MESSAGE_USAGE));

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT_OR_INDEX, RemarksCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validRemarks_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + REMARKS_DESC;

        RemarksCommand expectedCommand = new RemarksCommand(targetIndex, VALID_REMARKS);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_emptyRemarks_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + EMPTY_REMARKS_DESC;

        RemarksCommand expectedCommand = new RemarksCommand(targetIndex, VALID_REMARKS_EMPTY);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

}
