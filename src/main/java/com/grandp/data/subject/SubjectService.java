package com.grandp.data.subject;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public Subject saveSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    public Optional<Subject> getSubjectById(Long id) {
        return subjectRepository.findById(id);
    }

    public Optional<Subject> getSubjectByName(String name) {
        return subjectRepository.findByName(name);
    }

    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }
}