package com.grandp.data.entity.curriculum;

import com.grandp.data.entity.subject.Subject;
import com.grandp.data.entity.subject.SubjectService;
import com.grandp.data.exception.notfound.entity.CurriculumNotFoundException;
import com.grandp.data.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

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

    public Curriculum getCurriculumById(Long id) throws CurriculumNotFoundException {
        return curriculumRepository.findById(id).orElseThrow(() -> new CurriculumNotFoundException("Curriculum with id: '" + id +"' not found."));
    }


//    public Curriculum updateCurriculum(Curriculum curriculum, List<Long> subjectIdsToAdd, List<Long> subjectIdsToRemove) {

    public Curriculum updateCurriculum(Long id, UpdateCurriculumRequest request) throws CurriculumNotFoundException {
        Curriculum curriculum = getCurriculumById(id);

        if (request == null) {
            throw new RuntimeException("Cannot update curriculum with id: '" + id +"', given UpdateCurriculumRequest object is null.");
        }

        List<Long> subjectIdsToAdd = request.getSubjectIdsToAdd();
        List<Long> subjectIdsToRemove = request.getSubjectIdsToRemove();


        Collection<Subject> existingSubjects = curriculum.getSubjects();
        Subject subject;

        for (Long subjectId : subjectIdsToAdd) {
            subject = subjectService.getSubjectById(subjectId);
            subject.setCurriculum(curriculum);
            subject = subjectService.saveSubject(subject);
            existingSubjects.add(subject);
        }

        for (Long subjectId : subjectIdsToRemove) {
            subject = subjectService.getSubjectById(subjectId);
            subject.setCurriculum(null);
            subject = subjectService.saveSubject(subject); //saving the subject with removed curriculum
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

    public Set<Curriculum> getCurriculumsByEmail(String email) {
//        this.curriculumRepository.get

        return null;
    }
}