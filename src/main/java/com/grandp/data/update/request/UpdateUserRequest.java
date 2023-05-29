package com.grandp.data.update.request;

import com.grandp.data.authorities.SimpleAuthority;
import com.grandp.data.update.command.Command;
import com.grandp.data.update.command.user.*;
import com.grandp.data.user.User;
import lombok.AllArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class UpdateUserRequest implements UpdateRequest {

    private User user;
    private String newFirstName;
    private String newLastName;
    private String newEmail;
    private String newPersonalId;
    private Set<SimpleAuthority> authoritiesToAdd;
    private Set<SimpleAuthority> authoritiesToRemove;

    private List<Command> commands = new LinkedList<>();

    public UpdateUserRequest(User user,
                             String newFirstName,
                             String newLastName,
                             String newPersonalId,
                             String newEmail,
                             Set<SimpleAuthority> authoritiesToAdd,
                             Set<SimpleAuthority> authoritiesToRemove) {
        this.user = user;
        this.newFirstName = newFirstName;
        this.newLastName = newLastName;
        this.newEmail = newEmail;
        this.newPersonalId = newPersonalId;
        this.authoritiesToAdd = authoritiesToAdd;
        this.authoritiesToRemove = authoritiesToRemove;
    }

    @Override
    public void execute() {
        load();

        for (Command c : commands) {
            c.execute();
        }
    }

    private void load() {
        if (newFirstName != null) {
            commands.add(new UpdateFirstNameCommand(user, newFirstName));
        }

        if (newLastName != null) {
            commands.add(new UpdateLastNameCommand(user, newLastName));
        }

        if (newEmail != null) {
            commands.add(new UpdateEmailCommand(user, newEmail));
        }

        if (newPersonalId != null) {
            commands.add(new UpdatePersonalIdCommand(user, newPersonalId));
        }

        if (authoritiesToAdd != null && authoritiesToRemove != null) {
            commands.add(new UpdateAuthoritiesCommand(user, authoritiesToAdd, authoritiesToRemove));
        }
    }
}
