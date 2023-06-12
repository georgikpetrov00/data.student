package com.grandp.data.entity.dto;

import com.grandp.data.entity.authority.SimpleAuthority;
import com.grandp.data.entity.curriculum.Curriculum;
import com.grandp.data.entity.user.User;
import com.grandp.data.entity.student_data.StudentData;
import lombok.Getter;

import java.util.Set;

@Getter
public class UserDTO {

    private String firstName;
    private String lastName;
    private String email;

    private String personalId; // Uniform Civil Number
    private String facultyNumber;

    private Set<Curriculum> curriculums;
    private Set<SimpleAuthority> authorities; // ?? Maybe security session will contain this info

    public UserDTO(User user) {
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        personalId = user.getPersonalId();

        if (user.isStudent()) {
            StudentData studentData = user.getUserData();

            facultyNumber = studentData.getFacultyNumber();
            curriculums = studentData.getCurricula();
        } else {
            facultyNumber = null;
            curriculums = null;
        }

        authorities = user.getAuthorities();
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", personalId='" + personalId + '\'' +
                ", facultyNumber='" + facultyNumber + '\'' +
                ", curriculums=" + curriculums +
                ", authorities=" + authorities +
                '}';
    }
}
