package com.grandp.data.entity.dto;

import com.grandp.data.entity.curriculum.Curriculum;
import com.grandp.data.entity.user.User;
import lombok.Getter;

import java.util.Set;

@Getter
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    private String personalId; // Uniform Civil Number

    public UserDTO(User user) {
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        personalId = user.getPersonalId();
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", personalId='" + personalId + '\'' +
                '}';
    }
}
