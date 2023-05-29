package com.grandp.data.update.command.user;

import com.grandp.data.update.command.Command;
import com.grandp.data.user.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdatePersonalIdCommand implements Command {

    private User user;
    private String newPersonalId;

    @Override
    public void execute() {
        user.setPersonalId(newPersonalId);
    }
}
