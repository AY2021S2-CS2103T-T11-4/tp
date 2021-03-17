package seedu.address.testutil;

import seedu.address.model.AddressBook;
import seedu.address.model.order.Order;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code AddressBook ab = new AddressBookBuilder().withOrder("John", "Doe").build();}
 */
public class AddressBookBuilder {

    private AddressBook addressBook;

    public AddressBookBuilder() {
        addressBook = new AddressBook();
    }

    public AddressBookBuilder(AddressBook addressBook) {
        this.addressBook = addressBook;
    }

    /**
     * Adds a new {@code Order} to the {@code AddressBook} that we are building.
     */
    public AddressBookBuilder withOrder(Order order) {
        addressBook.addOrder(order);
        return this;
    }

    public AddressBook build() {
        return addressBook;
    }
}
