package com.grandp.data.update.request;

import com.grandp.data.faculty.Faculty;
import com.grandp.data.update.command.Command;
import com.grandp.data.update.command.student.UpdateDegreeCommand;
import com.grandp.data.update.command.student.UpdateFacultyCommand;
import com.grandp.data.update.command.student.UpdateFacultyNumberCommand;
import com.grandp.data.update.command.student.UpdateSemesterCommand;
import com.grandp.data.update.command.user.*;
import com.grandp.data.user.User;
import com.grandp.data.user.student.Degree;
import com.grandp.data.user.student.Semester;
import lombok.AllArgsConstructor;

import java.util.LinkedList;
import java.util.List;

public class UpdateStudentDataRequest implements UpdateRequest {

    private Degree degree;
    private Faculty faculty;
    private Semester semester;
    private String facultyNumber;

    private User user;
    private List<Command> commands = new LinkedList<>();

    public UpdateStudentDataRequest(User user, Degree degree, Faculty faculty, Semester semester, String facultyNumber) {
        this.user = user;
        this.degree = degree;
        this.faculty = faculty;
        this.semester = semester;
        this.facultyNumber = facultyNumber;
    }

    @Override
    public void execute() {
        load();

        for (Command c : commands) {
            c.execute();
        }
    }

    private void load() {
        if (degree != null) {
            commands.add(new UpdateDegreeCommand(user, degree));
        }

        if (faculty != null) {
            commands.add(new UpdateFacultyCommand(user, faculty));
        }

        if (semester != null) {
            commands.add(new UpdateSemesterCommand(user, semester));
        }

        if (facultyNumber != null) {
            commands.add(new UpdateFacultyNumberCommand(user, facultyNumber));
        }
    }
}
