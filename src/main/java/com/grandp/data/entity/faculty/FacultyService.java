package com.grandp.data.entity.faculty;

import java.util.List;

import com.grandp.data.exception.notfound.entity.FacultyNotFoundException;
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

    public Faculty getFacultyById(Long id) throws FacultyNotFoundException {
        return facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException("Faculty with id '" + id + "' not found."));
    }

    public void deleteFacultyById(Long id) {
        if (facultyRepository.existsById(id)) {
            facultyRepository.deleteById(id);
        } else {
            throw new FacultyNotFoundException("Faculty with id '" + id + "' not found.");
        }
    }

    public Faculty getFacultyByAbbreviation(String abbreviation) {
        return facultyRepository.getFacultyByAbbreviation(abbreviation).orElseThrow(() -> new FacultyNotFoundException("Faculty with Abbreviation '" + abbreviation + "' not found."));
    }
}