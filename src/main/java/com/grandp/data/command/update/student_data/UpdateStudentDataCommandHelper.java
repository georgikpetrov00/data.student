package com.grandp.data.command.update.student_data;

import com.grandp.data.command.Command;
import com.grandp.data.entity.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class UpdateStudentDataCommandHelper implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateStudentDataCommandHelper.class);

    protected boolean executed;

    UpdateStudentDataCommandHelper(User user) {
        if (user == null) {
            IllegalArgumentException e = new IllegalArgumentException("Cannot create Command " + this.getClass().getName() + "'. " + User.class + " object is null");
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }
}
