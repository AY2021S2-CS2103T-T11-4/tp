package seedu.cakecollate.model.orderitem;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.cakecollate.logic.commands.CommandTestUtil.VALID_TYPE_STRAWBERRY;
import static seedu.cakecollate.testutil.TypicalOrderItems.CHOCOLATE;
import static seedu.cakecollate.testutil.TypicalOrderItems.STRAWBERRY;

import org.junit.jupiter.api.Test;

import seedu.cakecollate.testutil.OrderItemBuilder;


public class OrderItemTest {

    @Test
    public void equals() {
        //same values -> return true
        OrderItem chocolateCopy = new OrderItemBuilder(CHOCOLATE).build();
        assertTrue(CHOCOLATE.equals(chocolateCopy));

        //same object -> returns true
        assertTrue(CHOCOLATE.equals(CHOCOLATE));

        //null -> returns false
        assertFalse(CHOCOLATE.equals(null));

        //different data type -> returns false
        assertFalse(CHOCOLATE.equals(5));

        //different order item -> returns false
        assertFalse(CHOCOLATE.equals(STRAWBERRY));

        // different type -> returns false
        OrderItem editedChocolate = new OrderItemBuilder(CHOCOLATE).withType(VALID_TYPE_STRAWBERRY).build();
        assertFalse(CHOCOLATE.equals(editedChocolate));

    }

}
