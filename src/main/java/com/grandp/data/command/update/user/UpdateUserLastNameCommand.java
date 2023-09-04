package com.grandp.data.command.update.user;

import com.grandp.data.command.Command;
import com.grandp.data.entity.user.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.undo.CannotUndoException;

public class UpdateUserLastNameCommand extends UpdateUserCommandHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateUserLastNameCommand.class);

    private User user;
    private String newLastName;
    private String oldLastName;

    public UpdateUserLastNameCommand(User user, String newLastName) {
        super(user);
        this.user = user;
        this.newLastName = newLastName;
    }

    @Override
    public void execute() {
        oldLastName = user.getLastName();
        user.setLastName(newLastName);
        executed = true;
        LOGGER.info("Changed Last Name for User with Personal ID: '" + user.getPersonalId() + "' from '" + oldLastName + "' to '" + newLastName + "'.");
    }

    @Override
    public void revert() throws CannotUndoException {
        if (executed) {
            user.setLastName(oldLastName);
            LOGGER.info("Reverted Last Name for User with Personal ID: '" + user.getPersonalId() + "' back to '" + oldLastName + "'.");
        } else {
            LOGGER.info("No revert of Last Name required.");
        }
    }
}