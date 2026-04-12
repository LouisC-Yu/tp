package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.AddSupplierCommand.MESSAGE_INCORRECT_TIME_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_FORMAT_OPENING_HOURS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_HOUR_OPENING_HOURS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_MINUTE_OPENING_HOURS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.OPENING_HOURS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_VEGETABLE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_OPENING_HOURS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_VEGETABLE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddSupplierCommand;
import seedu.address.model.person.Supplier;
import seedu.address.testutil.SupplierBuilder;

public class AddSupplierCommandParserTest {

    private final AddSupplierCommandParser parser = new AddSupplierCommandParser();

    @Test
    public void parse_allFieldsValid_success() {
        Supplier validSupplier = new SupplierBuilder()
                .withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB)
                .withOpeningHours(VALID_OPENING_HOURS)
                .withTags(VALID_TAG_VEGETABLE)
                .build();

        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + OPENING_HOURS_DESC + TAG_DESC_VEGETABLE,
                new AddSupplierCommand(validSupplier));
    }

    @Test
    public void parse_compulsoryPersonFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSupplierCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser,
                VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + OPENING_HOURS_DESC + TAG_DESC_VEGETABLE,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser,
                NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + OPENING_HOURS_DESC + TAG_DESC_VEGETABLE,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB + ADDRESS_DESC_BOB
                        + OPENING_HOURS_DESC + TAG_DESC_VEGETABLE,
                expectedMessage);

        // missing address prefix
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + VALID_ADDRESS_BOB
                        + OPENING_HOURS_DESC + TAG_DESC_VEGETABLE,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_ADDRESS_BOB
                        + VALID_OPENING_HOURS + VALID_TAG_VEGETABLE,
                expectedMessage);
    }

    @Test
    public void parse_openingHoursMissing_failure() {
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + ADDRESS_DESC_BOB + TAG_DESC_VEGETABLE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSupplierCommand.MESSAGE_OPENING_HOURS_REQUIRED));
    }

    @Test
    public void parse_tagsMissing_failure() {
        assertParseFailure(parser,
            NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                    + ADDRESS_DESC_BOB + OPENING_HOURS_DESC,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSupplierCommand.MESSAGE_TAG_REQUIRED));
    }

    @Test
    public void parse_invalidFormatOpeningHours_throwsParseException() {
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + INVALID_FORMAT_OPENING_HOURS_DESC + TAG_DESC_VEGETABLE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_INCORRECT_TIME_FORMAT));
    }

    @Test
    public void parse_invalidMinuteValueOpeningHours_throwsParseException() {
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + INVALID_MINUTE_OPENING_HOURS_DESC + TAG_DESC_VEGETABLE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSupplierCommand.MESSAGE_INCORRECT_TIME_FORMAT));
    }

    @Test
    public void parse_invalidHourValueOpeningHours_throwsParseException() {
        assertParseFailure(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + INVALID_HOUR_OPENING_HOURS_DESC + TAG_DESC_VEGETABLE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_INCORRECT_TIME_FORMAT));
    }
}
