package com.grandp.data.curriculum.subjectname;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectNameRepository extends JpaRepository<SubjectName, Long> {
    Optional<SubjectName> getSubjectNameByName(String name);

}