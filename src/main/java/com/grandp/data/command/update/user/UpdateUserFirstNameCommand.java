package com.grandp.data.command.update.user;

import com.grandp.data.entity.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.undo.CannotUndoException;

public class UpdateUserFirstNameCommand extends UpdateUserCommandHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateUserFirstNameCommand.class);

    private User user;
    private String newFirstName;
    private String oldFirstName;

    public UpdateUserFirstNameCommand(User user, String newFirstName) {
        super(user);
        this.user = user;
        this.newFirstName = newFirstName;
    }

    @Override
    public void execute() {
        oldFirstName = user.getFirstName();
        user.setFirstName(newFirstName);
        executed = true;
        LOGGER.info("Changed First Name for User with Personal ID: '" + user.getPersonalId() + "' from '" + oldFirstName + "' to '" + newFirstName + "'.");
    }

    @Override
    public void revert() throws CannotUndoException {
        if (executed) {
            user.setFirstName(oldFirstName);
            LOGGER.info("Reverted First Name for User with Personal ID: '" + user.getPersonalId() + "' back to '" + oldFirstName + "'.");
        } else {
            LOGGER.info("No revert of First Name required.");
        }
    }
}