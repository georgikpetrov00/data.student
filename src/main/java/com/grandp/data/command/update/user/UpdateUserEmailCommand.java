package com.grandp.data.command.update.user;

import com.grandp.data.command.Command;
import com.grandp.data.command.update.student_data.UpdateStudentDataDegreeCommand;
import com.grandp.data.entity.user.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.undo.CannotUndoException;

public class UpdateUserEmailCommand extends UpdateUserCommandHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateUserEmailCommand.class);

    private User user;
    private String newEmail;
    private String oldEmail;

    public UpdateUserEmailCommand(User user, String newEmail) {
        super(user);
        this.user = user;
        this.newEmail = newEmail;
    }

    @Override
    public void execute() {
        oldEmail = user.getEmail();
        user.setEmail(newEmail);
        executed = true;
        LOGGER.debug("Changed Email for User with Personal ID: '" + user.getPersonalId() + "' from '" + oldEmail + "' to '" + newEmail + "'.");
    }

    @Override
    public void revert() throws CannotUndoException {
        if (executed) {
            user.setEmail(oldEmail);
            LOGGER.debug("Reverted Email for User with Personal ID: '" + user.getPersonalId() + "' back to '" + oldEmail + "'.");
        } else {
            LOGGER.debug("No revert of Email required.");
        }
    }
}
