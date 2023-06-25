package com.grandp.data.command.update.subject;

import com.grandp.data.entity.subject.Subject;
import com.grandp.data.exception.CommandCannotBeExecutedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.undo.CannotUndoException;
import java.time.LocalTime;

public class UpdateSubjectStartTimeCommand extends UpdateSubjectCommandHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSubjectStartTimeCommand.class);
    private final Subject subject;
    private final LocalTime newStartTime;
    private final LocalTime oldStartTime;

    public UpdateSubjectStartTimeCommand(Subject subject, LocalTime newStartTime) {
        super(subject);
        this.subject = subject;
        this.newStartTime = newStartTime;
        this.oldStartTime = subject.getStartTime();
    }

    @Override
    public void execute() throws CommandCannotBeExecutedException {
        subject.setStartTime(newStartTime);
        LOGGER.debug("Changing starting time for Subject @" + subject.hashCode() + ", from '" + oldStartTime + "' to '" + newStartTime + "'.");
        executed = true;
    }

    @Override
    public void revert() throws CannotUndoException {
        if (executed) {
            subject.setStartTime(oldStartTime);
            LOGGER.debug("Reverting start time for Subject @" + subject.hashCode());
        } else {
            LOGGER.debug("Nothing to revert for Subject @" + subject.hashCode());
        }
    }
}