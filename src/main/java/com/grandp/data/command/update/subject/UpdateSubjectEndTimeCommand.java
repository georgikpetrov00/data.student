package com.grandp.data.command.update.subject;

import com.grandp.data.entity.subject.Subject;
import com.grandp.data.exception.CommandCannotBeExecutedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.undo.CannotUndoException;
import java.time.LocalTime;

public class UpdateSubjectEndTimeCommand extends UpdateSubjectCommandHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSubjectEndTimeCommand.class);
    private final Subject subject;
    private final LocalTime newEndTime;
    private final LocalTime oldEndTime;

    public UpdateSubjectEndTimeCommand(Subject subject, LocalTime newEndTime) {
        super(subject);
        this.subject = subject;
        this.newEndTime = newEndTime;
        this.oldEndTime = subject.getEndTime();
    }

    @Override
    public void execute() throws CommandCannotBeExecutedException {
        subject.setEndTime(newEndTime);
        LOGGER.debug("Changing starting time for Subject @" + subject.hashCode() + ", from '" + oldEndTime + "' to '" + newEndTime + "'.");
        executed = true;
    }

    @Override
    public void revert() throws CannotUndoException {
        if (executed) {
            subject.setEndTime(oldEndTime);
            LOGGER.debug("Reverting end time for Subject @" + subject.hashCode());
        } else {
            LOGGER.debug("Nothing to revert for Subject @" + subject.hashCode());
        }
    }
}