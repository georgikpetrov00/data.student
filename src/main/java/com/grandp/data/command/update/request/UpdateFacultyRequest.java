package com.grandp.data.command.update.request;

import com.grandp.data.command.Command;
import com.grandp.data.command.update.faculty.UpdateFacultyAbbreviationCommand;
import com.grandp.data.command.update.faculty.UpdateFacultyNameCommand;
import com.grandp.data.entity.faculty.Faculty;
import com.grandp.data.exception.CommandCannotBeExecutedException;

import java.util.ArrayList;
import java.util.List;

public class UpdateFacultyRequest implements UpdateRequest {

    private Faculty faculty;
    private String facultyAbbreviation;
    private String facultyName;

    private List<Command> commands = new ArrayList<>();

    public UpdateFacultyRequest(Faculty faculty, String facultyAbbreviation, String facultyName) {
        if (faculty == null) {
            throw new IllegalArgumentException("Cannot create UpdateFacultyRequest. Given Faculty is null.");
        }

        this.faculty = faculty;
        this.facultyAbbreviation = facultyAbbreviation;
        this.facultyName = facultyName;
    }

    @Override
    public void execute() {
        load();

        for (Command c : commands) {
            try {
                c.execute();
            } catch (CommandCannotBeExecutedException e) {
                System.out.println("An exception occurred while executing " + c.getClass() + " cannot be executed. " + e.getMessage() + ". Reverting all changes.");
                revert();
            }
        }
    }

    @Override
    public void revert() {
        for (Command c : commands) {
            c.revert();
        }
    }

    private void load() {
        if (facultyAbbreviation != null) {
            commands.add(new UpdateFacultyAbbreviationCommand(faculty, facultyAbbreviation));
        }

        if (facultyName != null) {
            commands.add(new UpdateFacultyNameCommand(faculty, facultyName));
        }
    }
}
