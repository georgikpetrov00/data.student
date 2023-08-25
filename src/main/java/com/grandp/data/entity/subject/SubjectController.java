package com.grandp.data.entity.subject;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import com.grandp.data.command.update.request.UpdateSubjectRequest;
import com.grandp.data.entity.curriculum.Curriculum;
import com.grandp.data.entity.enumerated.Semester;
import com.grandp.data.entity.subjectname.SubjectName;
import com.grandp.data.entity.subjectname.SubjectNameService;
import com.grandp.data.exception.UpdateRequestCannotBeExecutedException;
import com.grandp.data.exception.notfound.entity.UserNotFoundException;
import com.grandp.data.entity.user.User;
import com.grandp.data.entity.user.UserService;
import com.grandp.data.entity.student_data.StudentData;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/subject")
public class SubjectController {

    public static final Logger log = LoggerFactory.getLogger(SubjectController.class);

    private final SubjectService subjectService;
    private final SubjectNameService subjectNameService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createSubject(@RequestParam String subjectName,
                                           @RequestParam String facultyNumber,
//                                           @RequestParam Boolean passed,
//                                           @RequestParam Integer grade,
                                           @RequestParam String dayOfWeek,
                                           @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
                                           @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime endTime,
                                           @RequestParam String semester,
                                           @RequestParam String hall,
                                           @RequestParam String type) {
        SubjectName subjectNameObj = subjectNameService.getSubjectNameByName(subjectName);

        User user = userService.getUserByFacultyNumber(facultyNumber);

        DayOfWeek dayOfWeekObj;
        try {
            dayOfWeekObj = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        Semester semesterObj = Semester.of(semester);

        Subject subject;
        try {
            subject = new Subject(subjectNameObj, user, dayOfWeekObj, startTime, endTime, semesterObj, hall, type);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        Subject savedSubject = subjectService.saveSubject(subject);

//        StudentData studentData = user.getStudentData();
//        Curriculum curriculum = studentData.getCurricula().stream().filter(curriculum1 -> curriculum1.getSemester().getValue().equalsIgnoreCase(semester)).findFirst().orElse(null);


        return ResponseEntity.ok(savedSubject);

    }

    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDTO> getSubjectById(@PathVariable Long id) {
        Subject subject = subjectService.getSubjectById(id);
        SubjectDTO dto = new SubjectDTO(subject);

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        log.info("Subject with id: '" + id + "' successfully deleted.");
        return ResponseEntity.ok("Subject with id: '" + id + "' successfully deleted.");
    }

    @PostMapping(path = "/update/{facultyNumber}/{subjectName}")
    public ResponseEntity<?> updateSubject(@PathVariable String facultyNumber,
                                           @PathVariable String subjectName,
                                           @RequestParam(required = false) Boolean passed,
                                           @RequestParam(required = false) Integer grade,
                                           @RequestParam(required = false) String dayOfWeek,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime endTime,
                                           @RequestParam(required = false) String semester) throws UserNotFoundException {
        String msg;

        User user = userService.getUserByFacultyNumber(facultyNumber);

        StudentData studentData = user.getStudentData();
        if (studentData == null) {
            msg = "User with faculty number: '" + facultyNumber + "' does not have Student Data.";
            log.error(msg);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }

        if (! studentData.hasSubject(subjectName)) {
            msg = "User with faculty number: '" + facultyNumber + "' does not have Subject: '" + subjectName +"' assigned.";
            log.error(msg);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }

        Subject subject;
        try {
            subject = studentData.getSubject(subjectName);
        } catch (IllegalStateException ex) {
            log.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }

        UpdateSubjectRequest request = new UpdateSubjectRequest(subject, passed, grade, startTime, endTime);

        try {
            request.execute();
        } catch (UpdateRequestCannotBeExecutedException e) {
            log.error("An error occurred while updating Subject @" + subject.hashCode() + ". No changes are applied.");
        }

        subjectService.saveSubject(subject);

        SubjectDTO dto = new SubjectDTO(subject);

        log.info("Successfully updated Subject: " + dto);
        return ResponseEntity.ok("Successfully updated Subject: " + dto);
    }

}