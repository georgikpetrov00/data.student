package com.grandp.data.entity.subject;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import com.grandp.data.entity.enumerated.Semester;
import com.grandp.data.entity.curriculum.CurriculumService;
import com.grandp.data.entity.subjectname.SubjectName;
import com.grandp.data.entity.subjectname.SubjectNameService;
import com.grandp.data.exception.notfound.entity.UserNotFoundException;
import com.grandp.data.entity.user.User;
import com.grandp.data.entity.user.UserService;
import com.grandp.data.entity.student_data.StudentData;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    private final SubjectService subjectService;
    private final SubjectNameService subjectNameService;
    private final CurriculumService curriculumService;
    private final UserService userService;
    public SubjectController(SubjectService subjectService, SubjectNameService subjectNameService, CurriculumService curriculumService, UserService userService) {
        this.subjectService = subjectService;
        this.subjectNameService = subjectNameService;
        this.curriculumService = curriculumService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSubject(@RequestParam String subjectName,
                                           @RequestParam String facultyNumber,
                                           @RequestParam Boolean passed,
                                           @RequestParam Integer grade,
                                           @RequestParam String dayOfWeek,
                                           @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
                                           @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime endTime,
                                           @RequestParam String semester) {
        try {
            SubjectName subjectNameObj = subjectNameService.getSubjectNameByName(subjectName);

            User user = userService.getUserByFacultyNumber(facultyNumber);

            DayOfWeek dayOfWeekObj = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
            Semester semesterObj = Semester.of(semester);

            Subject subject = new Subject(subjectNameObj, user, passed, grade, dayOfWeekObj, startTime, endTime, semesterObj);
            Subject savedSubject = subjectService.saveSubject(subject);

            return ResponseEntity.ok(savedSubject);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<SubjectDTO> getSubjectById(@PathVariable Long id) {
        Subject subject = subjectService.getSubjectById(id);
        SubjectDTO dto = new SubjectDTO(subject);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/subject/{id}/passed")
    public ResponseEntity<?> setSubjectPassed(@PathVariable Long id, @RequestParam Boolean passed) {
        if (!subjectService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Subject existingSubject = subjectService.getSubjectById(id);
        existingSubject.setPassed(passed);

        Subject updatedSubject = subjectService.saveSubject(existingSubject);

        return ResponseEntity.ok(updatedSubject);
    }

    @PutMapping("/subject/{id}/grade")
    public ResponseEntity<?> setSubjectGrade(@PathVariable Long id, @RequestParam Integer grade) {
        if (!subjectService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Subject existingSubject = subjectService.getSubjectById(id);
        existingSubject.setGrade(grade);

        Subject updatedSubject = subjectService.saveSubject(existingSubject);

        return ResponseEntity.ok(updatedSubject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
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
        User user = userService.getUserByFacultyNumber(facultyNumber);


        StudentData studentData = user.getUserData();
        if (studentData == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with faculty number: '" + facultyNumber + "' does not have Student Data.");
        }

        if (! studentData.hasSubject(subjectName)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with faculty number: '" + facultyNumber + "' does not have Subject: '" + subjectName +"' assigned.");
        }

        Subject subject = studentData.getSubject(subjectName);

        if (passed != null) {
            subject.setPassed(passed);
        }

        if (grade != null) {
            subject.setGrade(grade);
        }

        if (dayOfWeek != null) {
            DayOfWeek day;

            try {
                day = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
                subject.setDayOfWeek(day);
            } catch (IllegalArgumentException e) {
                //TODO trace that dayOfWeek is not correct
            }
        }

        if (startTime != null) {
            subject.setStartTime(startTime);
        }

        if (endTime != null) {
            subject.setEndTime(endTime);
        }

        if (semester != null) {
            Semester semesterObj = Semester.of(semester);

            if (semesterObj != null) {
                subject.setSemester(semesterObj);
            }
        }

        subjectService.saveSubject(subject);

        return ResponseEntity.ok("Successfully updated Subject: " + subject.toString());
    }

}