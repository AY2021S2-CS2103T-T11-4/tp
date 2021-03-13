package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.core.index.IndexList;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    public static final String MESSAGE_DELETE_PEOPLE_SUCCESS = "Deleted People: %1$s";

    private final IndexList targetIndexList;

    public DeleteCommand(IndexList targetIndexList) {
        this.targetIndexList = targetIndexList;
    }

    public static String getResultString(List<Person> peopleToDelete) {
        if(peopleToDelete.size() == 1) {
            return String.format(MESSAGE_DELETE_PERSON_SUCCESS, peopleToDelete.get(0));
        }
        String convertedToString = "";
        for (Person person: peopleToDelete) {
            convertedToString = convertedToString + String.format("\n%1$s", person);
        }
        return String.format(MESSAGE_DELETE_PEOPLE_SUCCESS, convertedToString);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        List<Person> peopleToDelete = new ArrayList<>();
        for (Index targetIndex:this.targetIndexList.getIndexList()) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
            peopleToDelete.add(personToDelete);
            model.deletePerson(personToDelete);
        }

        return new CommandResult(getResultString(peopleToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && targetIndexList.equals(((DeleteCommand) other).targetIndexList)); // state check
    }
}
