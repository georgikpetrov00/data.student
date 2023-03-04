package com.grandp.data.student;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class StudentController {
	private final StudentService studentService;

	@Autowired
	public StudentController(StudentService courseService) {
		this.studentService = courseService;
	}

	@GetMapping
	public List<Student> getStudents() {
		return studentService.getStudents();
	}

	@RequestMapping("/get-by-id")
	public String getHelloWorld(@RequestParam String id) {
		return "Hello world! " + id;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Student> createStudent(@RequestBody Student student) {
		Student createdStudent = studentService.createStudent(student);
		return ResponseEntity.created(URI.create("/students/" + createdStudent.getId())).body(createdStudent);
	}
}