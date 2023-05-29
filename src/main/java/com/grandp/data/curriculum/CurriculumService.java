package com.grandp.data.curriculum;

import com.grandp.data.curriculum.subject.Subject;
import com.grandp.data.curriculum.subject.SubjectService;
import com.grandp.data.curriculum.subjectname.SubjectName;
import com.grandp.data.curriculum.subjectname.SubjectNameService;
import com.grandp.data.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CurriculumService {
    private final CurriculumRepository curriculumRepository;
    private final SubjectService subjectService;

    @Autowired
    public CurriculumService(CurriculumRepository curriculumRepository, SubjectService subjectService) {
        this.curriculumRepository = curriculumRepository;
        this.subjectService = subjectService;
    }

    public Curriculum createCurriculum(Curriculum curriculum) {
        return curriculumRepository.save(curriculum);
    }

    public List<Curriculum> getAllCurriculums() {
        return curriculumRepository.findAll();
    }

    public Optional<Curriculum> getCurriculumById(Long id) {
        return curriculumRepository.findById(id);
    }


    public Curriculum updateCurriculum(Curriculum curriculum, List<Long> subjectIdsToAdd, List<Long> subjectIdsToRemove) {
        Collection<Subject> existingSubjects = curriculum.getSubjects();

        for (Long subjectId : subjectIdsToAdd) {
            Subject subject = subjectService.getSubjectById(subjectId);
            subject.setCurriculum(curriculum);
            subject = subjectService.saveSubject(subject);
            existingSubjects.add(subject);
        }

        for (Long subjectId : subjectIdsToRemove) {
            Subject subject = subjectService.getSubjectById(subjectId);
            subject.setCurriculum(null);
            subject = subjectService.saveSubject(subject);
            existingSubjects.remove(subject);
        }

        return curriculumRepository.save(curriculum);
    }

    public Curriculum assignStudentToCurriculum(Curriculum curriculum, User user) {
        curriculum.setUser(user);
        return curriculumRepository.save(curriculum);
    }

    public void deleteCurriculum(Long id) {
        curriculumRepository.deleteById(id);
    }
}