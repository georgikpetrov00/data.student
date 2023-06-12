package com.grandp.data.entity.subjectname;

import com.grandp.data.exception.notfound.entity.SubjectNameNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/subjectNames")
public class SubjectNameController {

    private final SubjectNameService subjectNameService;

    public SubjectNameController(SubjectNameService subjectNameService) {
        this.subjectNameService = subjectNameService;
    }

    @GetMapping
    public List<SubjectName> getAllSubjectNames() {
        return subjectNameService.getAllSubjectNames();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectNameDTO> getSubjectNameById(@PathVariable Long id) throws SubjectNameNotFoundException {
        SubjectName subjectName = subjectNameService.getSubjectNameById(id);

        return ResponseEntity.ok(new SubjectNameDTO(subjectName));
    }

    @PostMapping("/create/{subjectName}")
    public ResponseEntity<SubjectNameDTO> createSubjectName(@PathVariable String subjectName) {
        SubjectName createdSubjectName = subjectNameService.createSubjectName(subjectName);

        return ResponseEntity.created(URI.create("/subjectNames/" + createdSubjectName.getId())).body(new SubjectNameDTO(createdSubjectName));
    }

    @PutMapping("/update/{oldSubjectName}/{newSubjectName}")
    public ResponseEntity<SubjectNameDTO> updateSubjectName(@PathVariable String oldSubjectName, @PathVariable String newSubjectName) throws SubjectNameNotFoundException {
        SubjectName updatedSubjectName = subjectNameService.updateSubjectName(oldSubjectName, newSubjectName);

        return ResponseEntity.ok(new SubjectNameDTO(updatedSubjectName));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubjectName(@PathVariable Long id) {
        subjectNameService.deleteSubjectName(id);

        return ResponseEntity.ok("Subject Name with id: '" + id + "' deleted successfully.");
    }
}