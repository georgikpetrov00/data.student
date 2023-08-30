package com.grandp.data.security.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.grandp.data.entity.authority.SimpleAuthority;
import com.grandp.data.entity.curriculum.Curriculum;
import com.grandp.data.entity.curriculum.CurriculumService;
import com.grandp.data.entity.student_data.StudentData;
import com.grandp.data.entity.student_data.StudentDataService;
import com.grandp.data.entity.subject.Subject;
import com.grandp.data.entity.user.User;
import com.grandp.data.entity.user.UserService;
import com.grandp.data.exception.notfound.entity.UserNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Controller
public class GUIController {

  private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

  private static final String[] DAYS = new String[] {
    DayOfWeek.MONDAY.toString(),
    DayOfWeek.TUESDAY.toString(),
    DayOfWeek.WEDNESDAY.toString(),
    DayOfWeek.THURSDAY.toString(),
    DayOfWeek.FRIDAY.toString()
  };

  private final UserService userService;
  private final StudentDataService studentDataService;
  private final CurriculumService curriculumService;

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
    StudentData studentData = studentDataService.getStudentDataByUserID(loggedInUser.getId());
    model.addAttribute("studentData", studentData);
    model.addAttribute("activePage", "profile");


    logger.info(String.format("User '%s' accessed Profile page.", username));
    return "profile";
  }

  @GetMapping("/grades")
  public String userGrades(Model model, Principal principal) throws UserNotFoundException {
    if (principal == null) {
      //unauthenticated
      return "login";
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

    logger.info(String.format("User '%s' accessed Grades page.", username));
    return "grades";
  }

  @GetMapping("/program")
  public String userCurriculum(Model model, Principal principal) {
    if (principal == null) {
      //unauthenticated
      return "login";
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

    logger.info(String.format("User '%s' accessed Program page.", username));
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

  @GetMapping("/manage_profile")
  public String userManageProfile(Model model, Principal principal) {
    if (principal == null) {
      //unauthenticated
      return "login";
    }

    String username = principal.getName();
    User loggedInUser = userService.getUserByEmail(username);
    model.addAttribute("loggedInUser", loggedInUser);

    StudentData studentData = studentDataService.getStudentDataByUserID(loggedInUser.getId());
    model.addAttribute("studentData", studentData);
    model.addAttribute("activePage", "manage_profile");



    logger.info(String.format("User '%s' accessed Manage Profile page.", username));
    return "manage_profile";
  }

  @PostMapping("manage_profile/save_changes")
  public String saveChanges(
    @RequestParam(value = "ibanInput", required = false) String ibanInput,
    @RequestParam(value = "newSecretInput", required = false) String newPassword,
    @RequestParam(value = "confirmNewSecretInput", required = false) String confirmNewPassword,
    Model model,
    Principal principal,
    HttpServletResponse response) throws IOException {

    Authentication authn = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authn.getPrincipal();
    StudentData studentData = studentDataService.getStudentDataByUserID(user.getId());

    if (ibanInput !=null && !ibanInput.equalsIgnoreCase(studentData.getIban())) {
      studentData.setIban(ibanInput);
    }

    if (newPassword != null && confirmNewPassword != null && !newPassword.isBlank()) {
      if (newPassword.equals(confirmNewPassword)) {
        user.setPassword(newPassword);
      }
    }

    userService.save(user);
    studentDataService.save(studentData);

    response.sendRedirect("/manage_profile");
    return "manage_profile";
  }

  @GetMapping("/administrate")
  public String adminPage(Model model, Principal principal) throws UserNotFoundException {
    if (principal == null) {
      //unauthenticated
      return "login";
    }

    String username = principal.getName();
    User loggedInUser = userService.getUserByEmail(username);
    model.addAttribute("loggedInUser", loggedInUser);
    model.addAttribute("activePage", "home");

    logger.info(String.format("User '%s' accessed Profile page.", username));
    return "administrate";
  }
}
