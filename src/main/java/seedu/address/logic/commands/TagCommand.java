package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Replaces the tags of an existing person in the address book.
 * Format: tag INDEX t/TAG [t/TAG]...
 * Example: tag 3 t/vegetable t/fruits
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Replaces the tags of the person identified by the "
            + "index number used in the displayed person list.\n"
            + "Parameters: INDEX t/TAG [t/TAG]...\n"
            + "Example: " + COMMAND_WORD + " 3 t/vegetable t/fruits";

    public static final String MESSAGE_SUCCESS = "Updated tags for person: %1$s";
    public static final String MESSAGE_INVALID_INDEX = "Invalid index: the index is out of range.";

    private final Index index;
    private final Set<Tag> tags;

    public TagCommand(Index index, Set<Tag> tags) {
        requireNonNull(index);
        requireNonNull(tags);
        this.index = index;
        this.tags = tags;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        // Replace tags (e.g., default [NONE] becomes the new tags user typed)
        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                tags
        );

        model.setPerson(personToEdit, editedPerson);

        return new CommandResult(String.format(MESSAGE_SUCCESS, editedPerson));
    }
}