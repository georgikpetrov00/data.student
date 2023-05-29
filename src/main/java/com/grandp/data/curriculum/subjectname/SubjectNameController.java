package com.grandp.data.curriculum.subjectname;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<SubjectNameDTO> getSubjectNameById(@PathVariable Long id) {
        SubjectName subjectName = subjectNameService.getSubjectNameById(id);
        if (subjectName != null) {
            return ResponseEntity.ok(new SubjectNameDTO(subjectName));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<SubjectNameDTO> createSubjectName(@RequestBody SubjectName subjectName) {
        Optional<SubjectName> optionalSubjectName = subjectNameService.getSubjectNameByName(subjectName.getName());

        SubjectName createdSubjectName = subjectNameService.createSubjectName(subjectName);
        return ResponseEntity.created(URI.create("/subjectNames/" + createdSubjectName.getId())).body(new SubjectNameDTO(createdSubjectName));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectNameDTO> updateSubjectName(@PathVariable Long id, @RequestBody SubjectName subjectName) {
        SubjectName updatedSubjectName = subjectNameService.updateSubjectName(id, subjectName);
        if (updatedSubjectName != null) {
            return ResponseEntity.ok(new SubjectNameDTO(updatedSubjectName));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubjectName(@PathVariable Long id) {
        if (subjectNameService.deleteSubjectName(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}