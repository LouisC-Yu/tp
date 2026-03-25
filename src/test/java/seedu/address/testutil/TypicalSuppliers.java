package seedu.address.testutil;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Supplier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

/**
 * A utility class containing a list of {@code Supplier} objects to be used in tests.
 */
public class TypicalSuppliers {

    public static final Supplier ALI = new SupplierBuilder().withName("Mohammed Ali")
            .withAddress("111A, Tengah Rise, #12-456").withEmail("ali@example.com")
            .withPhone("94351253")
            .withTags("friends").withOpeningHours("0000 - 2359").withAlternativeContact("91234567").build();
    public static final Supplier BEN = new SupplierBuilder().withName("Ben Leong")
            .withAddress("234A Seletar Street 25, #02-34")
            .withEmail("benedict@example.com").withPhone("98765432")
            .withTags("borrowMoney", "fishmonger").withOpeningHours("0700 - 2300")
            .withAlternativeContact("98765432").build();
    public static final Supplier CHANG = new SupplierBuilder().withName("Chang Long").withPhone("81234567")
            .withEmail("changlong@example.com").withAddress("777 Simpang Terrace")
            .withOpeningHours("1200 - 1600").withAlternativeContact("87654321").build();

    private TypicalSuppliers() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical suppliers.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Supplier supplier : getTypicalSuppliers()) {
            ab.addPerson(supplier);
        }
        return ab;
    }

    public static List<Supplier> getTypicalSuppliers() {
        return new ArrayList<>(Arrays.asList(ALI, BEN, CHANG));
    }
}
