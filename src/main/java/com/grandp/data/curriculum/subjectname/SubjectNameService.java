package com.grandp.data.curriculum.subjectname;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectNameService {

    private final SubjectNameRepository subjectNameRepository;

    public SubjectNameService(SubjectNameRepository subjectNameRepository) {
        this.subjectNameRepository = subjectNameRepository;
    }

    public List<SubjectName> getAllSubjectNames() {
        return subjectNameRepository.findAll();
    }

    public SubjectName getSubjectNameById(Long id) {
        return subjectNameRepository.findById(id).orElse(null);
    }

    public SubjectName createSubjectName(SubjectName subjectName) {
        return subjectNameRepository.save(subjectName);
    }

    public SubjectName updateSubjectName(Long id, SubjectName subjectName) {
        if (subjectNameRepository.existsById(id)) {
            subjectName.setId(id);
            return subjectNameRepository.save(subjectName);
        } else {
            return null;
        }
    }

    public boolean deleteSubjectName(Long id) {
        if (subjectNameRepository.existsById(id)) {
            subjectNameRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Optional<SubjectName> getSubjectNameByName(String name) {
        return subjectNameRepository.getSubjectNameByName(name);
    }
}