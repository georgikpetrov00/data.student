package com.grandp.data.command.update.faculty;

import com.grandp.data.command.Command;
import com.grandp.data.entity.faculty.Faculty;
import com.grandp.data.entity.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class UpdateFacultyCommandHelper implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateFacultyCommandHelper.class);

    boolean executed;
    UpdateFacultyCommandHelper(Faculty faculty) {
        if (faculty == null) {
            IllegalArgumentException e = new IllegalArgumentException("Cannot create Command " + this.getClass().getName() + "'. " + Faculty.class + " object is null");
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }

}
