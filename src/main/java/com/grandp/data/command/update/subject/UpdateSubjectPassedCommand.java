package com.grandp.data.command.update.subject;

import com.grandp.data.command.Command;
import com.grandp.data.entity.subject.Subject;
import com.grandp.data.entity.subjectname.SubjectName;
import com.grandp.data.exception.CommandCannotBeExecutedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.undo.CannotUndoException;

public class UpdateSubjectPassedCommand extends UpdateSubjectCommandHelper {

    private static final Logger logger = LoggerFactory.getLogger(UpdateSubjectSubjectNameCommand.class);
    private Subject subject;
    private boolean newPassed;
    private boolean oldPassed;

    public UpdateSubjectPassedCommand(Subject subject, boolean newPassed) {
        super(subject);
        this.subject = subject;
        this.newPassed = newPassed;
    }

    @Override
    public void execute() throws CommandCannotBeExecutedException {
        oldPassed = subject.getPassed();
        subject.setPassed(newPassed);
        logger.debug("Changing passed status for Subject: " + subject.hashCode() + ", from '" + oldPassed + "' to '" + newPassed +'.');
        executed = true;
    }

    @Override
    public void revert() throws CannotUndoException {
        if (executed) {
            subject.setPassed(oldPassed);
            logger.debug("Reverting passed status for Subject: " + subject.hashCode());
        } else {
            logger.debug("No passed status to revert for Subject: " + subject.hashCode());
        }
    }
}
