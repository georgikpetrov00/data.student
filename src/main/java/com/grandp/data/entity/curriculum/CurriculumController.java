package com.grandp.data.entity.curriculum;

import com.grandp.data.entity.enumerated.Semester;
import com.grandp.data.exception.notfound.entity.CurriculumNotFoundException;
import com.grandp.data.exception.notfound.entity.UserNotFoundException;
import com.grandp.data.entity.user.User;
import com.grandp.data.entity.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping(path = "/create/{userFacNum}")
    public ResponseEntity<?> createCurriculum(@RequestParam String semester, @PathVariable String userFacNum) throws UserNotFoundException {
        User user = userService.getUserByFacultyNumber(userFacNum);

        Semester s = Semester.of(semester);

        Curriculum curriculum = new Curriculum(s, user);

        curriculum.setUser(user);
        Curriculum returnVal = curriculumService.createCurriculum(curriculum);

        return ResponseEntity.ok(returnVal);
    }

    @GetMapping
    public List<Curriculum> getAllCurriculums() {
        return curriculumService.getAllCurriculums();
    }

    @GetMapping("/{id}") //FIXME make DTO
    public ResponseEntity<Curriculum> getCurriculumById(@PathVariable Long id) throws CurriculumNotFoundException {
        Curriculum curriculum = curriculumService.getCurriculumById(id);

        return ResponseEntity.ok(curriculum);
    }

    @DeleteMapping("/{id}")
    public void deleteCurriculum(@PathVariable Long id) {
        curriculumService.deleteCurriculum(id);
    }
}