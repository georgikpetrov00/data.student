package com.grandp.data.command.update.authority;

import com.grandp.data.entity.authority.SimpleAuthority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.undo.CannotUndoException;

public class UpdateSimpleAuthorityNameDescription extends UpdateSimpleAuthorityCommandHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSimpleAuthorityNameDescription.class);

    private SimpleAuthority simpleAuthority;
    private String newName;
    private String oldName;

    public UpdateSimpleAuthorityNameDescription(SimpleAuthority simpleAuthority, String newName) {
        super(simpleAuthority);
        this.simpleAuthority = simpleAuthority;
        this.newName = newName;
    }

    @Override
    public void execute() {
        oldName = simpleAuthority.getName();
        simpleAuthority.setName(newName);
        LOGGER.debug("Changing Name for SimpleAuthority: " + simpleAuthority.getName() + ", from '" + oldName + "' to '" + newName + "'.");
        executed = true;
    }

    @Override
    public void revert() throws CannotUndoException {
        if (executed) {
            this.simpleAuthority.setName(oldName);
            LOGGER.debug("Reverting Name for SimpleAuthority: " + simpleAuthority.getName());
        } else {
            LOGGER.debug("Nothing to revert for SimpleAuthority: " + simpleAuthority.getName());
        }
    }
}
