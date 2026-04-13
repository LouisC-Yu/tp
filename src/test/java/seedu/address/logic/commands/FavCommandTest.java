package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
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
 * {@code FavCommand}.
 */
public class FavCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToFav = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        FavCommand favCommand = new FavCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(FavCommand.MESSAGE_SUCCESS,
                Messages.format(personToFav));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.saveStateForUndo();
        expectedModel.setPersonAsFavourite(personToFav);

        assertCommandSuccess(favCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        FavCommand favCommand = new FavCommand(outOfBoundIndex);

        assertCommandFailure(favCommand, model, Messages.MESSAGE_INDEX_NOT_IN_LIST);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToFav = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        FavCommand favCommand = new FavCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(FavCommand.MESSAGE_SUCCESS,
                Messages.format(personToFav));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        expectedModel.saveStateForUndo();
        expectedModel.setPersonAsFavourite(personToFav);

        assertCommandSuccess(favCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        FavCommand favCommand = new FavCommand(outOfBoundIndex);

        assertCommandFailure(favCommand, model, Messages.MESSAGE_INDEX_NOT_IN_LIST);
    }

    @Test
    public void execute_personAtBottom_pushesPersonToTop() {
        Person personToFav = model.getFilteredPersonList().get(INDEX_THIRD_PERSON.getZeroBased());
        FavCommand favCommand = new FavCommand(INDEX_THIRD_PERSON);

        String expectedMessage = String.format(
                FavCommand.MESSAGE_SUCCESS, Messages.format(personToFav));

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setPersonAsFavourite(personToFav);

        assertCommandSuccess(favCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), personToFav);
    }

    @Test
    public void execute_personAlreadyFavourite_throwsCommandException() {
        Person personToFav = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        FavCommand favCommand = new FavCommand(INDEX_FIRST_PERSON);

        model.setPersonAsFavourite(personToFav);

        assertCommandFailure(favCommand, model, FavCommand.MESSAGE_PERSON_ALREADY_FAVOURITE);
    }

    @Test
    public void equals() {
        FavCommand favFirstCommand = new FavCommand(INDEX_FIRST_PERSON);
        FavCommand favSecondCommand = new FavCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(favFirstCommand.equals(favFirstCommand));

        // same values -> returns true
        FavCommand favFirstCommandCopy = new FavCommand(INDEX_FIRST_PERSON);
        assertTrue(favFirstCommand.equals(favFirstCommandCopy));

        // different types -> returns false
        assertFalse(favFirstCommand.equals(1));

        // null -> returns false
        assertFalse(favFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(favFirstCommand.equals(favSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        FavCommand favCommand = new FavCommand(targetIndex);
        String expected = FavCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, favCommand.toString());
    }
}
