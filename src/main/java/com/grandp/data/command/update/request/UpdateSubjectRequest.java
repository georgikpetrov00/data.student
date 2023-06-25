package com.grandp.data.command.update.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grandp.data.command.Command;
import com.grandp.data.command.update.subject.*;
import com.grandp.data.entity.curriculum.Curriculum;
import com.grandp.data.entity.enumerated.Semester;
import com.grandp.data.entity.subject.Subject;
import com.grandp.data.entity.subjectname.SubjectName;
import com.grandp.data.exception.CommandCannotBeExecutedException;
import com.grandp.data.exception.UpdateRequestCannotBeExecutedException;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class UpdateSubjectRequest implements UpdateRequest {

    private static final Logger logger = LoggerFactory.getLogger(UpdateSubjectSubjectNameCommand.class);
    private Subject subject;
    private Boolean passed;
    private Integer grade;
    private LocalTime startTime;
    private LocalTime endTime;

    private List<Command> commands = new ArrayList<>();

    public UpdateSubjectRequest(Subject subject, Boolean passed, Integer grade, LocalTime startTime, LocalTime endTime) {
        this.subject = subject;
        this.passed = passed;
        this.grade = grade;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public void execute() throws UpdateRequestCannotBeExecutedException {
        load();

        for (Command c : commands) {
            try {
                c.execute();
                logger.debug("Executed " + c.getClass().getName() + " for Subject " + subject.hashCode());
            } catch (CommandCannotBeExecutedException e) {
                logger.debug("An exception occurred while executing Command " + c.getClass().getName() + " for Subject @" + subject.hashCode() + ". Reverting all changes", e);

                revert();

                logger.debug("All changes reverted.");
                throw new UpdateRequestCannotBeExecutedException(e);
            }
        }
    }

    @Override
    public void revert() {
        for (Command c : commands) {
            c.revert();
            logger.debug("Reverted change " + c.getClass().getName() + " for Subject " + subject.hashCode());
        }
    }

    private void load() {
        if (passed != null) {
            commands.add(new UpdateSubjectPassedCommand(subject, passed));
        }

        if (grade != null) {
            commands.add(new UpdateSubjectGradeCommand(subject, grade));
        }

        if (startTime != null) {
            commands.add(new UpdateSubjectStartTimeCommand(subject, startTime));
        }

        if (endTime != null) {
            commands.add(new UpdateSubjectEndTimeCommand(subject, endTime));
        }
    }
}
