package com.grandp.data.update.command.user;

import com.grandp.data.update.command.Command;
import com.grandp.data.user.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateLastNameCommand implements Command {

    private User user;
    private String newLastName;

    @Override
    public void execute() {
        user.setLastName(newLastName);
    }
}
