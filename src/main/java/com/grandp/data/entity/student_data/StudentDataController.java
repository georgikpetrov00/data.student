package com.grandp.data.entity.student_data;

import com.grandp.data.entity.enumerated.Degree;
import com.grandp.data.entity.enumerated.Semester;
import com.grandp.data.entity.user.User;
import com.grandp.data.entity.user.UserService;
import com.grandp.data.exception.notfound.entity.UserNotFoundException;
import com.grandp.data.entity.faculty.Faculty;
import com.grandp.data.entity.faculty.FacultyService;
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
    public ResponseEntity<?> createStudentData(@RequestParam String faculty,
      @RequestParam String degree,
      @RequestParam String facultyNumber,
      @RequestParam String potok,
      @RequestParam String groupName) throws UserNotFoundException {
        Faculty facultyObj = facultyService.getFacultyByAbbreviation(faculty);

        Degree degreeObj = Degree.valueOf(degree);

        User user = userService.getUserByFacultyNumber(facultyNumber);

        StudentData studentData = new StudentData(user, facultyObj, degreeObj, facultyNumber, potok, groupName);

        user.setStudentData(studentData);

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

    @GetMapping("/get-by-student-id/{id}")
    public ResponseEntity<?> getStudentDataByUserId(@PathVariable Long id) {
        StudentData studentData = studentDataService.getStudentDataByUserID(id);
        if (studentData == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(studentData);
    }

}