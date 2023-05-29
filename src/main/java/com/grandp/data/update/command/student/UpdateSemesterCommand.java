package com.grandp.data.update.command.student;

import com.grandp.data.update.command.Command;
import com.grandp.data.user.User;
import com.grandp.data.user.student.Semester;
import com.grandp.data.user.student_data.StudentData;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateSemesterCommand implements Command {

    private User user;
    private Semester semester;

    @Override
    public void execute() {
        StudentData studentData = user.getUserData();

        if (studentData == null) {
            throw new IllegalStateException("User with personal ID: '" + user.getPersonalId() + "' does not have Student Data assigned.");
        }

        user.getUserData().setSemester(semester);
    }
}
