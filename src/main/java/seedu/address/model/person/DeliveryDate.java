package seedu.address.model.person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents an Order's delivery datre in the CakeCollate.
 * Guarantees: immutable; is valid as declared in {@link #isValidDeliveryDate(String)}
 */
public class DeliveryDate {

    public static final String MESSAGE_CONSTRAINTS = "Deliver date should be of the format dd/mm/yyyy, dd-mm-yyyy, "
            + "dd.mm.yyyy or dd MMM yyyy and adhere to the following constraints:\n"
            + "1. The delivery date should have a valid day, month and year."
            + "2. The delivery date should be at least 3 days after the order date.";
    private static final DateTimeFormatter ACCEPTABLE_FORMATS =
            DateTimeFormatter.ofPattern("[dd/MM/yyyy][dd-MM-yyyy][dd.MM.yyyy][dd MMM yyyy]");
    private static final long DELIVERY_DATE_BUFFER = 3L;

    public final LocalDate value;

    /**
     * Constructs a {@code DeliveryDate}.
     *
     * @param deliveryDate A valid delivery date.
     */
    public DeliveryDate(String deliveryDate) {
        requireNonNull(deliveryDate);
        checkArgument(isValidDeliveryDate(deliveryDate), MESSAGE_CONSTRAINTS);
        value = LocalDate.parse(deliveryDate, ACCEPTABLE_FORMATS);
    }

    /**
     * Returns true if a given string is a valid date 3 working days after current date.
     */
    public static boolean isThreeDaysLater(String test) {
        LocalDate toTest = LocalDate.parse(test, ACCEPTABLE_FORMATS);
        LocalDate dateToday = LocalDate.now();
        LocalDate acceptableDate = dateToday.plusDays(DELIVERY_DATE_BUFFER);
        return toTest.isAfter(acceptableDate) || toTest.isEqual(acceptableDate);
    }

    /**
     * Returns true if a given string is a valid delivery date.
     */
    public static boolean isValidDeliveryDate(String test) {
        // Test if date is valid using LocalDate.parse()
        try {
            LocalDate.parse(test, ACCEPTABLE_FORMATS);
        } catch (DateTimeParseException e) {
            return false;
        }
        // Test if date is 3 working days after current Date
        return isThreeDaysLater(test);
    }

    @Override
    public String toString() {
        DateTimeFormatter toDisplay = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return toDisplay.format(value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof DeliveryDate
                && value.equals(((DeliveryDate) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
