package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code UnfavCommand}.
 */
public class UnfavCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToUnfav = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        UnfavCommand favCommand = new UnfavCommand(INDEX_FIRST_PERSON);
        model.setPersonAsFavourite(personToUnfav);

        String expectedMessage = String.format(UnfavCommand.MESSAGE_SUCCESS,
                Messages.format(personToUnfav));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.saveStateForUndo();

        assertCommandSuccess(favCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        UnfavCommand favCommand = new UnfavCommand(outOfBoundIndex);

        assertCommandFailure(favCommand, model, Messages.MESSAGE_INDEX_NOT_IN_LIST);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToUnfav = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        UnfavCommand unfavCommand = new UnfavCommand(INDEX_FIRST_PERSON);
        model.setPersonAsFavourite(personToUnfav);

        String expectedMessage = String.format(UnfavCommand.MESSAGE_SUCCESS,
                Messages.format(personToUnfav));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        expectedModel.saveStateForUndo();

        assertCommandSuccess(unfavCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        UnfavCommand favCommand = new UnfavCommand(outOfBoundIndex);

        assertCommandFailure(favCommand, model, Messages.MESSAGE_INDEX_NOT_IN_LIST);
    }

    @Test
    public void execute_personAlreadyFavourite_throwsCommandException() {
        UnfavCommand favCommand = new UnfavCommand(INDEX_FIRST_PERSON);

        assertCommandFailure(favCommand, model, UnfavCommand.MESSAGE_PERSON_NOT_FAVOURITE);
    }

    @Test
    public void equals() {
        UnfavCommand unfavFirstCommand = new UnfavCommand(INDEX_FIRST_PERSON);
        UnfavCommand unfavSecondCommand = new UnfavCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(unfavFirstCommand.equals(unfavFirstCommand));

        // same values -> returns true
        UnfavCommand unfavFirstCommandCopy = new UnfavCommand(INDEX_FIRST_PERSON);
        assertTrue(unfavFirstCommand.equals(unfavFirstCommandCopy));

        // different types -> returns false
        assertFalse(unfavFirstCommand.equals(1));

        // null -> returns false
        assertFalse(unfavFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(unfavFirstCommand.equals(unfavSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        UnfavCommand favCommand = new UnfavCommand(targetIndex);
        String expected = UnfavCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, favCommand.toString());
    }
}
