package com.grandp.data.security.controller;

import java.security.Principal;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.grandp.data.entity.authority.SimpleAuthority;
import com.grandp.data.exception.notfound.entity.UserNotFoundException;
import com.grandp.data.entity.user.User;
import com.grandp.data.entity.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final UserService userService;

//    @PostMapping("/login-error")
//    public String onFailedLogin(@ModelAttribute("username") String userName,
//                                RedirectAttributes redirectAttributes) {
//        redirectAttributes.addFlashAttribute("username", userName);
//        redirectAttributes.addFlashAttribute("badCredentials", true);
//        return "redirect:/login";
//    }


//    @PostMapping("/signOut")
//    public ResponseEntity<Void> signOut(@AuthenticationPrincipal UserDto user) {
//        SecurityContextHolder.clearContext();
//        return ResponseEntity.noContent().build();
//    }


    //fixme remove from here
    @GetMapping("/profile")
    public String userProfile(Model model, Principal principal) throws UserNotFoundException {
        if (principal == null) {
            //unauthenticated
            return "login";
        }

        String username = principal.getName();
        User loggedInUser = userService.getUserByEmail(username);
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("roles", loggedInUser.getAuthorities().stream()
                .map(SimpleAuthority::getAuthority)
                .collect(Collectors.toList()));

        logger.info("User '" + username + "' accessed profile page.");
        return "profile";
    }
}
