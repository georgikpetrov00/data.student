package com.grandp.data.user.student_data;

import com.grandp.data.faculty.Faculty;
import com.grandp.data.faculty.FacultyService;
import com.grandp.data.user.User;
import com.grandp.data.user.UserService;
import com.grandp.data.user.student.Degree;
import com.grandp.data.user.student.Semester;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;

@RestController
@RequestMapping("/student-data")
public class StudentDataController {
    private final StudentDataService studentDataService;
    private final FacultyService facultyService;

    private final UserService userService;

    public StudentDataController(StudentDataService studentDataService, FacultyService facultyService, UserService userService) {
        this.studentDataService = studentDataService;
        this.facultyService = facultyService;
        this.userService = userService;
    }

    @PutMapping(path = "/create")
    public ResponseEntity<?> createStudentData(@RequestParam String faculty, @RequestParam String degree, @RequestParam String semester, @RequestParam String facultyNumber) {
        //TODO validate params for null
        Optional<Faculty> fOpt = facultyService.getFacultyByAbbreviation(faculty);

        if (fOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Faculty with Abbreviation: '" + faculty + "' does not exist");
        }

        Faculty facultyObj = fOpt.get();


        Degree degreeObj = Degree.valueOf(degree);


        Semester semesterObj;
        try {
            semesterObj = Semester.of(semester);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Semester: '" + semester + "' does not exist");
        }


        Optional<User> userOpt = userService.getUserByFacultyNumber(facultyNumber);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with faculty number: '" + facultyNumber + "' does not exist");
        }

        User user = userOpt.get();

        StudentData studentData = new StudentData(facultyObj, degreeObj, semesterObj, new HashSet<>(), facultyNumber);

        user.setUserData(studentData);

        studentDataService.save(studentData);
        userService.save(user);

        return ResponseEntity.ok("FIX THIS MESSAGE");
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentData(@PathVariable Long id) {
        Optional<StudentData> studentData = studentDataService.getStudentDataById(id);
        if (studentData.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentData.get());
    }

    // Други методи за създаване, обновяване, изтриване и др.
}