package com.grandp.data.entity.user;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.grandp.data.entity.authority.SimpleAuthority;
import com.grandp.data.entity.authority.SimpleAuthorityService;
import com.grandp.data.entity.curriculum.Curriculum;
import com.grandp.data.entity.subject.Subject;
import com.grandp.data.entity.subject.SubjectService;
import com.grandp.data.entity.subjectname.SubjectName;
import com.grandp.data.entity.subjectname.SubjectNameService;
import com.grandp.data.entity.dto.UserDTO;
import com.grandp.data.entity.enumerated.Degree;
import com.grandp.data.entity.enumerated.Semester;
import com.grandp.data.exception.notfound.entity.UserNotFoundException;
import com.grandp.data.entity.faculty.Faculty;
import com.grandp.data.entity.faculty.FacultyService;
import com.grandp.data.command.update.request.UpdateStudentDataRequest;
import com.grandp.data.command.update.request.UpdateUserRequest;
import com.grandp.data.entity.student_data.SimpleData;
import com.grandp.data.entity.student_data.StudentData;
import com.grandp.data.entity.student_data.StudentDataService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
	public ResponseEntity<?> getUserByEmail(@PathVariable @NotBlank(message = "Email cannot be null or empty.") String email) throws UserNotFoundException {
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

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
		User createdUser = userService.createUser(user);
		return ResponseEntity.created(URI.create("/user/" + createdUser.getId())).body(new UserDTO(createdUser));
	}

	@PutMapping("/assign-role/{facultyNumber}/{roleName}")
	public ResponseEntity<?> assignRole(@PathVariable @NotBlank(message = "Faculty number cannot be null or empty.") String facultyNumber,
										@PathVariable @NotBlank(message = "Role name cannot be null or empty.") String roleName) throws UserNotFoundException {

		User user = userService.assignRole(facultyNumber, roleName);
		UserDTO userDTO = new UserDTO(user);
		return ResponseEntity.ok(userDTO);
	}

	@PostMapping(path = "/set-student")
	public ResponseEntity<?> makeUserAStudent(@RequestParam @NotNull String personalId,
											  @RequestParam @NotNull String faculty,
											  @RequestParam @NotNull String facultyNumber,
											  @RequestParam @NotNull String semester,
											  @RequestParam @NotNull String degree) {
		User user = userService.getUserByPersonalId(personalId);

		Faculty facultyObj = facultyService.getFacultyByAbbreviation(faculty);
		Semester semesterObj;
		semesterObj = Semester.of(semester);

		Degree degreeObj = Degree.of(degree);

		StudentData studentData = new StudentData(user, facultyObj, degreeObj, semesterObj, facultyNumber);

		user.addAuthority(SimpleAuthority.STUDENT);
		user.setStudentData(studentData);

		studentDataService.save(studentData);
		userService.save(user);

		return ResponseEntity.ok("Successfully made User with personal ID: '" + personalId + "' a Student with Student Details: " + studentData.toString() + "'.");
	}

	@PostMapping(path = "/create-student")
	public ResponseEntity<?> createStudent(@RequestParam String personalId,
										   @RequestParam String firstName,
										   @RequestParam String lastName,
										   @RequestParam String email,
										   @RequestParam String faculty,
										   @RequestParam String facultyNumber,
										   @RequestParam String semester,
										   @RequestParam String degree) {
		SimpleAuthority authority = simpleAuthorityService.getAuthorityByName("ROLE_STUDENT");
		User user = new User(firstName, lastName, email, personalId);

		Faculty facultyObj = facultyService.getFacultyByAbbreviation(faculty);

		Semester semesterObj = Semester.of(semester);
		if (semesterObj == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Semester: '" + semester + "' does not exist.");
		}

		Degree degreeObj = Degree.of(degree);
		if (degreeObj == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Degree: '" + degree + "' does not exist.");
		}


		user.addAuthority(authority);

		StudentData studentData = new StudentData(user, facultyObj, degreeObj, semesterObj, facultyNumber);
		user.setStudentData(studentData);

		studentDataService.save(studentData);
		userService.save(user);
//		studentDataService.save(studentData);

		return ResponseEntity.ok("Successfully created Student: " + user);
	}

	@PostMapping(path = "/update-user/{personalId}") //FIXME: move business logic into Service
	public ResponseEntity<?> updateUser(@PathVariable @NotNull String personalId,
										@RequestParam(required = false) String firstName,
										@RequestParam(required = false) String lastName,
										@RequestParam(required = false) String newPersonalId,
										@RequestParam(required = false) String email) {

		User user = userService.getUserByPersonalId(personalId);
		UpdateUserRequest updateUserRequest = new UpdateUserRequest(user, firstName, lastName, newPersonalId, email, null, null);

		try {
			updateUserRequest.execute();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		userService.save(user);
		return ResponseEntity.ok("Successfully updated details of User: " + user);
	}

	@PostMapping(path = "/update-student/{personalId}") //FIXME: move busines logic into Service
	public ResponseEntity<?> updateStudent(@PathVariable @NotNull String personalId,
										   @RequestParam(required = false) String firstName,
										   @RequestParam(required = false) String lastName,
										   @RequestParam(required = false) String newPersonalId,
										   @RequestParam(required = false) String email,
										   @RequestParam(required = false) String faculty,
										   @RequestParam(required = false) String facultyNumber,
										   @RequestParam(required = false) String semester,
										   @RequestParam(required = false) String degree) {

		User user = userService.getUserByPersonalId(personalId);

		if (! user.isStudent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student with personal ID: '" + personalId + "' does not have any Student Data. Make the User a Student first.");
		}

		Faculty facultyObj = null;
		if (faculty != null) {
			facultyObj = facultyService.getFacultyByAbbreviation(faculty);
		}

		Semester semesterObj = null;
		if (semester != null) {
			semesterObj = Semester.of(semester);
		}

		Degree degreeObj = null;
		if (degree != null) {
			degreeObj = Degree.of(degree);
		}

		UpdateUserRequest updateUserRequest = new UpdateUserRequest(user, firstName, lastName, newPersonalId, email, null, null);
		UpdateStudentDataRequest updateStudentDataRequest = new UpdateStudentDataRequest(user, degreeObj, facultyObj, semesterObj, facultyNumber);

		try {
			updateUserRequest.execute();
			updateStudentDataRequest.execute();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		StudentData studentData = user.getStudentData();

		studentDataService.save(studentData);
		userService.save(user);
		return ResponseEntity.ok("Successfully updated Student: " + user.toString());
	}

}