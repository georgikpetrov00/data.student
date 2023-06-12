package com.grandp.data.command.update.user;

import com.grandp.data.command.update.student_data.UpdateStudentDataDegreeCommand;
import com.grandp.data.entity.authority.SimpleAuthority;
import com.grandp.data.command.Command;
import com.grandp.data.entity.user.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.undo.CannotUndoException;
import java.util.Set;

public class UpdateUserAuthoritiesCommand extends UpdateUserCommandHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateUserAuthoritiesCommand.class);

    private User user;
    private Set<SimpleAuthority> authoritiesToAdd;
    private Set<SimpleAuthority> authoritiesToRemove;
    private Set<SimpleAuthority> initialAuthorities;

    public UpdateUserAuthoritiesCommand(User user, Set<SimpleAuthority> authoritiesToAdd, Set<SimpleAuthority> authoritiesToRemove) {
        super(user);
        this.user = user;
        this.authoritiesToAdd = authoritiesToAdd;
        this.authoritiesToRemove = authoritiesToRemove;
        this.initialAuthorities = user.getAuthorities();
    }

    @Override
    public void execute() {
        if (authoritiesToRemove != null) {
            for (SimpleAuthority authority : user.getAuthorities()) {
                user.removeAuthority(authority);
            }

            LOGGER.debug("Removed Authorities " + authoritiesToRemove + " from User with Personal ID: '" + user.getPersonalId() + "'.");

        }

        if (authoritiesToAdd != null) {
            for (SimpleAuthority authority : user.getAuthorities()) {
                user.addAuthority(authority);

                LOGGER.debug("Added Authorities " + authoritiesToRemove + " to User with Personal ID: '" + user.getPersonalId() + "'.");
            }
        }

        executed = true;
    }

    @Override
    public void revert() throws CannotUndoException {
        if (executed) {
            user.setAuthorities(initialAuthorities);
            LOGGER.debug("Reverted Authorities  to User with Personal ID: '" + user.getPersonalId() + "'.");
        } else {
            LOGGER.debug(" No Revert of Authorities required.");
        }
    }
}