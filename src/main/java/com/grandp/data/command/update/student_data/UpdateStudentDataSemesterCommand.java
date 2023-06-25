package com.grandp.data.command.update.student_data;

import com.grandp.data.entity.user.User;
import com.grandp.data.entity.enumerated.Semester;
import com.grandp.data.entity.student_data.StudentData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.undo.CannotUndoException;

public class UpdateStudentDataSemesterCommand extends UpdateStudentDataCommandHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateStudentDataSemesterCommand.class);

    private User user;
    private Semester newSemester;
    private Semester oldSemester;

    public UpdateStudentDataSemesterCommand(User user, Semester newSemester) {
        super(user);
        this.user = user;
        this.newSemester = newSemester;
    }

    @Override
    public void execute() {
        StudentData studentData = user.getStudentData();

        if (studentData == null) {
            IllegalStateException e = new IllegalStateException("User with personal ID: '" + user.getPersonalId() + "' does not have Student Data assigned.");
            LOGGER.error(e.getMessage(), e);
            throw e;
        }

        oldSemester = studentData.getSemester();
        studentData.setSemester(newSemester);
        LOGGER.debug("Changed Semester for User with personal ID: '" + user.getPersonalId() + "'.");
        executed = true;
    }

    @Override
    public void revert() throws CannotUndoException {
        if (executed) {
            user.getStudentData().setSemester(oldSemester);
            LOGGER.debug("Reverting Semester for User with personal ID: '" + user.getPersonalId() + "'.");
        } else {
            LOGGER.debug("No Semester to revert for User with personal ID: '" + user.getPersonalId() + "'.");
        }
    }
}
