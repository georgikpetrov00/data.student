package com.grandp.data.command.update.student_data;

import com.grandp.data.entity.user.User;
import com.grandp.data.entity.student_data.StudentData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.undo.CannotUndoException;

public class UpdateStudentDataFacultyNumberCommand extends UpdateStudentDataCommandHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateStudentDataDegreeCommand.class);

    private User user;
    private String newFacultyNumber;
    private String oldFacultyNumber;

    public UpdateStudentDataFacultyNumberCommand(User user, String newFacultyNumber) {
        super(user);
        this.user = user;
        this.newFacultyNumber = newFacultyNumber;
    }

    @Override
    public void execute() {
        StudentData studentData = user.getStudentData();

        if (studentData == null) {
            IllegalStateException e = new IllegalStateException("User with personal ID: '" + user.getPersonalId() + "' does not have Student Data assigned.");
            LOGGER.error(e.getMessage(), e);
            throw e;
        }

        oldFacultyNumber = studentData.getFacultyNumber();
        studentData.setFacultyNumber(newFacultyNumber);
        LOGGER.debug("Changed Faculty Number for User with personal ID: '" + user.getPersonalId() + "'.");
        executed = true;
    }

    @Override
    public void revert() throws CannotUndoException {
        if (executed) {
            user.getStudentData().setFacultyNumber(oldFacultyNumber);
            LOGGER.debug("Reverting Faculty Number for User with personal ID: '" + user.getPersonalId() + "'.");
        } else {
            LOGGER.debug("No Faculty Number to revert for User with personal ID: '" + user.getPersonalId() + "'.");
        }
    }
}