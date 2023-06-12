package com.grandp.data.command.update.request;

import com.grandp.data.command.Command;
import com.grandp.data.command.update.authority.UpdateSimpleAuthorityDescriptionCommand;
import com.grandp.data.command.update.authority.UpdateSimpleAuthorityNameDescription;
import com.grandp.data.entity.authority.SimpleAuthority;
import com.grandp.data.exception.CommandCannotBeExecutedException;

import java.util.ArrayList;
import java.util.List;

public class UpdateSimpleAuthorityRequest implements UpdateRequest {

    private SimpleAuthority simpleAuthority;
    private String newName;
    private String newDescription;

    private List<Command> commands = new ArrayList<>();

    public UpdateSimpleAuthorityRequest(SimpleAuthority simpleAuthority, String newName, String newDescription) {
        if (simpleAuthority == null) {
            throw new IllegalArgumentException("Cannot create UpdateSimpleAuthorityRequest. Given Faculty is null.");
        }

        this.simpleAuthority = simpleAuthority;
        this.newName = newName;
        this.newDescription = newDescription;
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
        if (this.newName != null) {
            commands.add(new UpdateSimpleAuthorityNameDescription(this.simpleAuthority, this.newName));
        }

        if (this.newName != null) {
            commands.add(new UpdateSimpleAuthorityDescriptionCommand(this.simpleAuthority, this.newDescription));
        }
    }
}
