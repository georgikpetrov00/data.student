package com.grandp.data.user;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.grandp.data.authorities.SimpleAuthority;
import com.grandp.data.authorities.SimpleAuthorityService;
import com.grandp.data.curriculum.Curriculum;
import com.grandp.data.curriculum.subject.Subject;
import com.grandp.data.curriculum.subject.SubjectService;
import com.grandp.data.curriculum.subjectname.SubjectName;
import com.grandp.data.curriculum.subjectname.SubjectNameService;
import com.grandp.data.faculty.Faculty;
import com.grandp.data.faculty.FacultyService;
import com.grandp.data.update.request.UpdateStudentDataRequest;
import com.grandp.data.update.request.UpdateUserRequest;
import com.grandp.data.user.student.Degree;
import com.grandp.data.user.student.Semester;
import com.grandp.data.user.student_data.SimpleData;
import com.grandp.data.user.student_data.StudentData;
import com.grandp.data.user.student_data.StudentDataService;
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
		Optional<User> userOpt = userService.getUserById(id);

		if (userOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id: '" + id + "' does not exist.");
		}

		UserDTO dto = new UserDTO(userOpt.get());
		return ResponseEntity.ok(dto);
	}
	
	@RequestMapping("/get-by-email/{email}")
	public Optional<User> getUserByEmail(@PathVariable @NotBlank(message = "Email cannot be null or empty.") String email) {
		return userService.getUserByEmail(email);
	}

	@RequestMapping("/get-by-fac-number/{number}")
	public ResponseEntity<?> getUserByFacultyNumber(@PathVariable @NotBlank(message = "Faculty number cannot be null or empty.") String facultyNumber) {
		Optional<User> userOpt = userService.getUserByFacultyNumber(facultyNumber);

		if (userOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with faculty number: '" + facultyNumber + "' does not exist.");
		}

		UserDTO dto = new UserDTO(userOpt.get());
		return ResponseEntity.ok(dto);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
		User createdUser = userService.createUser(user);
		return ResponseEntity.created(URI.create("/user/" + createdUser.getId())).body(new UserDTO(createdUser));
	}

	@PutMapping("/assign-role/{facultyNumber}/{roleName}")
	public ResponseEntity<?> assignRole(@PathVariable @NotBlank(message = "Faculty number cannot be null or empty.") String facultyNumber,
										@PathVariable @NotBlank(message = "Role name cannot be null or empty.") String roleName) {

		User user = userService.assignRole(facultyNumber, roleName);
		UserDTO userDTO = new UserDTO(user);
		return ResponseEntity.ok(userDTO);
	}

	@Deprecated
	@PutMapping(path = "set-grade/{facultyNumber}/{subjectName}/{grade}")
	public ResponseEntity<?> setGrade(@PathVariable @NotBlank(message = "Faculty number cannot be null or empty.") String facultyNumber,
									  @PathVariable @NotBlank(message = "Subject name cannot be null or empty.") String subjectName,
									  @PathVariable @Min(value = 2, message = "Grade must be a valid number [2-6].") @Max(value = 2, message = "Grade must be a valid number [2-6].") Integer grade) {

		return updateSubjectInfo(facultyNumber, subjectName, grade);
	}

	@Deprecated
	@PutMapping(path = "set-passed/{facultyNumber}/{subjectName}/{passed}")
	public ResponseEntity<?> setPassed(@PathVariable @NotBlank(message = "Faculty number cannot be null or empty.") String facultyNumber,
									   @PathVariable @NotBlank(message = "Subject name cannot be null or empty.") String subjectName,
									   @PathVariable @NotNull(message = "'Passed' cannot be null. Expected values: [true,false]") Boolean passed) {
		return updateSubjectInfo(facultyNumber, subjectName, passed);
	}

	@Deprecated
	private ResponseEntity<?> updateSubjectInfo(String facultyNumber, String subjectName, Object value) {
		if (facultyNumber == null || facultyNumber.equals("")) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Invalid parameter: 'faculty number'");
		}

		Optional<User> userOpt = userService.getUserByFacultyNumber(facultyNumber);
		// Check if User exists
		if (userOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find user with faculty number: '" + facultyNumber + "'.");
		}

		User user = userOpt.get();

		// Check if User is a Student
		try {
			user.isStudent();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getCause());
		}

		SimpleData studentData = user.getUserData();
		if (!(studentData instanceof StudentData)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student with faculty number: '" + facultyNumber + "' has invalid Student Data.");
		}

		Set<Curriculum> curricula = ((StudentData) studentData).getCurricula();

		if (curricula == null || curricula.isEmpty()) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Student with faculty number: '" + facultyNumber + "' currently does not have any assigned curriculums");
		}

		Optional<SubjectName> subjectNameOpt = subjectNameService.getSubjectNameByName(subjectName);

		if (subjectNameOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subject with name '" + subjectName + "' does not exist.");
		}

		SubjectName subjectNameObj = subjectNameOpt.get();
		Subject subject = null;

		for (Curriculum c : curricula) {
			for (Subject s : c.getSubjects()) {
				if (s.getName().equals(subjectNameObj)) {
					subject = s;
					break;
				}
			}
		}

		if (subject == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with faculty number: '" + facultyNumber + "' currently does not have assigned subject '" + subjectName + "'.");
		}

		// Update the subject with the provided value
		if (value instanceof Integer) {
			subject.setGrade((Integer) value);
		} else if (value instanceof Boolean) {
			subject.setPassed((Boolean) value);
		}

		subjectService.saveSubject(subject);

		return ResponseEntity.ok("Successfully updated subject: '" + subjectName + "' on user with faculty ID: '" + ((StudentData) studentData).getFacultyNumber() + "'.");
	}

//	@PutMapping(path = "set-grade/{facultyNumber}/{subjectName}/{grade}")
//	public ResponseEntity<?> setGrade(@PathVariable String facultyNumber, @PathVariable String subjectName, @PathVariable Integer grade) {
//		if (facultyNumber == null || facultyNumber.equals("")) {
//			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Invalid parameter: 'faculty number'");
//		}
//
//		Optional<User> userOpt = userService.getUserByFacultyNumber(facultyNumber);
//		// Check if User exists
//		if (userOpt.isEmpty()) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find user with faculty number: '" + facultyNumber + "'.");
//		}
//
//		User user = userOpt.get();
//
//		// Check if User is a Student
//		try {
//			user.isStudent();
//		} catch(Exception e) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getCause());
//		}
//
//		SimpleData studentData = user.getUserData();
//		if (! (studentData instanceof StudentData)) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student with faculty number: '" + facultyNumber + "' has invalid Student Data.");
//		}
//
//		Set<Curriculum> curricula = ((StudentData) studentData).getCurricula();
//
//
//		if (curricula == null || curricula.isEmpty()) {
//			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Student with faculty number: '" + facultyNumber + "' currently does not have any assigned curriculums");
//		}
//
//		Optional<SubjectName> subjectNameOpt = subjectNameService.getSubjectNameByName(subjectName);
//
//		if (subjectNameOpt.isEmpty()) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subject with name '" + subjectName + "' does not exist.");
//		}
//
//		SubjectName subjectNameObj = subjectNameOpt.get();
//		Subject subject = null;
//
//		for (Curriculum c : curricula) {
//			for (Subject s : c.getSubjects()) {
//				if (s.getName().equals(subjectNameObj)) {
//					subject = s;
//					break;
//				}
//			}
//		}
//
//		if (subject == null) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with faculty number: '" + facultyNumber + "' currently does not have assigned subject '" + subjectName + "'.");
//		}
//
//		subject.setGrade(grade);
//		subjectService.saveSubject(subject);
//
//		return ResponseEntity.ok("Successfully set '" + grade + "' on subject: '" + subjectName + "' on user with faculty ID: '" + ((StudentData) studentData).getFacultyNumber() +"'.");
//	}
//
//	@PutMapping(path = "set-passed/{facultyNumber}/{subjectName}/{passed}")
//	public ResponseEntity<?> setPassed(@PathVariable String facultyNumber, @PathVariable String subjectName, @PathVariable Boolean passed) {
//		if (facultyNumber == null || facultyNumber.equals("")) {
//			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Invalid parameter: 'faculty number'");
//		}
//
//		Optional<User> userOpt = userService.getUserByFacultyNumber(facultyNumber);
//		// Check if User exists
//		if (userOpt.isEmpty()) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find user with faculty number: '" + facultyNumber + "'.");
//		}
//
//		User user = userOpt.get();
//
//		// Check if User is a Student
//		try {
//			user.isStudent();
//		} catch(Exception e) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getCause());
//		}
//
//		SimpleData studentData = user.getUserData();
//		if (! (studentData instanceof StudentData)) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student with faculty number: '" + facultyNumber + "' has invalid Student Data.");
//		}
//
//		Set<Curriculum> curricula = ((StudentData) studentData).getCurricula();
//
//
//		if (curricula == null || curricula.isEmpty()) {
//			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Student with faculty number: '" + facultyNumber + "' currently does not have any assigned curriculums");
//		}
//
//		Optional<SubjectName> subjectNameOpt = subjectNameService.getSubjectNameByName(subjectName);
//
//		if (subjectNameOpt.isEmpty()) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subject with name '" + subjectName + "' does not exist.");
//		}
//
//		SubjectName subjectNameObj = subjectNameOpt.get();
//		Subject subject = null;
//
//		for (Curriculum c : curricula) {
//			for (Subject s : c.getSubjects()) {
//				if (s.getName().equals(subjectNameObj)) {
//					subject = s;
//					break;
//				}
//			}
//		}
//
//		if (subject == null) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with faculty number: '" + facultyNumber + "' currently does not have assigned subject '" + subjectName + "'.");
//		}
//
//		subject.setPassed(passed);
//		subjectService.saveSubject(subject);
//
//		return ResponseEntity.ok("Successfully set 'passed' status on subject: '" + subjectName + "' on user with faculty ID: '" + ((StudentData) studentData).getFacultyNumber() +"'.");
//	}

	@PostMapping(path = "/set-student")
	public ResponseEntity<?> makeUserAStudent(@RequestParam @NotNull String personalId,
											  @RequestParam @NotNull String faculty,
											  @RequestParam @NotNull String facultyNumber,
											  @RequestParam @NotNull String semester,
											  @RequestParam @NotNull String degree) {
		Optional<User> userOpt = userService.getUserByPersonalId(personalId);

		if (userOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with personalId: '" + personalId + "' does not exist.");
		}

		User user = userOpt.get();
		// ========================================================================

		Optional<Faculty> facultyOpt = facultyService.getFacultyByAbbreviation(faculty);
		if (facultyOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Faculty with Abbreviation: '" + faculty + "' does not exist.");
		}
		Faculty facultyObj = facultyOpt.get();

		Semester semesterObj;
		semesterObj = Semester.of(semester);

		Degree degreeObj = Degree.of(degree);

		StudentData studentData = new StudentData(facultyObj, degreeObj, semesterObj, new HashSet<>(), facultyNumber);

		user.addAuthority(SimpleAuthority.STUDENT);
		user.setUserData(studentData);

		studentDataService.save(studentData);
		userService.save(user);

		return ResponseEntity.ok("Successfully made User with personal ID: '" + personalId + "' a Student with Student Details: " + studentData.toString() + "'.");
	}

	@PostMapping(path = "/create-student/{personalId}")
	public ResponseEntity<?> createStudent(@PathVariable @NotNull String personalId,
										   @RequestParam String firstName,
										   @RequestParam String lastName,
										   @RequestParam String email,
										   @RequestParam String faculty,
										   @RequestParam String facultyNumber,
										   @RequestParam String semester,
										   @RequestParam String degree) {

		User user = new User(firstName, lastName, email, personalId);

		Optional<Faculty> facultyOpt = facultyService.getFacultyByAbbreviation(faculty);
		if (facultyOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Faculty with Abbreviation: '" + faculty + "' does not exist.");
		}
		Faculty facultyObj = facultyOpt.get();

		Semester semesterObj = Semester.of(semester);
		if (semesterObj == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Semester: '" + semester + "' does not exist.");
		}

		Degree degreeObj = Degree.of(degree);
		if (degreeObj == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Degree: '" + degree + "' does not exist.");
		}

		Optional<SimpleAuthority> authorityOpt = simpleAuthorityService.getAuthorityByName(SimpleAuthority.STUDENT.getName());
		if (authorityOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Role 'Student' not yet initialized. Call administrator.");
		}
		SimpleAuthority authority = authorityOpt.get();

		StudentData studentData = new StudentData(facultyObj, degreeObj, semesterObj, new HashSet<>(), facultyNumber);

		user.setUserData(studentData);
		user.addAuthority(authority);

		studentDataService.save(studentData);
		userService.save(user);
		return ResponseEntity.ok("Successfully created Student: " + user.toString());
	}

	@PostMapping(path = "/update-user/{personalId}") //FIXME: move busines logic into Service
	public ResponseEntity<?> updateUser(@PathVariable @NotNull String personalId,
										@RequestParam(required = false) String firstName,
										@RequestParam(required = false) String lastName,
										@RequestParam(required = false) String newPersonalId,
										@RequestParam(required = false) String email) {

		Optional<User> userOpt = userService.getUserByPersonalId(personalId);
		if (userOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with personal ID: '" + personalId + "' does not exist.");
		}
		User user = userOpt.get();

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

		Optional<User> userOpt = userService.getUserByPersonalId(personalId);
		if (userOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with personal ID: '" + personalId + "' does not exist.");
		}
		User user = userOpt.get();
		if (! user.isStudent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with personal ID: '" + personalId + "' does not have any Student Data. Make him a Student first.");
		}

		Faculty facultyObj = null;
		if (faculty != null) {
			Optional<Faculty> facultyOpt = facultyService.getFacultyByAbbreviation(faculty);
			if (facultyOpt.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Faculty with Abbreviation: '" + faculty + "' does not exist.");
			}
			facultyObj = facultyOpt.get();
		}

		Semester semesterObj = null;
		if (semester != null) {
			semesterObj = Semester.of(semester);
			if (semesterObj == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Semester: '" + semester + "' does not exist.");
			}
		}

		Degree degreeObj = null;
		if (degree != null) {
			degreeObj = Degree.of(degree);
			if (degreeObj == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Degree: '" + degree + "' does not exist.");
			}
		}

		UpdateUserRequest updateUserRequest = new UpdateUserRequest(user, firstName, lastName, newPersonalId, email, null, null);
		UpdateStudentDataRequest updateStudentDataRequest = new UpdateStudentDataRequest(user, degreeObj, facultyObj, semesterObj, facultyNumber);

		try {
			updateUserRequest.execute();
			updateStudentDataRequest.execute();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		StudentData studentData = user.getUserData();

		studentDataService.save(studentData);
		userService.save(user);
		return ResponseEntity.ok("Successfully updated Student: " + user.toString());
	}
}