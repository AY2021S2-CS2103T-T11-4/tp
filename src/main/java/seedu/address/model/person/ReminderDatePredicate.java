package seedu.address.model.person;

import java.time.LocalDate;
import java.util.function.Predicate;

/**
 * Tests that a {@code Orders}'s {@code DeliveryDate} is within 3 days of the current date.
 */
public class ReminderDatePredicate implements Predicate<Person> {

    private long days;

    public ReminderDatePredicate(long days) {
        this.days = days;
    }

    @Override
    public boolean test(Person person) {
        return isWithinXDays(person);
    }

    /**
     * Returns true if the order's delivery date is within X days of the current date.
     */
    public boolean isWithinXDays(Person person) {
        LocalDate toTest = person.getDeliveryDate().getValue();
        LocalDate dateToday = LocalDate.now();
        LocalDate acceptableDate = dateToday.plusDays(days + 1);
        return toTest.isEqual(dateToday) || toTest.isBefore(acceptableDate);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReminderDatePredicate // instanceof handles nulls
                && days == (((ReminderDatePredicate) other).days)); // state check
    }

}

