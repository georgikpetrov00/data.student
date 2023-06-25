package com.grandp.data.entity.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	public Optional<User> getUserById(Long id);
	
	public Optional<User> getUserByEmail(String email);

	public User save(User user);

	@Query("SELECT u FROM User u JOIN u.studentData ud WHERE ud.facultyNumber = :facultyNumber")
	public Optional<User> findUserByFacultyNumber(String facultyNumber);

	Optional<User> getUserByPersonalId(String personalId);
}
