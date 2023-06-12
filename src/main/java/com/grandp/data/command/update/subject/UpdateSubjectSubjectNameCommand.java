package com.grandp.data.command.update.subject;

import com.grandp.data.command.Command;
import com.grandp.data.entity.subject.Subject;
import com.grandp.data.entity.subjectname.SubjectName;
import com.grandp.data.exception.CommandCannotBeExecutedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.undo.CannotUndoException;

@Deprecated
public class UpdateSubjectSubjectNameCommand extends UpdateSubjectCommandHelper {

    private static final Logger logger = LoggerFactory.getLogger(UpdateSubjectSubjectNameCommand.class);
    private Subject subject;
    private SubjectName newSubjectName;
    private SubjectName oldSubjectName;

    public UpdateSubjectSubjectNameCommand(Subject subject, SubjectName newSubjectName) {
        super(subject);
        this.subject = subject;
        this.newSubjectName = newSubjectName;
        logger.debug("Created " + this.getClass().getName() + " for Subject: " + subject.hashCode());
    }

    @Override
    public void execute() throws CommandCannotBeExecutedException {
        oldSubjectName = subject.getName();
        subject.setName(newSubjectName);
        logger.debug("Changing SubjectName for Subject: " + subject.hashCode() + ", new SubjectName=" + newSubjectName + ", old SubjectName=" + oldSubjectName);
        executed = true;
    }

    @Override
    public void revert() throws CannotUndoException {
        if (executed) {
            subject.setName(oldSubjectName);
            logger.debug("Reverting SubjectName for Subject: " + subject.hashCode());
        } else {
            logger.debug("No SubjectName to revert for Subject: " + subject.hashCode());
        }
    }
}
