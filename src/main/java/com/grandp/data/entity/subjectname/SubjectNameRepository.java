package com.grandp.data.entity.subjectname;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectNameRepository extends JpaRepository<SubjectName, Long> {
    Optional<SubjectName> getSubjectNameByName(String name);

    boolean existsByName(String name);
}