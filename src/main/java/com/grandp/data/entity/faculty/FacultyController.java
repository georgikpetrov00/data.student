package com.grandp.data.entity.faculty;

import java.util.List;

import com.grandp.data.entity.dto.FacultyDTO;
import com.grandp.data.exception.notfound.entity.FacultyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<?> getFacultyById(@PathVariable Long id) throws FacultyNotFoundException {
        Faculty faculty = facultyService.getFacultyById(id);
        FacultyDTO facultyDTO = FacultyDTO.of(faculty);

        return ResponseEntity.ok(facultyDTO);
    }

    @GetMapping(path = "/get/{abbreviation}")
    public ResponseEntity<?> getFacultyByAbbreviation(@PathVariable String abbreviation) {
        Faculty faculty = facultyService.getFacultyByAbbreviation(abbreviation);
        FacultyDTO facultyDTO = FacultyDTO.of(faculty);

        return ResponseEntity.ok(facultyDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFacultyById(id);
        return ResponseEntity.ok("Faculty with id " + id + " has been deleted successfully.");
    }
}