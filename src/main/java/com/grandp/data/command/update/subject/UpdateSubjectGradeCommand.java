package com.grandp.data.command.update.subject;

import com.grandp.data.entity.subject.Subject;
import com.grandp.data.exception.CommandCannotBeExecutedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.undo.CannotUndoException;

public class UpdateSubjectGradeCommand extends UpdateSubjectCommandHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSubjectGradeCommand.class);
    private Subject subject;
    private int newGrade;
    private int oldGrade;

    public UpdateSubjectGradeCommand(Subject subject, int newGrade) {
        super(subject);
        this.subject = subject;
        this.newGrade = newGrade;
    }

    @Override
    public void execute() throws CommandCannotBeExecutedException {
        oldGrade = subject.getGrade();
        subject.setGrade(newGrade);
        LOGGER.debug("Changing grade for Subject: " + subject.hashCode() + ", from '" + oldGrade + "' to '" + newGrade + "'.");
        executed = true;
    }

    @Override
    public void revert() throws CannotUndoException {
        if (executed) {
            subject.setGrade(oldGrade);
            LOGGER.debug("Reverting grade for Subject: " + subject.hashCode());
        } else {
            LOGGER.debug("No grade to revert for Subject: " + subject.hashCode());
        }
    }
}
