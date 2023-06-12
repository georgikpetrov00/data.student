package com.grandp.data.log_helper;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @UserPrincipalNameAccessor class is used to get the current logged-in user username to be used in tracing
 */
public class UserPrincipalNameAccessor extends ClassicConverter {

    @Value("${server.port}")
    private static String serverPort;

    @Override
    public String convert(final ILoggingEvent e) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principalName;

        if (authentication != null && authentication.isAuthenticated()) {
            principalName = authentication.getName();
        } else if (Thread.currentThread().getName().contains("http") && Thread.currentThread().getName().contains(serverPort)) {
            principalName = "Guest";
        } else {
            principalName = "System";
        }

        return principalName;
    }
}