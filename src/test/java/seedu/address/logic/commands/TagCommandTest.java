package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Supplier;
import seedu.address.model.tag.Tag;

public class TagCommandTest {

    @Test
    public void execute_addTags_success() throws Exception {
        Model model = getModelWith(singlePerson());

        TagCommand command = new TagCommand(
                Index.fromOneBased(1),
                Set.of(new Tag("meat")),
                Set.of(),
                false
        );

        command.execute(model);

        Person updatedPerson = model.getFilteredPersonList().get(0);
        assertTrue(updatedPerson.getTags().contains(new Tag("veg")));
        assertTrue(updatedPerson.getTags().contains(new Tag("meat")));
    }

    @Test
    public void execute_deleteTags_success() throws Exception {
        Model model = getModelWith(personWithTwoTags());

        TagCommand command = new TagCommand(
                Index.fromOneBased(1),
                Set.of(),
                Set.of(new Tag("fish")),
                false
        );

        command.execute(model);

        Person updatedPerson = model.getFilteredPersonList().get(0);
        assertTrue(updatedPerson.getTags().contains(new Tag("veg")));
        assertEquals(1, updatedPerson.getTags().size());
    }

    @Test
    public void execute_clearTags_successForNonSupplier() throws Exception {
        Model model = getModelWith(personWithTwoTags());

        TagCommand command = new TagCommand(
                Index.fromOneBased(1),
                Set.of(),
                Set.of(),
                true
        );

        command.execute(model);

        Person updatedPerson = model.getFilteredPersonList().get(0);
        assertTrue(updatedPerson.getTags().isEmpty());
    }

    @Test
    public void execute_deleteNonExistingTag_failure() {
        Model model = getModelWith(singlePerson());

        TagCommand command = new TagCommand(
                Index.fromOneBased(1),
                Set.of(),
                Set.of(new Tag("fish")),
                false
        );

        CommandException e = org.junit.jupiter.api.Assertions.assertThrows(
                CommandException.class, () -> command.execute(model));
        assertEquals(TagCommand.MESSAGE_TAG_NOT_FOUND, e.getMessage());
    }

    @Test
    public void execute_clearTagsOnSupplier_failure() {
        Model model = getModelWith(singleSupplier());

        TagCommand command = new TagCommand(
                Index.fromOneBased(1),
                Set.of(),
                Set.of(),
                true
        );

        CommandException e = org.junit.jupiter.api.Assertions.assertThrows(
                CommandException.class, () -> command.execute(model));
        assertEquals(TagCommand.MESSAGE_SUPPLIER_MUST_HAVE_TAG, e.getMessage());
    }

    @Test
    public void execute_noAction_failure() {
        Model model = getModelWith(singlePerson());

        TagCommand command = new TagCommand(
                Index.fromOneBased(1),
                Set.of(),
                Set.of(),
                false
        );

        CommandException e = org.junit.jupiter.api.Assertions.assertThrows(
                CommandException.class, () -> command.execute(model));
        assertEquals(TagCommand.MESSAGE_NO_TAG_ACTION, e.getMessage());
    }

    @Test
    public void execute_clearConflict_failure() {
        Model model = getModelWith(singlePerson());

        TagCommand command = new TagCommand(
                Index.fromOneBased(1),
                Set.of(new Tag("fish")),
                Set.of(),
                true
        );

        CommandException e = org.junit.jupiter.api.Assertions.assertThrows(
                CommandException.class, () -> command.execute(model));
        assertEquals(TagCommand.MESSAGE_CLEAR_TAGS_CONFLICT, e.getMessage());
    }

    @Test
    public void execute_invalidIndex_failure() {
        Model model = getModelWith(singlePerson());

        TagCommand command = new TagCommand(
                Index.fromOneBased(2),
                Set.of(new Tag("fish")),
                Set.of(),
                false
        );

        CommandException e = org.junit.jupiter.api.Assertions.assertThrows(
                CommandException.class, () -> command.execute(model));
        assertEquals(Messages.MESSAGE_INDEX_NOT_IN_LIST, e.getMessage());
    }

    private static Model getModelWith(Person person) {
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(person);
        return new ModelManager(addressBook, new UserPrefs());
    }

    private static Person singlePerson() {
        return new Person(
                new Name("Alice"),
                new Phone("91234567"),
                new Email("alice@example.com"),
                new Address("Yishun"),
                "",
                Set.of(new Tag("veg")),
                false
        );
    }

    private static Person personWithTwoTags() {
        return new Person(
                new Name("Bob"),
                new Phone("92345678"),
                new Email("bob@example.com"),
                new Address("Tampines"),
                "",
                Set.of(new Tag("veg"), new Tag("fish")),
                false
        );
    }

    private static Supplier singleSupplier() {
        return new Supplier(
                new Name("AhSeng"),
                new Phone("93456789"),
                new Email("ahseng@example.com"),
                new Address("Jurong"),
                "",
                Set.of(new Tag("veg")),
                false,
                "0900 - 1800",
                new Phone("93456789")
        );
    }
}
