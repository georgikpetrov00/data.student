package com.grandp.data.entity.subject;

import java.util.List;

import com.grandp.data.exception.notfound.entity.SubjectNotFoundException;
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
        return subjectRepository.findById(id).orElseThrow(() -> new SubjectNotFoundException("Subject with id: '" + id + "' not found."));
    }

    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        boolean isExisting = subjectRepository.existsById(id);

        return isExisting;
    }


}