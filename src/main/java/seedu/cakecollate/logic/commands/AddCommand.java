package seedu.cakecollate.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.cakecollate.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.cakecollate.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.cakecollate.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.cakecollate.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.cakecollate.logic.parser.CliSyntax.PREFIX_ORDER_DESCRIPTION;
import static seedu.cakecollate.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.cakecollate.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.cakecollate.commons.core.Messages;
import seedu.cakecollate.commons.core.index.Index;
import seedu.cakecollate.commons.core.index.IndexList;
import seedu.cakecollate.logic.commands.exceptions.CommandException;
import seedu.cakecollate.model.Model;
import seedu.cakecollate.model.order.Address;
import seedu.cakecollate.model.order.DeliveryDate;
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
 * Adds an order to CakeCollate.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an order to CakeCollate. \n"
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_ORDER_DESCRIPTION + "ORDER DESCRIPTION... "
            + "[" + PREFIX_TAG + "TAG]..."
            + PREFIX_DATE + "DELIVERY_DATE \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_ORDER_DESCRIPTION + "2 x Chocolate Cakes "
            + PREFIX_ORDER_DESCRIPTION + "2 x Strawberry Cakes "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney "
            + PREFIX_DATE + "01/01/2022";

    public static final String MESSAGE_SUCCESS = "New order added: %1$s";
    public static final String MESSAGE_DUPLICATE_ORDER = "This order already exists in CakeCollate";

    private final IndexList orderItemIndexList;
    private final AddOrderDescriptor addOrderDescriptor;


    /**
     * Creates an add command object with indexlist containing the indexes passed in by user,
     * and a descriptor that contains other fields needed for the Order to be built
     *
     * @param orderItemIndexList null if no index was given by user
     *                           (todo not sure if should change to empty indexlist instead)
     * @param addOrderDescriptor contains all other fields needed for Order to be built and added into model
     */
    public AddCommand(IndexList orderItemIndexList, AddOrderDescriptor addOrderDescriptor) {
        // requireNonNull(orderItemIndexList);

        requireNonNull(addOrderDescriptor);
        this.orderItemIndexList = orderItemIndexList;
        this.addOrderDescriptor = new AddOrderDescriptor(addOrderDescriptor); // defensive copy like in edit command
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        assert this.addOrderDescriptor.getOrderDescriptions().isPresent()
                || this.orderItemIndexList.getIndexList().size() != 0
                : "some error here; neither order description nor order item index was provided";

        if (this.addOrderDescriptor.getOrderDescriptions().isPresent()) {
            addToOrderItems(model);
        }

        if (orderItemIndexList != null) {
            mapIndexToOrderItems(model);
        }

        Order toAdd = addOrderDescriptor.build(); // slightly diff from editOrderDescriptor

        if (model.hasOrder(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_ORDER);
        }

        model.addOrder(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    /**
     * Adds order descriptions in the descriptor to order items model if they aren't already in the model.
     * These order descriptions are inputted by the user and already added into the descriptor before
     * this add command object is created.
     *
     * @param model
     */
    private void addToOrderItems(Model model) {
        this.addOrderDescriptor.getOrderDescriptions().get().stream()
                .map(OrderDescription::getValue) // because a string is needed for creating a new Type for new OrderItem
                .map(o -> new OrderItem(new Type(o))) // map to order item so can check if already in model
                .filter(o -> !model.hasOrderItem(o)) // filters out items that already exist in model
                .forEach(model::addOrderItem);

    }

    /**
     * Adds order items from the order model that correspond to order item indexes given by the user to the descriptor
     *
     * @param model
     * @throws CommandException thrown when invalid indexes are given
     */
    private void mapIndexToOrderItems(Model model) throws CommandException {
        List<OrderItem> lastShownOrderItems = model.getFilteredOrderItemsList();

        List<Index> list = orderItemIndexList.getIndexList(); // gets the inner list to perform operations on each index

        for (Index i : list) {
            if (i.getZeroBased() >= lastShownOrderItems.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_ORDER_ITEM_INDEX);
            }
        }

        if (addOrderDescriptor.getOrderDescriptions().isPresent()) {
            addToOrderItemsModel(model);
        }

        /*
        for each index specified
        get corresponding order item
        create an order description from the order item
         */
        list.forEach(i -> addOrderDescriptor.setOrderDescription(
                new OrderDescription(
                        lastShownOrderItems
                                .get(i.getZeroBased())
                                .getType()
                                .toString()
                )
        ));
    }

    /**
     * If user inputs an order description that isn't in the order items model,
     * add it to the order items model.
     */
    private void addToOrderItemsModel(Model model) {
        assert addOrderDescriptor.getOrderDescriptions().isPresent();

        Set<OrderDescription> orderDescriptionSet = addOrderDescriptor.getOrderDescriptions().get();
        orderDescriptionSet.stream()
                .map(o -> new OrderItem(new Type(o.toString())))
                .filter(o -> !model.hasOrderItem(o))
                .forEach(model::addOrderItem);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        // state check
        AddCommand a = (AddCommand) other;
        return (Objects.equals(orderItemIndexList, a.orderItemIndexList))
                && addOrderDescriptor.equals(a.addOrderDescriptor);
    }


    /**
     * Stores the details to edit the order with. Each non-empty field value will replace the
     * corresponding field value of the order.
     */
    public static class AddOrderDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<OrderDescription> orderDescriptions;
        private Set<Tag> tags;
        private DeliveryDate deliveryDate;
        private Request request;

        public AddOrderDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code orderDescriptions} and {@code tags} is used internally.
         */
        public AddOrderDescriptor(AddCommand.AddOrderDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setOrderDescriptions(toCopy.orderDescriptions);
            setTags(toCopy.tags);
            setDeliveryDate(toCopy.deliveryDate);
            setRequest(toCopy.request);
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

        public void setOrderDescriptions(Set<OrderDescription> orderDescriptions) {
            if (orderDescriptions == null) {
                return;
            }

            if (this.orderDescriptions == null) {
                this.orderDescriptions = new HashSet<>(orderDescriptions);
            } else {
                this.orderDescriptions.addAll(orderDescriptions);
            }
        }

        public void setOrderDescription(OrderDescription orderDescription) {
            if (this.orderDescriptions == null) {
                this.orderDescriptions = new HashSet<>();
            }

            this.orderDescriptions.add(orderDescription);
        }

        public Optional<Set<OrderDescription>> getOrderDescriptions() {
            return (orderDescriptions != null)
                    ? Optional.of(Collections.unmodifiableSet(orderDescriptions))
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

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof AddCommand.AddOrderDescriptor)) {
                return false;
            }

            // state check
            AddCommand.AddOrderDescriptor e = (AddCommand.AddOrderDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getAddress().equals(e.getAddress())
                    && getOrderDescriptions().equals(e.getOrderDescriptions())
                    && getTags().equals(e.getTags())
                    && getDeliveryDate().equals(e.getDeliveryDate());
        }

        /**
         * Builds an order based on the fields added to the descriptor
         * @return
         */
        public Order build() {
            return new Order(this.name, this.phone, this.email, this.address, this.orderDescriptions, this.tags,
                    this.deliveryDate, this.request);
        }
    }
}
