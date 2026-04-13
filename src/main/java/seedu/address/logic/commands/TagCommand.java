package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLEAR_TAGS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Supplier;
import seedu.address.model.tag.Tag;

/**
 * Modifies the tags of an existing person in the address book.
 * Supports adding tags, deleting tags, or clearing all tags.
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Modifies the tags of the person identified by the index number used "
            + "in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_ADD_TAG + "TAG]... "
            + "[" + PREFIX_DELETE_TAG + "TAG]... "
            + "[" + PREFIX_CLEAR_TAGS + "]\n"
            + "You can add tags using at/, delete tags using dt/, "
            + "or clear all tags using ct/.\n"
            + "Examples:\n"
            + COMMAND_WORD + " 2 " + PREFIX_ADD_TAG + "fish " + PREFIX_ADD_TAG + "seafood\n"
            + COMMAND_WORD + " 2 " + PREFIX_DELETE_TAG + "fish\n"
            + COMMAND_WORD + " 2 " + PREFIX_CLEAR_TAGS;

    public static final String MESSAGE_SUCCESS = "Updated tags for: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";
    public static final String MESSAGE_NO_TAG_ACTION =
            "At least one tag action must be provided: at/, dt/, or ct/.";
    public static final String MESSAGE_CLEAR_TAGS_CONFLICT =
            "ct/ cannot be used together with at/ or dt/.";
    public static final String MESSAGE_TAG_NOT_FOUND =
            "One or more tags to delete do not exist on this contact.";
    public static final String MESSAGE_SUPPLIER_MUST_HAVE_TAG =
            "Tag names should be alphanumeric, and suppliers must have at least one tag.";

    private final Index index;
    private final Set<Tag> addTags;
    private final Set<Tag> deleteTags;
    private final boolean clearTags;

    /**
     * Creates a {@code TagCommand}.
     *
     * @param index Index of the person in the filtered person list whose tags will be modified.
     * @param addTags Tags to add.
     * @param deleteTags Tags to delete.
     * @param clearTags Whether to clear all tags.
     */
    public TagCommand(Index index, Set<Tag> addTags, Set<Tag> deleteTags, boolean clearTags) {
        requireNonNull(index);
        requireNonNull(addTags);
        requireNonNull(deleteTags);
        this.index = index;
        this.addTags = addTags;
        this.deleteTags = deleteTags;
        this.clearTags = clearTags;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (clearTags && (!addTags.isEmpty() || !deleteTags.isEmpty())) {
            throw new CommandException(MESSAGE_CLEAR_TAGS_CONFLICT);
        }

        if (!clearTags && addTags.isEmpty() && deleteTags.isEmpty()) {
            throw new CommandException(MESSAGE_NO_TAG_ACTION);
        }

        List<Person> lastShownList = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INDEX_NOT_IN_LIST);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Set<Tag> updatedTags = getUpdatedTags(personToEdit, personToEdit.getTags());

        Person taggedPerson = createTaggedPerson(personToEdit, updatedTags);

        if (!personToEdit.isSamePerson(taggedPerson) && model.hasPerson(taggedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.saveStateForUndo();
        model.setPerson(personToEdit, taggedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(taggedPerson)));
    }

    /**
     * Returns the updated tag set after applying the requested tag actions.
     */
    private Set<Tag> getUpdatedTags(Person personToEdit, Set<Tag> currentTags) throws CommandException {
        Set<Tag> updatedTags = new HashSet<>(currentTags);

        if (clearTags) {
            updatedTags.clear();
        } else {
            if (!updatedTags.containsAll(deleteTags)) {
                throw new CommandException(MESSAGE_TAG_NOT_FOUND);
            }
            updatedTags.removeAll(deleteTags);
            updatedTags.addAll(addTags);
        }

        if (personToEdit instanceof Supplier && updatedTags.isEmpty()) {
            throw new CommandException(MESSAGE_SUPPLIER_MUST_HAVE_TAG);
        }

        return updatedTags;
    }

    /**
     * Creates the tagged version of {@code personToEdit}, preserving subtype-specific fields.
     */
    private static Person createTaggedPerson(Person personToEdit, Set<Tag> tags) {
        if (personToEdit instanceof Supplier supplier) {
            return new Supplier(
                    supplier.getName(),
                    supplier.getPhone(),
                    supplier.getEmail(),
                    supplier.getAddress(),
                    supplier.getRemarks(),
                    tags,
                    supplier.isFavourite(),
                    supplier.getOpeningHours(),
                    supplier.getAlternativeContact()
            );
        }

        return new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                personToEdit.getRemarks(),
                tags,
                personToEdit.isFavourite()
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof TagCommand)) {
            return false;
        }
        TagCommand otherTagCommand = (TagCommand) other;
        return index.equals(otherTagCommand.index)
                && addTags.equals(otherTagCommand.addTags)
                && deleteTags.equals(otherTagCommand.deleteTags)
                && clearTags == otherTagCommand.clearTags;
    }
}
