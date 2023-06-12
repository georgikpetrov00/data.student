package com.grandp.data.command.update.faculty;

import com.grandp.data.entity.faculty.Faculty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.undo.CannotUndoException;

public class UpdateFacultyNameCommand extends UpdateFacultyCommandHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateFacultyNameCommand.class);

    private Faculty faculty;
    private String newName;
    private String oldName;

    public UpdateFacultyNameCommand(Faculty faculty, String newName) {
        super(faculty);
        this.faculty = faculty;
        this.newName = newName;
    }

    @Override
    public void execute() {
        oldName = faculty.getName();
        faculty.setName(newName);
        LOGGER.debug("Changing Name for Faculty: " + faculty.getName() + " [Faculty@" + faculty.hashCode() + "], from '" + oldName + "' to '" + newName + "'.");
        executed = true;
    }

    @Override
    public void revert() throws CannotUndoException {
        if (executed) {
            this.faculty.setName(oldName);
            LOGGER.debug("Reverting Name for Faculty: " + faculty.hashCode());
        } else {
            LOGGER.debug("Nothing to revert for Faculty: " + faculty.hashCode());
        }
    }
}
