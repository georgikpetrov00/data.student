package com.grandp.data.faculty;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @GetMapping("/{id}")
    public Faculty getFacultyById(@PathVariable Long id) {
    	System.out.println("GRRRRRT PRRRRRRRT");
        return facultyService.getFacultyById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteFacultyById(@PathVariable Long id) {
        facultyService.deleteFacultyById(id);
    }

    // Other methods...
}