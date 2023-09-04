package com.grandp.data.security.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
//  public ModelAndView saveChanges(
  public String saveChanges(
    @RequestParam(value = "ibanInput", required = false) String ibanInput,
    @RequestParam(value = "phoneNumberInput", required = false) String newPhoneNumber,
    @RequestParam(value = "emailInput", required = false) String newPersonalEmail,
    @RequestParam(value = "newSecretInput", required = false) String newPassword,
    @RequestParam(value = "confirmNewSecretInput", required = false) String confirmNewPassword,
    Model model,
    RedirectAttributes redirectAttributes,
    Principal principal,
    HttpServletResponse response) throws IOException {

    Authentication authn = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authn.getPrincipal();
    StudentData studentData = studentDataService.getStudentDataByUserID(user.getId());

    List<String> messages = new ArrayList<>();

    if (ibanInput !=null && !ibanInput.isBlank()) {
      if (ibanInput.length() > 16 && ibanInput.length() < 36) {
        studentData.setIban(ibanInput);
        logger.info("Successfully changed IBAN to [" + ibanInput + "].");
      } else {
        String errMsg = "Invalid IBAN: [" + ibanInput + "].";
        messages.add(errMsg);
        logger.error(errMsg);
      }
    }

    if (newPersonalEmail != null && !newPersonalEmail.isBlank() && !user.getEmail().equals(newPersonalEmail)) {
      boolean isValidMail = isValidEmail(newPersonalEmail);

      if (isValidMail) {
        user.setPersonalEmail(newPersonalEmail);
        logger.info("Successfully changed email address to [" + newPersonalEmail + "].");
      } else {
        messages.add("Invalid Email address: [" + newPersonalEmail + "].");
      }
    }

    if (newPhoneNumber != null && !newPhoneNumber.isBlank()) {
      boolean isValidNumber = isValidPhoneNumber(newPhoneNumber);

      if (isValidNumber) {
        user.setPhoneNumber(newPhoneNumber);
        logger.info("Successfully changed Phone number to [" + newPhoneNumber + "].");
      } else {
        messages.add("Invalid Phone Number: [" + newPhoneNumber + "].");
      }
    }

    if (newPassword != null && confirmNewPassword != null && !confirmNewPassword.isBlank()) {
      if (newPassword.equals(confirmNewPassword)) {
        user.setPassword(newPassword);

        logger.info("Successfully changed password.");
      } else {
        String errMsg = "Error during password change - passwords doesn't match.";
        messages.add(errMsg);
        logger.error(errMsg);
      }
    }

    if (messages.isEmpty()) {
      userService.save(user);
      studentDataService.save(studentData);
      messages.add("Changes saved successfully.");
    }

    redirectAttributes.addFlashAttribute("messages", messages);
    return "redirect:/manage_profile";
  }

  private static boolean isValidEmail(String email) {
    String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
    Pattern pattern = Pattern.compile(emailRegex);
    Matcher matcher = pattern.matcher(email);
    boolean result = matcher.matches();

    if (!result) {
      logger.error("Invalid Email address: [" + email + "].");
    }

    return result;
  }

  private static boolean isValidPhoneNumber(String phoneNumber) {
    String phoneRegex = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";
    Pattern pattern = Pattern.compile(phoneRegex);
    Matcher matcher = pattern.matcher(phoneNumber);
    boolean result = matcher.matches();

    if (!result) {
      logger.error("Invalid Phone number: [" + phoneNumber + "].");
    }

    return result;
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

  @GetMapping("/create_user")
  public String createUserPage(Model model, Principal principal) throws UserNotFoundException {
    if (principal == null) {
      //unauthenticated
      return "login";
    }

    String username = principal.getName();
    User loggedInUser = userService.getUserByEmail(username);
    model.addAttribute("loggedInUser", loggedInUser);
    model.addAttribute("activePage", "home");

    return "create_user";
  }

  @GetMapping("/update_user")
  public String updateUserPage(Model model, Principal principal) throws UserNotFoundException {
    if (principal == null) {
      //unauthenticated
      return "login";
    }

    String username = principal.getName();
    User loggedInUser = userService.getUserByEmail(username);
    model.addAttribute("loggedInUser", loggedInUser);
    model.addAttribute("activePage", "home");

    return "update_user";
  }
}
