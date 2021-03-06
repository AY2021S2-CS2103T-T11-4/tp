package seedu.cakecollate.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.cakecollate.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.cakecollate.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.cakecollate.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.cakecollate.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.cakecollate.logic.parser.CliSyntax.PREFIX_ORDER_DESCRIPTION;
import static seedu.cakecollate.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.cakecollate.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.cakecollate.model.Model.PREDICATE_SHOW_ALL_ORDERS;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import seedu.cakecollate.commons.core.Messages;
import seedu.cakecollate.commons.core.index.Index;
import seedu.cakecollate.commons.util.CollectionUtil;
import seedu.cakecollate.logic.commands.exceptions.CommandException;
import seedu.cakecollate.model.Model;
import seedu.cakecollate.model.order.Address;
import seedu.cakecollate.model.order.DeliveryDate;
import seedu.cakecollate.model.order.DeliveryStatus;
import seedu.cakecollate.model.order.Email;
import seedu.cakecollate.model.order.Name;
import seedu.cakecollate.model.order.Order;
import seedu.cakecollate.model.order.OrderDescription;
import seedu.cakecollate.model.order.Phone;
import seedu.cakecollate.model.order.Request;
import seedu.cakecollate.model.orderitem.OrderItem;
import seedu.cakecollate.model.orderitem.Type;
import seedu.cakecollate.model.tag.Tag;

/**
 * Edits the details of an existing order in CakeCollate.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the order identified "
            + "by the index number used in the displayed order list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE_NUMBER] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_DATE + "DELIVERY_DATE]"
            + "[" + PREFIX_ORDER_DESCRIPTION + "ORDER_DESCRIPTION]... "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_ORDER_SUCCESS = "Edited order: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_ORDER = "This order already exists in CakeCollate.\n"
            + "Note that Name, Address, Order Description and Delivery Date are taken into consideration "
            + "when checking for duplicated orders.";
    public static final String MESSAGE_NO_CHANGE = "No changes were made. Edit will not change the specified order.";

    private final Index index;
    private final EditOrderDescriptor editOrderDescriptor;

    /**
     * @param index of the order in the filtered order list to edit
     * @param editOrderDescriptor details to edit the order with
     */
    public EditCommand(Index index, EditOrderDescriptor editOrderDescriptor) {
        requireNonNull(index);
        requireNonNull(editOrderDescriptor);
        this.index = index;
        this.editOrderDescriptor = new EditOrderDescriptor(editOrderDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Order> lastShownList = model.getFilteredOrderList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ORDER_DISPLAYED_INDEX);
        }

        Order orderToEdit = lastShownList.get(index.getZeroBased());
        Order editedOrder = createEditedOrder(orderToEdit, editOrderDescriptor);

        if (!orderToEdit.isSameOrder(editedOrder) && model.hasOrder(editedOrder)) {
            throw new CommandException(MESSAGE_DUPLICATE_ORDER);
        }

        if (orderToEdit.equals(editedOrder)) {
            throw new CommandException(MESSAGE_NO_CHANGE);
        }

        addToOrderItemsModel(model);

        model.setOrder(orderToEdit, editedOrder);
        model.updateFilteredOrderList(PREDICATE_SHOW_ALL_ORDERS);
        return new CommandResult(String.format(MESSAGE_EDIT_ORDER_SUCCESS, editedOrder));
    }

    /**
     * Creates and returns a {@code Order} with the details of {@code orderToEdit}
     * edited with {@code editOrderDescriptor}.
     */
    private static Order createEditedOrder(Order orderToEdit, EditOrderDescriptor editOrderDescriptor) {
        assert orderToEdit != null;

        Name updatedName = editOrderDescriptor.getName().orElse(orderToEdit.getName());
        Phone updatedPhone = editOrderDescriptor.getPhone().orElse(orderToEdit.getPhone());
        Email updatedEmail = editOrderDescriptor.getEmail().orElse(orderToEdit.getEmail());
        Address updatedAddress = editOrderDescriptor.getAddress().orElse(orderToEdit.getAddress());
        Map<OrderDescription, Integer> updatedOrderDescriptions =
                editOrderDescriptor.getOrderDescriptions().orElse(orderToEdit.getOrderDescriptions());
        Set<Tag> updatedTags = editOrderDescriptor.getTags().orElse(orderToEdit.getTags());
        DeliveryDate updatedDeliveryDate =
                editOrderDescriptor.getDeliveryDate().orElse(orderToEdit.getDeliveryDate());
        DeliveryStatus deliveryStatus = orderToEdit.getDeliveryStatus();
        // Normal edit command does not support editing request but this is here to support test cases for edit command
        Request updatedRequest = editOrderDescriptor.getRequest().orElse(orderToEdit.getRequest());

        return new Order(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedOrderDescriptions,
                updatedTags, updatedDeliveryDate, deliveryStatus, updatedRequest);
    }

    private void addToOrderItemsModel(Model model) {
        if (this.editOrderDescriptor.getOrderDescriptions().isEmpty()) {
            return;
        }

        this.editOrderDescriptor.getOrderDescriptions().get().keySet().stream()
                .map(OrderDescription::getValue) // because a string is needed for creating a new Type for new OrderItem
                .map(o -> new OrderItem(new Type(o))) // map to order item so can check if already in model
                .filter(o -> !model.hasOrderItem(o)) // filters out items that already exist in model
                .forEach(model::addOrderItem);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editOrderDescriptor.equals(e.editOrderDescriptor);
    }

    /**
     * Stores the details to edit the order with. Each non-empty field value will replace the
     * corresponding field value of the order.
     */
    public static class EditOrderDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Map<OrderDescription, Integer> orderDescriptions;
        private Set<Tag> tags;
        private DeliveryDate deliveryDate;
        private Request request;

        public EditOrderDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code orderDescriptions} and {@code tags} is used internally.
         */
        public EditOrderDescriptor(EditOrderDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setOrderDescriptions(toCopy.orderDescriptions);
            setTags(toCopy.tags);
            setDeliveryDate(toCopy.deliveryDate);
            setRequest(toCopy.request);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, orderDescriptions, tags, deliveryDate);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setOrderDescriptions(Map<OrderDescription, Integer> orderDescriptions) {
            this.orderDescriptions = (orderDescriptions != null) ? new HashMap<>(orderDescriptions) : null;
        }

        public Optional<Map<OrderDescription, Integer>> getOrderDescriptions() {
            return (orderDescriptions != null)
                    ? Optional.of(Collections.unmodifiableMap(orderDescriptions))
                    : Optional.empty();
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        public void setDeliveryDate(DeliveryDate deliveryDate) {
            this.deliveryDate = deliveryDate;
        }

        public Optional<DeliveryDate> getDeliveryDate() {
            return Optional.ofNullable(deliveryDate);
        }

        public void setRequest(Request request) {
            this.request = request;
        }

        public Optional<Request> getRequest() {
            return Optional.ofNullable(request);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditOrderDescriptor)) {
                return false;
            }

            // state check
            EditOrderDescriptor e = (EditOrderDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getAddress().equals(e.getAddress())
                    && getOrderDescriptions().equals(e.getOrderDescriptions())
                    && getTags().equals(e.getTags())
                    && getDeliveryDate().equals(e.getDeliveryDate());
        }
    }
}
