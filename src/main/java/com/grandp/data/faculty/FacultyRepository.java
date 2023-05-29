package com.grandp.data.faculty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
	
	public Faculty save(Faculty faculty);

    Optional<Faculty> getFacultyByAbbreviation(String abbreviation);
}
