package com.grandp.data.entity.subjectname;

import com.grandp.data.exception.notfound.entity.SubjectNameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectNameService {

    private final SubjectNameRepository subjectNameRepository;

    public SubjectNameService(SubjectNameRepository subjectNameRepository) {
        this.subjectNameRepository = subjectNameRepository;
    }

    public List<SubjectName> getAllSubjectNames() {
        return subjectNameRepository.findAll();
    }

    public SubjectName getSubjectNameById(Long id) throws SubjectNameNotFoundException {
        return subjectNameRepository.findById(id).orElseThrow(() -> new SubjectNameNotFoundException("Subject Name with id: '" + id +"' not found."));
    }

    public SubjectName createSubjectName(String subjectName) {
        SubjectName subjectNameObj = new SubjectName(subjectName);

        return subjectNameRepository.save(subjectNameObj);
    }

    public SubjectName updateSubjectName(String oldSubjectName, String newSubjectName) throws SubjectNameNotFoundException {
        if (subjectNameRepository.existsByName(oldSubjectName)) {
            SubjectName subjectName = subjectNameRepository.getSubjectNameByName(oldSubjectName).get();

            subjectName.setName(newSubjectName);
            return subjectNameRepository.save(subjectName);
        } else {
            throw new SubjectNameNotFoundException("Subject Name '" + oldSubjectName + "' not found.");
        }
    }

    public void deleteSubjectName(Long id) throws SubjectNameNotFoundException {
        if (subjectNameRepository.existsById(id)) {
            subjectNameRepository.deleteById(id);
        } else {
            throw new SubjectNameNotFoundException("Subject Name with id: '" + id + "' not found.");
        }
    }

    public SubjectName getSubjectNameByName(String name) {
        return subjectNameRepository.getSubjectNameByName(name).orElseThrow(() -> new SubjectNameNotFoundException("Subject Name '" + name + "' not found."));
    }
}