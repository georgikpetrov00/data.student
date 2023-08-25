package com.grandp.data.security.controller;

import java.security.Principal;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.grandp.data.entity.curriculum.Curriculum;
import com.grandp.data.entity.curriculum.CurriculumController;
import com.grandp.data.entity.curriculum.CurriculumService;
import com.grandp.data.entity.student_data.StudentData;
import com.grandp.data.entity.student_data.StudentDataService;
import com.grandp.data.entity.subject.Subject;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.grandp.data.entity.authority.SimpleAuthority;
import com.grandp.data.exception.notfound.entity.UserNotFoundException;
import com.grandp.data.entity.user.User;
import com.grandp.data.entity.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@AllArgsConstructor
@Controller
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private static final int[] DAYS = new int[] {
      DayOfWeek.MONDAY.getValue(),
      DayOfWeek.TUESDAY.getValue(),
      DayOfWeek.WEDNESDAY.getValue(),
      DayOfWeek.THURSDAY.getValue(),
      DayOfWeek.FRIDAY.getValue()
    };

    private final UserService userService;
    private final StudentDataService studentDataService;
    private final CurriculumService curriculumService;

    @PostMapping("/login-error")
    public String onFailedLogin(@ModelAttribute("username") String userName,
                                RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("username", userName);
        redirectAttributes.addFlashAttribute("badCredentials", true);
        return "redirect:/login";
    }


//    @PostMapping("/signOut")
//    public ResponseEntity<Void> signOut(@AuthenticationPrincipal UserDto user) {
//        SecurityContextHolder.clearContext();
//        return ResponseEntity.noContent().build();
//    }


    @GetMapping("/login")
    public String showLoginForm(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }

        model.addAttribute("activePage", "profile");
        return "profile";
    }

    @GetMapping("/logouted")
    public String showLogoutPage(Model model) {

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
//            return "login";
//        }

        return "logouted";
    }

//    @PostMapping("/login")
//    public String login(Model model, HttpServletRequest request) {
//
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
//                .collect(Collectors.toList()));
                .toList());
        model.addAttribute("activePage", "profile");


        logger.info(String.format("User '%s' accessed profile page.", username));
        return "profile";
    }

    @GetMapping("/grades")
    public String userGrades(Model model, Principal principal) throws UserNotFoundException {
        if (principal == null) {
            //unauthenticated
            return "grades";
        }

        String username = principal.getName();
        User loggedInUser = userService.getUserByEmail(username);
        model.addAttribute("loggedInUser", loggedInUser);


        StudentData studentData = studentDataService.getStudentDataByUserID(loggedInUser.getId());
        model.addAttribute("studentData", studentData);

        Set<Curriculum> curriculumSet = studentData.getCurricula();
        List<Curriculum> orderedCurricula = curriculumSet.stream()
          .sorted(Comparator.comparingInt(curriculum -> -curriculum.getSemester().getIntValue()))
          .collect(Collectors.toList());
        model.addAttribute("orderedCurricula", orderedCurricula); // ordered set here
        model.addAttribute("activePage", "grades");

        logger.info(String.format("User '%s' accessed grades page.", username));
        return "grades";
    }

    @GetMapping("/program")
    public String userCurriculum(Model model, Principal principal) {
        if (principal == null) {
            //unauthenticated
            return "grades";
        }

        String username = principal.getName();
        User loggedInUser = userService.getUserByEmail(username);
        model.addAttribute("loggedInUser", loggedInUser);

        StudentData studentData = studentDataService.getStudentDataByUserID(loggedInUser.getId());
        model.addAttribute("studentData", studentData);

        Curriculum curriculum = studentData.getCurriculum(studentData.getSemester());
        List<Subject> sortedSubjects = new ArrayList<>(curriculum.getSubjects());
        sortSubjectsByDayAndTime(sortedSubjects);

        model.addAttribute("curriculum", curriculum);
        model.addAttribute("sortedSubjects", sortedSubjects);
        model.addAttribute("curriculumSize", curriculum.getSubjects().size());
        model.addAttribute("days", DAYS);
        model.addAttribute("activePage", "program");

        for (Subject s : curriculum.getSubjects()) {
            System.out.println(s.toString());
        }

        logger.info(String.format("User '%s' accessed program page.", username));
        return "program";
    }

    private void sortSubjectsByDayAndTime(List<Subject> subjects) {
        Collections.sort(subjects, new Comparator<Subject>() {
            @Override
            public int compare(Subject subject1, Subject subject2) {
                int dayComparison = subject1.getDayOfWeek().compareTo(subject2.getDayOfWeek());
                if (dayComparison != 0) {
                    return dayComparison;
                }

                return subject1.getStartTime().compareTo(subject2.getStartTime());
            }
        });
    }
}
