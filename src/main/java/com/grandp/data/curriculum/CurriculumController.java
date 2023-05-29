package com.grandp.data.curriculum;

import com.grandp.data.exception.UserNotFoundException;
import com.grandp.data.user.User;
import com.grandp.data.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/curriculums")
public class CurriculumController {
    @Autowired
    private final CurriculumService curriculumService;
    @Autowired
    private final UserService userService;

    @Autowired
    public CurriculumController(CurriculumService curriculumService, UserService userService) {
        this.curriculumService = curriculumService;
        this.userService = userService;
    }

    @PostMapping(path = "/create/{userFacNum}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCurriculum(@RequestBody Curriculum curriculum, @PathVariable String userFacNum) {
        Optional<User> userOpt = userService.getUserByFacultyNumber(userFacNum);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with faculty number: '" + userFacNum + "' does not exist.");
        }

        curriculum.setUser(userOpt.get());
        Curriculum returnVal = curriculumService.createCurriculum(curriculum);

        return ResponseEntity.ok(returnVal);
    }

    @GetMapping
    public List<Curriculum> getAllCurriculums() {
        return curriculumService.getAllCurriculums();
    }

    @GetMapping("/{id}")
    public Optional<Curriculum> getCurriculumById(@PathVariable Long id) {
        Optional<Curriculum> curriculumOpt = curriculumService.getCurriculumById(id);
        return curriculumOpt;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCurriculum(@PathVariable Long id, @RequestBody UpdateCurriculumRequest request) {
        Optional<Curriculum> curriculumOpt = curriculumService.getCurriculumById(id);

        if (curriculumOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Curriculum with id: '" + id + "' does not exist.");
        }

        List<Long> subjectIdsToAdd = request.getSubjectIdsToAdd();
        List<Long> subjectIdsToRemove = request.getSubjectIdsToRemove();

        Curriculum curriculum = curriculumService.updateCurriculum(curriculumOpt.get(), subjectIdsToAdd, subjectIdsToRemove);

        return ResponseEntity.ok(curriculum);
    }

    @PutMapping("/assign/{curriculumId}/{userId}")
    public ResponseEntity<?> assignStudentToCurriculum(@PathVariable Long curriculumId, @PathVariable Long userId) {
        Optional<Curriculum> curriculumOpt = curriculumService.getCurriculumById(curriculumId);

        if (curriculumOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Curriculum with id: '" + curriculumId + "' does not exist.");
        }

        Optional<User> userOpt = userService.getUserById(userId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id: '" + userId + "' does not exist.");
        }

        Curriculum curriculum = curriculumService.assignStudentToCurriculum(curriculumOpt.get(), userOpt.get());

        return ResponseEntity.ok().body(curriculum);
    }

    @DeleteMapping("/{id}")
    public void deleteCurriculum(@PathVariable Long id) {
        curriculumService.deleteCurriculum(id);
    }
}