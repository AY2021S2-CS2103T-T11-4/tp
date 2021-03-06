package seedu.cakecollate.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.cakecollate.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.cakecollate.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.cakecollate.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.cakecollate.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.cakecollate.logic.parser.CliSyntax.PREFIX_ORDER_DESCRIPTION;
import static seedu.cakecollate.logic.parser.CliSyntax.PREFIX_ORDER_ITEM_IDX;
import static seedu.cakecollate.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.cakecollate.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.cakecollate.testutil.Assert.assertThrows;
import static seedu.cakecollate.testutil.TypicalIndexes.INDEX_FIRST_ORDER;
import static seedu.cakecollate.testutil.TypicalIndexes.INDEX_THIRD_ORDER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import seedu.cakecollate.commons.core.index.Index;
import seedu.cakecollate.commons.core.index.IndexList;
import seedu.cakecollate.logic.commands.exceptions.CommandException;
import seedu.cakecollate.logic.parser.Prefix;
import seedu.cakecollate.model.CakeCollate;
import seedu.cakecollate.model.Model;
import seedu.cakecollate.model.OrderItems;
import seedu.cakecollate.model.order.ContainsKeywordsPredicate;
import seedu.cakecollate.model.order.Order;
import seedu.cakecollate.model.orderitem.OrderItem;
import seedu.cakecollate.model.orderitem.Type;
import seedu.cakecollate.testutil.EditOrderDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    //the following variables are for an OrderItem
    public static final String VALID_TYPE_CHOCOLATE = "Chocolate cake";
    public static final String VALID_TYPE_STRAWBERRY = "Strawberry cake";

    //the following variables are for an Order
    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_CHOCOLATE_ORDER = "Amys Chocolate Cakes";
    public static final String VALID_BERRY_ORDER = "Bobs Berry Cakes";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";
    public static final String VALID_DELIVERY_DATE_AMY = "01/01/2022";
    public static final String VALID_DELIVERY_DATE_BOB = "02/01/2022";
    public static final String VALID_REQUEST_AMY = "More tomatoes.";
    public static final String VALID_REQUEST_BOB = "Less sugar";

    // these set of fields prefix the variables above with the right prefixes
    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    public static final String ORDER_DESC_AMY = " " + PREFIX_ORDER_DESCRIPTION + VALID_CHOCOLATE_ORDER;
    public static final String ORDER_DESC_BOB = " " + PREFIX_ORDER_DESCRIPTION + VALID_BERRY_ORDER;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;
    public static final String DELIVERY_DATE_DESC_AMY = " " + PREFIX_DATE + VALID_DELIVERY_DATE_AMY;
    public static final String DELIVERY_DATE_DESC_BOB = " " + PREFIX_DATE + VALID_DELIVERY_DATE_BOB;

    // corresponding order item to add into order items model, needed for building expected models during testing
    public static final OrderItem ORDER_ITEM_AMY = new OrderItem(new Type(VALID_CHOCOLATE_ORDER));
    public static final OrderItem ORDER_ITEM_BOB = new OrderItem(new Type(VALID_BERRY_ORDER));

    public static final String INDEXES_2 = "1 3";
    public static final String ORDER_ITEM_INDEXES_1 = " " + PREFIX_ORDER_ITEM_IDX + "1 2 4";
    public static final String ORDER_ITEM_INDEXES_2 = " " + PREFIX_ORDER_ITEM_IDX + INDEXES_2;

    // these are some user input fields with prefixes, invalid according to validation specified in model classes
    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_ORDER_DESC = " " + PREFIX_ORDER_DESCRIPTION; // empty string not allowed
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags
    public static final String INVALID_DELIVERY_DATE_DESC1 = " " + PREFIX_DATE + "2021/03/03"; // invalid format
    public static final String INVALID_DELIVERY_DATE_DESC2 = " " + PREFIX_DATE + "01/01/2000"; // invalid value

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditOrderDescriptor DESC_AMY;
    public static final EditCommand.EditOrderDescriptor DESC_BOB;

    public static final IndexList ORDER_ITEM_INDEXLIST_2;

    static {
        DESC_AMY = new EditOrderDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withOrderDescriptions(VALID_CHOCOLATE_ORDER).withTags(VALID_TAG_FRIEND)
                .withDeliveryDate(VALID_DELIVERY_DATE_AMY).build();
        DESC_BOB = new EditOrderDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withOrderDescriptions(VALID_BERRY_ORDER).withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
                .withDeliveryDate(VALID_DELIVERY_DATE_BOB).build();


        List<Index> l = new ArrayList<>();
        l.add(INDEX_FIRST_ORDER);
        l.add(INDEX_THIRD_ORDER);
        ORDER_ITEM_INDEXLIST_2 = new IndexList(l);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
                                            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
                                            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the cakecollate, filtered order list and selected order in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        CakeCollate expectedCakeCollate = new CakeCollate(actualModel.getCakeCollate());
        List<Order> expectedFilteredList = new ArrayList<>(actualModel.getFilteredOrderList());

        OrderItems expectedOrderItems = new OrderItems(actualModel.getOrderItems());
        List<OrderItem> expectedOrderItemsList = new ArrayList<>(actualModel.getFilteredOrderItemsList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedCakeCollate, actualModel.getCakeCollate());
        assertEquals(expectedFilteredList, actualModel.getFilteredOrderList());
        assertEquals(expectedOrderItems, actualModel.getOrderItems());
        assertEquals(expectedOrderItemsList, actualModel.getFilteredOrderItemsList());
    }

    /**
     * Updates {@code model}'s filtered list to show only the order at the given {@code targetIndex} in the
     * {@code model}'s cakecollate.
     */
    public static void showOrderAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredOrderList().size());

        Order order = model.getFilteredOrderList().get(targetIndex.getZeroBased());
        final String[] splitName = order.getName().fullName.split("\\s+");
        HashMap<Prefix, List<String>> mapping = new HashMap<>();
        mapping.put(PREFIX_NAME, Arrays.asList(splitName[0]));
        model.updateFilteredOrderList(new ContainsKeywordsPredicate(mapping));

        assertEquals(1, model.getFilteredOrderList().size());
    }


}
