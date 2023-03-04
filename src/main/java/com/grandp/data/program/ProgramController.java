package com.grandp.data.program;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/program")
public class ProgramController {
    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Program> getById(@PathVariable Long id) {
        Optional<Program> program = programService.findById(id);
        return program.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Program> create(@RequestBody Program program) {
        Program createdProgram = programService.save(program);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdProgram.getId()).toUri();
        return ResponseEntity.created(location).body(createdProgram);
    }

    @GetMapping
    public ResponseEntity<List<Program>> getAll() {
        List<Program> programs = programService.findAll();
        return ResponseEntity.ok(programs);
    }
}