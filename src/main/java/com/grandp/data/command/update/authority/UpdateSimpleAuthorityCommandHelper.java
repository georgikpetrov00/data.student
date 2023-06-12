package com.grandp.data.command.update.authority;

import com.grandp.data.command.Command;
import com.grandp.data.entity.authority.SimpleAuthority;
import com.grandp.data.entity.faculty.Faculty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class UpdateSimpleAuthorityCommandHelper implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSimpleAuthorityCommandHelper.class);

    boolean executed;

    UpdateSimpleAuthorityCommandHelper(SimpleAuthority simpleAuthority) {
        if (simpleAuthority == null) {
            IllegalArgumentException e = new IllegalArgumentException("Cannot create Command " + this.getClass().getName() + "'. " + Faculty.class + " object is null");
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }
}
