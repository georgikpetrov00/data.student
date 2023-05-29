package com.grandp.data.curriculum.subject;

import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Subject saveSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    public Subject getSubjectById(Long id) {
        Subject subject = subjectRepository.getById(id);
        return subject;
    }

//    public Optional<Subject> getSubjectByName(String name) {
//        return subjectRepository.findByName(name);
//    }

    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        boolean isExisting = subjectRepository.existsById(id);

        return isExisting;
    }

//    public void deleteSubject(String name) {
//        subjectRepository.deleteByName(name);
//    }
}