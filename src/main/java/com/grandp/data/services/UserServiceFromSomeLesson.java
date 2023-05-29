package com.grandp.data.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.grandp.data.dto.ImageDto;
import com.grandp.data.dto.MessageDto;
import com.grandp.data.dto.ProfileDto;
import com.grandp.data.dto.SignUpDto;
import com.grandp.data.dto.UserDto;
import com.grandp.data.dto.UserSummaryDto;

@Service
public class UserServiceFromSomeLesson {

    public ProfileDto getProfile(Long userId) {
        return new ProfileDto(new UserSummaryDto(1L, "Sergio", "Lema"),
                Arrays.asList(new UserSummaryDto(2L, "John", "Doe")),
                Arrays.asList(new MessageDto(1L, "My message")),
                Arrays.asList(new ImageDto(1L, "Title", null)));
    }

    public void addFriend(Long friendId) {
        return;
    }

    public List<UserSummaryDto> searchUsers(String term) {
        return Arrays.asList(new UserSummaryDto(1L, "Sergio", "Lema"),
                new UserSummaryDto(2L, "John", "Doe"));
    }

    public UserDto signUp(SignUpDto user) {
        return new UserDto(1L, "Sergio", "Lema", "login", "token");
    }

    public void signOut(UserDto user) {
        // nothing to do at the moment
    }
}
