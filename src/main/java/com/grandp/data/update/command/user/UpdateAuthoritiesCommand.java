package com.grandp.data.update.command.user;

import com.grandp.data.authorities.SimpleAuthority;
import com.grandp.data.update.command.Command;
import com.grandp.data.user.User;
import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public class UpdateAuthoritiesCommand  implements Command {

    private User user;
    private Set<SimpleAuthority> authoritiesToAdd;
    private Set<SimpleAuthority> authoritiesToRemove;

    @Override
    public void execute() {
        if (authoritiesToAdd != null) {
            for (SimpleAuthority authority : user.getAuthorities()) {
                user.addAuthority(authority);
            }
        }

        if (authoritiesToRemove != null) {
            for (SimpleAuthority authority : user.getAuthorities()) {
                user.removeAuthority(authority);
            }
        }
    }
}
