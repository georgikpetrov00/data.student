package com.grandp.data.entity.user;

import java.util.List;
import java.util.Optional;

import com.grandp.data.entity.authority.SimpleAuthority;
import com.grandp.data.entity.authority.SimpleAuthorityService;
import com.grandp.data.exception.notfound.entity.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final SimpleAuthorityService simpleAuthorityService;

	public List<User> getUsers() {
		return userRepository.findAll();
	}

	public User getUserById(Long id) {
		return userRepository.getUserById(id).orElseThrow(() -> new UserNotFoundException("User with id: '" + id + "' not found."));
	}

	public User save(User user) throws ConstraintViolationException {
		return userRepository.save(user);
	}

	public User getUserByEmail(String email) throws UserNotFoundException {
		return userRepository.getUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email: '" + email +"' not found."));
	}
	
	public User createUser(User user) {
		User createdUser = userRepository.save(user);
		return createdUser;
	}

	public User getUserByFacultyNumber(String facultyNumber) throws UserNotFoundException {
		Optional<User> optionalUser = userRepository.findUserByFacultyNumber(facultyNumber);
		return optionalUser.orElseThrow(() -> new UserNotFoundException("User with faculty number: '" + facultyNumber + "' does not exist."));
	}

	public User assignRole(String personalId, String roleName) throws UserNotFoundException {
		assertValid(personalId, "personalId");
		assertValid(roleName, "roleName");

		User user = getUserByPersonalId(personalId);

		SimpleAuthority role = simpleAuthorityService.getAuthorityByName(roleName);
		user.addAuthority(role);
		userRepository.save(user);

		return user;
	}

	public User removeRole(String personalId, String roleName) {
		assertValid(personalId, "personalId");
		assertValid(roleName, "roleName");

		Optional<User> optionalUser = userRepository.getUserByPersonalId(personalId);
		User user;

		if (optionalUser.isPresent()) {
			user = optionalUser.get();
		} else {
			throw new IllegalArgumentException("User with Personal Id: '" + personalId + "' does not exist.");
		}

		SimpleAuthority role = simpleAuthorityService.getAuthorityByName(roleName);
		user.removeAuthority(role);
		userRepository.save(user);

		return user;
	}

	private void assertValid(Object object, String fieldName) {
		if (object == null) {
			throw new IllegalArgumentException("Param: '" + fieldName + "' is null.");
		}

		if (object instanceof String && object.equals("")) {
			throw new IllegalArgumentException("Param: '" + fieldName + "' is empty String.");
		}
	}

	public User getUserByPersonalId(String personalId) {
		return userRepository.getUserByPersonalId(personalId).orElseThrow(() -> new UserNotFoundException("User with personal ID: '" + personalId + "' does not exist."));
	}
}
