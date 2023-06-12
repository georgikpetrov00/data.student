package com.grandp.data.command.update.student_data;

import com.grandp.data.entity.faculty.Faculty;
import com.grandp.data.entity.user.User;
import com.grandp.data.entity.student_data.StudentData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.undo.CannotUndoException;

public class UpdateStudentDataFacultyCommand extends UpdateStudentDataCommandHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateStudentDataFacultyCommand.class);

    private User user;
    private Faculty newFaculty;
    private Faculty oldFaculty;

    public UpdateStudentDataFacultyCommand(User user, Faculty newFaculty) {
        super(user);
        this.user = user;
        this.newFaculty = newFaculty;
    }

    @Override
    public void execute() {
        StudentData studentData = user.getUserData();

        if (studentData == null) {
            IllegalStateException e = new IllegalStateException("User with personal ID: '" + user.getPersonalId() + "' does not have Student Data assigned.");
            LOGGER.error(e.getMessage(), e);
            throw e;
        }

        oldFaculty = studentData.getFaculty();
        studentData.setFaculty(newFaculty);
        LOGGER.debug("Changed Faculty for User with personal ID: '" + user.getPersonalId() + "'.");
        executed = true;
    }

    @Override
    public void revert() throws CannotUndoException {
        if (executed) {
            user.getUserData().setFaculty(oldFaculty);
            LOGGER.debug("Reverting Degree for User with personal ID: '" + user.getPersonalId() + "'.");
        } else {
            LOGGER.debug("No Degree to revert for User with personal ID: '" + user.getPersonalId() + "'.");
        }
    }
}