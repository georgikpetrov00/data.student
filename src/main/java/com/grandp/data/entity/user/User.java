package com.grandp.data.entity.user;

import com.grandp.data.entity.student_data.StudentData;
import com.grandp.data.hasher.PasswordHash;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.grandp.data.entity.authority.SimpleAuthority;

import lombok.Builder;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Builder
@Table
@AllArgsConstructor
@Setter
@Entity
public class User implements SimpleUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "first_name")
	@Pattern(regexp = UserHelper.REGEX_NAME, message = UserHelper.INVALID_FNAME_MSG)
	private String firstName;

	@Column(name = "last_name")
	@Pattern(regexp = UserHelper.REGEX_NAME, message = UserHelper.INVALID_LNAME_MSG)
	private String lastName;

	@Column(name = "email", unique = true)
	@Email(message = UserHelper.EMAIL_CONSTRAINT)
	private String email;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "personal_email")
	@Email(message = UserHelper.EMAIL_CONSTRAINT)
	private String personalEmail;

	@Column(name = "personal_id")
	@Pattern(regexp = UserHelper.REGEX_PERSONAL_ID, message = UserHelper.INVALID_PID_MSG)
	private String personalId; // Uniform Civil Number

	@Column(name = "password")
	private String password;

	private boolean isExpired;

	private boolean isActive;

	private boolean isLocked;

	@OneToOne(fetch = FetchType.LAZY)
	private StudentData studentData;

	@ManyToMany(cascade = CascadeType.MERGE ,fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_authorities",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "authority_id"))
	private Set<SimpleAuthority> authorities = new HashSet<>();

	public User() {
		authorities.add(SimpleAuthority.STUDENT);
	}

	@JsonCreator
	public User(@JsonProperty("firstName") String firstName,
				@JsonProperty("lastName") String lastName,
				@JsonProperty("email") String email,
				@JsonProperty("personalId") String personalId,
				SimpleAuthority[] auths) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.personalId = checkNumericField(personalId, UserUtils.PERSONAL_ID);
		String pwd = firstName + personalId + ".";
//		this.password = PasswordHash.getInstanceSingleton().encode(pwd);
		this.password = PasswordHash.getInstanceSingleton().encode(personalId);

		for (SimpleAuthority sa : auths) {
			authorities.add(sa);
		}

		isActive = true;
		isExpired = false;
		isLocked = false;
	}

	private String checkField(String input, String fieldName) {
		assertNotNull(input, fieldName);

		return input;
	}

	private String checkNumericField(String personalId, String fieldName) {
		assertNotNull(personalId, fieldName);
		assertOnlyDigits(personalId, fieldName);

		return personalId;
	}

	private void assertNotNull(String input, String fieldName) {
		if (input == null) {
			throw new IllegalArgumentException(fieldName + " cannot be null.");
		}
	}

	private void assertOnlyDigits(String input, String fieldName) {
		if (!input.matches(UserUtils.REGEX_DIGITS_ONLY)) {
			throw new IllegalArgumentException(fieldName + " can contain only digits.");
		}
	}

	public long getId() {
		return this.id;
	}

	@Override
	public Set<SimpleAuthority> getAuthorities() {
		return authorities;
	}

	public void removeAuthority(SimpleAuthority authority) {
		this.authorities.remove(authority);
	}

	public void addAuthority(SimpleAuthority authority) {
		this.authorities.add(authority);
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getEmail() {
		return this.email;
	}

	public String getPersonalId() {
		return this.personalId;
	}

	public StudentData getStudentData() {
		return this.studentData;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getPersonalEmail() {
		return personalEmail;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return !this.isExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !this.isLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.isActive;
	}

	public boolean isAdministrator() {
		return this.authorities.contains(SimpleAuthority.ADMINISTRATOR);
	}

	public boolean isTeacher() {
		return this.authorities.contains(SimpleAuthority.TEACHER);
	}

	public boolean isStudent(){
		return hasAuthority("STUDENT");
//		return true;
	}

	/**
	 * @param authorityName - name of the Authority without prefix "ROLE"
	 * @return true in case the User have the role and false if not.
	 */
	public boolean hasAuthority(String authorityName) {
		String conventionName = authorityName.toUpperCase();

		for (SimpleAuthority sa : authorities) {
			if (sa.getName().equals(conventionName)) {
				return true;
			}
		}

		return false;
	}

	public boolean isGuest() {
		return this.authorities.contains(SimpleAuthority.GUEST);
	}

	public void setStudentData(StudentData studentData) {
		this.studentData = studentData;
	}

	public void setPassword(String newPassword) {
		if (newPassword == null || newPassword.isBlank()) {
			//cannot set null or empty passwords
			return;
		}

		PasswordHash hasher = PasswordHash.getInstanceSingleton();
		String hashedPwd = hasher.encode(newPassword);

		this.password= hashedPwd;
	}

	@Override
	public String toString() {
		return "User [" +
				"id=" + id + ", " +
				"firstName=" + firstName +
				", lastName=" + lastName +
				", email=" + email +
				", personalId=" + personalId +
				", isExpired=" + isExpired +
				", isActive=" + isActive +
				", isLocked=" + isLocked +
				", authorities=" + String.join(", ", authorities.stream().map(SimpleAuthority::getAuthority).toArray(String[]::new)) + "]";
	}
}