package com.grandp.data.command.update.authority;

import com.grandp.data.entity.authority.SimpleAuthority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.undo.CannotUndoException;

public class UpdateSimpleAuthorityDescriptionCommand extends UpdateSimpleAuthorityCommandHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSimpleAuthorityDescriptionCommand.class);

    private SimpleAuthority simpleAuthority;
    private String newDescription;
    private String oldDescription;

    public UpdateSimpleAuthorityDescriptionCommand(SimpleAuthority simpleAuthority, String newDescription) {
        super(simpleAuthority);
        this.simpleAuthority = simpleAuthority;
        this.newDescription = newDescription;
    }

    @Override
    public void execute() {
        oldDescription = simpleAuthority.getDescription();
        simpleAuthority.setDescription(newDescription);
        LOGGER.debug("Changing Description for SimpleAuthority: " + simpleAuthority.getName() + ", from '" + oldDescription + "' to '" + newDescription + "'.");
        executed = true;
    }

    @Override
    public void revert() throws CannotUndoException {
        if (executed) {
            this.simpleAuthority.setDescription(oldDescription);
            LOGGER.debug("Reverting Description for SimpleAuthority: " + simpleAuthority.getName());
        } else {
            LOGGER.debug("Nothing to revert for SimpleAuthority: " + simpleAuthority.getName());
        }
    }
}
