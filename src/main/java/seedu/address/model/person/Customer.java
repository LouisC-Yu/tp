package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.time.LocalTime;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

public class Customer extends Person{
    public Customer(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        super(name, phone, email, address, tags);
    }

    public String getPersonType(){
        return "Customer";
    }
}