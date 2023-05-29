package com.grandp.data.user.student_data;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentDataService {
    private final StudentDataRepository studentDataRepository;

    public StudentDataService(StudentDataRepository studentDataRepository) {
        this.studentDataRepository = studentDataRepository;
    }

    public StudentData save(StudentData studentData) {
        return studentDataRepository.save(studentData);
    }

    public Optional<StudentData> getStudentDataById(Long id) {
        return studentDataRepository.findById(id);
    }


}