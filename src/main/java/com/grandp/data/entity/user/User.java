package com.grandp.data.entity.user;

import java.util.*;

import com.grandp.data.entity.student_data.StudentData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.grandp.data.entity.authority.SimpleAuthority;

import lombok.Builder;
import lombok.Setter;

import static com.grandp.data.hasher.PasswordHasher.HASHER;

@Builder
@Entity
@Table
@AllArgsConstructor
@Setter
public class User implements SimpleUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;

	@Column(name = "personal_id")
	private String personalId; // Uniform Civil Number

	@Column(name = "password")
	private String password;

	// ============================================================================================
	private boolean isExpired;

	private boolean isActive;

	private boolean isLocked;

	@OneToOne
	private StudentData userData; //FIXME this should be SimpleData

	@ManyToMany(cascade = CascadeType.ALL ,fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_authorities",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "authority_id"))
	private Set<SimpleAuthority> authorities = new HashSet<>();


	public User() {
		
	}

	@JsonCreator
	public User(@JsonProperty("firstName") String firstName,
				@JsonProperty("lastName") String lastName,
				@JsonProperty("email") String email,
				@JsonProperty("personalId") String personalId) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.personalId = checkNumericField(personalId, UserUtils.PERSONAL_ID);
		this.password = HASHER.encode(personalId); // first password is the personalId
		
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
	
	public boolean hasAuthority(String authority) {
		return authorities.stream().anyMatch(auth -> auth.getAuthority().equalsIgnoreCase(authority));
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

	public StudentData getUserData() {
		return this.userData;
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
		if (! this.authorities.contains(SimpleAuthority.STUDENT)) {
//			throw new Exception("User with personalID '" + personalId + "' does not have Role 'STUDENT'.");
			return false;
		}

		if (! (this.userData instanceof StudentData)) {
//			throw new Exception("User with personalID '" + personalId + "' does not have assigned Student Data."); //FIXME trace this
			return false;
		}

		return true;
	}

	public boolean isGuest() {
		return this.authorities.contains(SimpleAuthority.GUEST);
	}

	public void setUserData(StudentData userData) {
		this.userData = userData;
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