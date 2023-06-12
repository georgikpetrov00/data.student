package com.grandp.data.command.update.faculty;

import com.grandp.data.entity.faculty.Faculty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.undo.CannotUndoException;

public class UpdateFacultyAbbreviationCommand extends UpdateFacultyCommandHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateFacultyAbbreviationCommand.class);

    private Faculty faculty;
    private String newAbbreviation;
    private String oldAbbreviation;

    public UpdateFacultyAbbreviationCommand(Faculty faculty, String newAbbreviation) {
        super(faculty);
        this.faculty = faculty;
        this.newAbbreviation = newAbbreviation;
    }

    @Override
    public void execute() {
        oldAbbreviation = faculty.getAbbreviation();
        faculty.setName(newAbbreviation);
        LOGGER.debug("Changing Abbreviation for Faculty: " + faculty.getName() + " [Faculty@" + faculty.hashCode() + "], from '" + oldAbbreviation + "' to '" + newAbbreviation + "'.");
        executed = true;
    }

    @Override
    public void revert() throws CannotUndoException {
        if (executed) {
            this.faculty.setAbbreviation(oldAbbreviation);
            LOGGER.debug("Reverting Abbreviation for Faculty: " + faculty.hashCode());
        } else {
            LOGGER.debug("Nothing to revert for Faculty: " + faculty.hashCode());
        }
    }
}
