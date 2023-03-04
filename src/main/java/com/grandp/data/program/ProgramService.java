package com.grandp.data.program;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class ProgramService {
    private final ProgramRepository programRepository;

    public ProgramService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    public Program save(Program program) {
        return programRepository.save(program);
    }

    public Optional<Program> findById(Long id) {
        return programRepository.findById(id);
    }

    public List<Program> findAll() {
        return programRepository.findAll();
    }
}