package com.grandp.data.faculty;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/faculty") 
public class FacultyController {
    private final FacultyService facultyService;

    @Autowired
    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping
    public List<Faculty> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getFacultyById(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Invalid path variable: 'id'.");
        }

        Optional<Faculty> facultyOpt = facultyService.getFacultyById(id);

        if (facultyOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Faculty with id '" + id + "' does not exist.");
        }

        Faculty faculty = facultyOpt.get();
        FacultyDTO facultyDTO = FacultyDTO.of(faculty);

        return ResponseEntity.ok(facultyDTO);
    }

    @GetMapping(path = "/get/{abbreviation}")
    public ResponseEntity<?> getFacultyByAbbreviation(@PathVariable String abbreviation) {
        if (abbreviation == null || abbreviation.equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Invalid path variable: 'abbreviation'.");
        }

        Optional<Faculty> facultyOpt = facultyService.getFacultyByAbbreviation(abbreviation);

        if (facultyOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Faculty with abbreviation '" + abbreviation + "' does not exist.");
        }

        Faculty faculty = facultyOpt.get();
        FacultyDTO facultyDTO = FacultyDTO.of(faculty);

        return ResponseEntity.ok(facultyDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFacultyById(id);
        return ResponseEntity.ok("Faculty with id " + id + " has been deleted successfully.");
    }
}