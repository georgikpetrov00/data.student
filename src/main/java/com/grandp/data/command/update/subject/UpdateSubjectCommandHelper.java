package com.grandp.data.command.update.subject;

import com.grandp.data.command.Command;
import com.grandp.data.command.update.student_data.UpdateStudentDataCommandHelper;
import com.grandp.data.entity.subject.Subject;
import com.grandp.data.entity.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class UpdateSubjectCommandHelper implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateStudentDataCommandHelper.class);

    protected boolean executed;

    UpdateSubjectCommandHelper(Subject subject) {
        if (subject == null) {
            IllegalArgumentException e = new IllegalArgumentException("Cannot create Command " + this.getClass().getName() + "'. " + Subject.class + " object is null");
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }
}
