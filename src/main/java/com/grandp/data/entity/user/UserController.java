package com.grandp.data.entity.user;

import java.net.URI;
import java.util.List;

import com.grandp.data.entity.authority.SimpleAuthority;
import com.grandp.data.entity.authority.SimpleAuthorityService;
import com.grandp.data.entity.subject.SubjectService;
import com.grandp.data.entity.subjectname.SubjectNameService;
import com.grandp.data.entity.dto.UserDTO;
import com.grandp.data.entity.enumerated.Degree;
import com.grandp.data.entity.enumerated.Semester;
import com.grandp.data.exception.notfound.entity.UserNotFoundException;
import com.grandp.data.entity.faculty.Faculty;
import com.grandp.data.entity.faculty.FacultyService;
import com.grandp.data.command.update.request.UpdateStudentDataRequest;
import com.grandp.data.command.update.request.UpdateUserRequest;
import com.grandp.data.entity.student_data.StudentData;
import com.grandp.data.entity.student_data.StudentDataService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

	// These are injected from AllArgsConstructor
	private final FacultyService facultyService;
	private final StudentDataService studentDataService;
	private final SubjectService subjectService;
	private final SubjectNameService subjectNameService;
	private final UserService userService;
	private final SimpleAuthorityService simpleAuthorityService;


	@GetMapping
	public List<User> getUsers() {
		return userService.getUsers();
	}

	@RequestMapping("/get-by-id/{id}")
	public ResponseEntity<?> getUserById(@PathVariable @NotBlank(message = "Email cannot be null or empty.") Long id) {
		User user = userService.getUserById(id);

		UserDTO dto = new UserDTO(user);
		return ResponseEntity.ok(dto);
	}
	
	@RequestMapping("/get-by-email/{email}")
	public ResponseEntity<?> getUserByEmail(@PathVariable @NotNull(message = "Email cannot be null")
																					@NotBlank(message = "Email cannot be empty.") String email)
																					throws UserNotFoundException {

		User user= userService.getUserByEmail(email);
		UserDTO dto = new UserDTO(user);
		return ResponseEntity.ok(dto);
	}

	@RequestMapping("/get-by-fac-number/{facultyNumber}")
	public ResponseEntity<?> getUserByFacultyNumber(@PathVariable @NotBlank(message = "Faculty number cannot be null or empty.") String facultyNumber) throws UserNotFoundException {
		User user = userService.getUserByFacultyNumber(facultyNumber);
		UserDTO dto = new UserDTO(user);
		return ResponseEntity.ok(dto);
	}

	@PostMapping(path = "/create")
	public ResponseEntity<UserDTO> createUser(
		@RequestParam @Pattern(regexp = UserHelper.REGEX_NAME, message = UserHelper.INVALID_FNAME_MSG)	String firstName ,
		@RequestParam @Pattern(regexp = UserHelper.REGEX_NAME, message = UserHelper.INVALID_LNAME_MSG) String lastName,
		@RequestParam @Email(message = UserHelper.EMAIL_CONSTRAINT)String email,
		@RequestParam @Pattern(regexp = UserHelper.REGEX_PERSONAL_ID, message = UserHelper.INVALID_PID_MSG)String personalId) {

		SimpleAuthority authority = simpleAuthorityService.getAuthorityByName("STUDENT");
		User createdUser = new User(firstName, lastName, email, personalId, new SimpleAuthority[] {authority});

		userService.save(createdUser);
		return ResponseEntity.created(URI.create("/user/" + createdUser.getId())).body(new UserDTO(createdUser));
	}

	@PutMapping("/assign-role/{personalId}/{roleName}")
	public ResponseEntity<?> assignRole(@PathVariable @NotBlank(message = "Personal ID number cannot be null or empty.") String personalId,
										@PathVariable @NotBlank(message = "Role name cannot be null or empty.") String roleName) throws UserNotFoundException {

		User user = userService.assignRole(personalId, roleName);
		UserDTO userDTO = new UserDTO(user);
		return ResponseEntity.ok(userDTO);
	}

	@PutMapping("/remove-role/{personalId}/{roleName}")
	public ResponseEntity<?> removeRole(@PathVariable @NotBlank(message = "Personal ID number cannot be null or empty.") String personalId,
										@PathVariable @NotBlank(message = "Role name cannot be null or empty.") String roleName) throws UserNotFoundException {

		User user = userService.removeRole(personalId, roleName);
		UserDTO userDTO = new UserDTO(user);
		return ResponseEntity.ok("Successfully created: " + userDTO);
	}

	@PostMapping(path = "/set-student")
	public ResponseEntity<?> makeUserAStudent(@RequestParam String personalId,
											  @RequestParam String faculty,
											  @RequestParam String facultyNumber,
											  @RequestParam String degree,
											  @RequestParam String potok,
												@RequestParam String groupName) {
		User user = userService.getUserByPersonalId(personalId);

		Faculty facultyObj = facultyService.getFacultyByAbbreviation(faculty);

		Degree degreeObj = Degree.of(degree);

		StudentData studentData = new StudentData(user, facultyObj, degreeObj, facultyNumber, potok, groupName);

		user.addAuthority(SimpleAuthority.STUDENT);
		user.setStudentData(studentData);

		studentDataService.save(studentData);
		userService.save(user);

		return ResponseEntity.ok("Successfully made User with personal ID: '" + personalId + "' a Student with Student Details: " + studentData.toString() + "'.");
	}

	@PostMapping(path = "/create-student")
	public ResponseEntity<?> createStudent(
											 @RequestParam String personalId,
										   @RequestParam String firstName,
										   @RequestParam String lastName,
										   @RequestParam String email,
										   @RequestParam String faculty,
										   @RequestParam String facultyNumber,
										   @RequestParam String degree,
											 @RequestParam String potok,
											 @RequestParam String groupName) {
		SimpleAuthority authority = simpleAuthorityService.getAuthorityByName("STUDENT");
		User user = new User(firstName, lastName, email, personalId, new SimpleAuthority[]{authority});
		userService.save(user);

		Faculty facultyObj = facultyService.getFacultyByAbbreviation(faculty);

		Degree degreeObj = Degree.of(degree);
		if (degreeObj == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Degree: '" + degree + "' does not exist.");
		}

		user = userService.getUserByEmail(email);
		StudentData studentData = new StudentData(user, facultyObj, degreeObj, facultyNumber, potok, groupName);
		user.setStudentData(studentData);

		studentDataService.save(studentData);
		userService.save(user);
//		studentDataService.save(studentData);

		return ResponseEntity.ok("Successfully created Student: " + user);
	}

	@PostMapping(path = "/update")
	public ResponseEntity<?> updateUser(
									  @RequestParam @NotNull String personalId,
										@RequestParam(required = false) String firstName,
										@RequestParam(required = false) String lastName,
										@RequestParam(required = false) String newPersonalId,
										@RequestParam(required = false) String email,
										@RequestParam(required = false) String phoneNumber,
										@RequestParam(required = false) String personalEmail) {

		User user = userService.getUserByPersonalId(personalId);
		UpdateUserRequest updateUserRequest = new UpdateUserRequest(user, firstName, lastName, newPersonalId, email, null, null);

		try {
			updateUserRequest.execute();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		if (phoneNumber != null && phoneNumber.length() > 8) {
			user.setPhoneNumber(phoneNumber);
		}

		if (personalEmail != null && personalEmail.length() > 8) {
			user.setPersonalEmail(personalEmail);
		}

		userService.save(user);
		return ResponseEntity.ok("Successfully updated details of User: " + user);
	}

	@PostMapping(path = "/update-student") //FIXME: move business logic into Service
	public ResponseEntity<?> updateStudent(
											 @RequestParam @NotNull String personalId,
										   @RequestParam(required = false) String faculty,
										   @RequestParam(required = false) String facultyNumber,
										   @RequestParam(required = false) String semester,
										   @RequestParam(required = false) String degree,
										 	 @RequestParam String potok,
										 	 @RequestParam String groupName) {

		User user = userService.getUserByPersonalId(personalId);

		if (! user.isStudent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student with personal ID: '" + personalId + "' does not have any Student Data. Make the User a Student first.");
		}

		Faculty facultyObj = null;
		if (faculty != null && faculty.length() > 0) {
			facultyObj = facultyService.getFacultyByAbbreviation(faculty);
		}

		Semester semesterObj = null;
		if (semester != null && semester.length() > 0) {
			semesterObj = Semester.of(semester);
		}

		Degree degreeObj = null;
		if (degree != null && degree.length() > 0) {
			degreeObj = Degree.of(degree);
		}

		StudentData studentData = studentDataService.getStudentDataByUserID(user.getId());
		UpdateStudentDataRequest updateStudentDataRequest = new UpdateStudentDataRequest(user, studentData, degreeObj, facultyObj, semesterObj, facultyNumber);

		try {
			updateStudentDataRequest.execute();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		if (potok != null && !potok.isBlank()) {
			studentData.setPotok(potok);
		}

		if (groupName != null && !groupName.isBlank()) {
			studentData.setGroupName(groupName);
		}

		studentDataService.save(studentData);
		userService.save(user);
		return ResponseEntity.ok("Successfully updated Student: " + user);
	}
}