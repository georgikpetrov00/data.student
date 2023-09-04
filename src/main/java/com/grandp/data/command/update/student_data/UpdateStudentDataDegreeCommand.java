package com.grandp.data.command.update.student_data;

import com.grandp.data.entity.user.User;
import com.grandp.data.entity.enumerated.Degree;
import com.grandp.data.entity.student_data.StudentData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.undo.CannotUndoException;

public class UpdateStudentDataDegreeCommand extends UpdateStudentDataCommandHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateStudentDataDegreeCommand.class);
    private User user;
    private StudentData studentData;
    private Degree newDegree;
    private Degree oldDegree;

    public UpdateStudentDataDegreeCommand(User user, StudentData studentData, Degree newDegree) {
        super(user);
        this.user = user;
        this.newDegree = newDegree;
    }

    @Override
    public void execute() throws IllegalStateException {
        if (studentData == null) {
            IllegalStateException e = new IllegalStateException("User with personal ID: '" + user.getPersonalId() + "' does not have Student Data assigned.");
            LOGGER.error(e.getMessage(), e);
            throw e;
        }

        oldDegree = studentData.getDegree();
        studentData.setDegree(newDegree);
        LOGGER.debug("Changed Degree for User with personal ID: '" + user.getPersonalId() + "'.");
        executed = true;
    }

    @Override
    public void revert() throws CannotUndoException {
        if (executed) {
            user.getStudentData().setDegree(oldDegree);
            LOGGER.debug("Reverting Degree for User with personal ID: '" + user.getPersonalId() + "'.");
        } else {
            LOGGER.debug("No Degree to revert for User with personal ID: '" + user.getPersonalId() + "'.");
        }
    }
}
