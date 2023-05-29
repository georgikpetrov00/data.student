package com.grandp.data.faculty;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Optional<Faculty> getFacultyById(Long id) {
        return facultyRepository.findById(id);
    }

    public void deleteFacultyById(Long id) {
        facultyRepository.deleteById(id);
    }

    public Optional<Faculty> getFacultyByAbbreviation(String abbreviation) {
        Optional<Faculty> facultyOpt = facultyRepository.getFacultyByAbbreviation(abbreviation);

        return facultyOpt;
    }

    // Other methods...
}