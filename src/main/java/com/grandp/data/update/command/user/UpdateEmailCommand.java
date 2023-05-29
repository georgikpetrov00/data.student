package com.grandp.data.update.command.user;

import com.grandp.data.update.command.Command;
import com.grandp.data.user.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateEmailCommand implements Command {

    private User user;
    private String newEmail;

    @Override
    public void execute() {
        user.setEmail(newEmail);
    }
}
