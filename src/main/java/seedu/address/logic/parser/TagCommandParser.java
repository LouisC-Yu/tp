package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT_OR_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLEAR_TAGS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE_TAG;

import java.util.Collection;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new TagCommand object.
 */
public class TagCommandParser implements Parser<TagCommand> {

    @Override
    public TagCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ADD_TAG, PREFIX_DELETE_TAG, PREFIX_CLEAR_TAGS);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT_OR_INDEX, TagCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CLEAR_TAGS);

        Collection<String> addTags = argMultimap.getAllValues(PREFIX_ADD_TAG);
        Collection<String> deleteTags = argMultimap.getAllValues(PREFIX_DELETE_TAG);
        boolean clearTags = argMultimap.getValue(PREFIX_CLEAR_TAGS).isPresent();

        if (!clearTags && addTags.isEmpty() && deleteTags.isEmpty()) {
            throw new ParseException(TagCommand.MESSAGE_NO_TAG_ACTION);
        }

        if (clearTags && (!addTags.isEmpty() || !deleteTags.isEmpty())) {
            throw new ParseException(TagCommand.MESSAGE_CLEAR_TAGS_CONFLICT);
        }

        if (addTags.contains("") || deleteTags.contains("")) {
            throw new ParseException(TagCommand.MESSAGE_SUPPLIER_MUST_HAVE_TAG);
        }

        Set<Tag> addTagSet = ParserUtil.parseTags(addTags);
        Set<Tag> deleteTagSet = ParserUtil.parseTags(deleteTags);

        return new TagCommand(index, addTagSet, deleteTagSet, clearTags);
    }
}
