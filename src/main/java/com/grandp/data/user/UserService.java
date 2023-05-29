package com.grandp.data.user;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.grandp.data.authorities.SimpleAuthority;
import com.grandp.data.authorities.SimpleAuthorityService;
import com.grandp.data.exception.UserNotFoundException;
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

	public Optional<User> getUserById(Long id) {
		Optional<User> user = userRepository.getUserById(id);

		return user;
	}

	public User save(User user) {
		return userRepository.save(user);
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.getUserByEmail(email);
	}
	
	public User createUser(User user) {
		User createdUser = userRepository.save(user);
		return createdUser;
	}

    public Optional<User> getUserByFacultyNumber(String facultyNumber) {
		Optional<User> user = userRepository.findByStudentDataFacultyNumber(facultyNumber);
		return user;
    }

	public User assignRole(String facultyNumber, String roleName) {
		assertValid(facultyNumber, "facultyNumber");
		assertValid(roleName, "roleName");

		Optional<User> optionalUser = userRepository.findByStudentDataFacultyNumber(facultyNumber);
		User user;

		if (optionalUser.isPresent()) {
			user = optionalUser.get();
		} else {
			throw new IllegalArgumentException("User with Faculty Number: '" + facultyNumber + "' does not exist.");
		}

		Optional<SimpleAuthority> optionalAuthority = simpleAuthorityService.getAuthorityByName(roleName);
		SimpleAuthority role;

		if (optionalAuthority.isPresent()) {
			role = optionalAuthority.get();
		} else {
			throw new IllegalArgumentException("Role with Name: '" + roleName + "' does not exist.");
		}

		Collection<SimpleAuthority> roles = user.getAuthorities();
		roles.add(role);

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

	public Optional<User> getUserByPersonalId(String personalId) {
		Optional<User> user = userRepository.getUserByPersonalId(personalId);
		return user;
	}
}
