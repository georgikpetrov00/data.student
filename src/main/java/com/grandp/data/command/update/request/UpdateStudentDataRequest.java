package com.grandp.data.command.update.request;

import com.grandp.data.command.Command;
import com.grandp.data.entity.faculty.Faculty;
import com.grandp.data.command.update.student_data.UpdateStudentDataDegreeCommand;
import com.grandp.data.command.update.student_data.UpdateStudentDataFacultyCommand;
import com.grandp.data.command.update.student_data.UpdateStudentDataFacultyNumberCommand;
import com.grandp.data.command.update.student_data.UpdateStudentDataSemesterCommand;
import com.grandp.data.entity.student_data.StudentData;
import com.grandp.data.entity.user.User;
import com.grandp.data.entity.enumerated.Degree;
import com.grandp.data.entity.enumerated.Semester;
import com.grandp.data.exception.CommandCannotBeExecutedException;

import java.util.LinkedList;
import java.util.List;

public class UpdateStudentDataRequest implements UpdateRequest {

    private Degree degree;
    private Faculty faculty;
    private Semester semester;
    private String facultyNumber;

    private User user;
    private StudentData studentData;
    private List<Command> commands = new LinkedList<>();

    public UpdateStudentDataRequest(User user, StudentData studentData, Degree degree, Faculty faculty, Semester semester, String facultyNumber) {
        this.user = user;
        this.studentData = studentData;
        this.degree = degree;
        this.faculty = faculty;
        this.semester = semester;
        this.facultyNumber = facultyNumber;
    }

    @Override
    public void execute() {
        load();

        for (Command c : commands) {
            try {
                c.execute();
            } catch (CommandCannotBeExecutedException e) {
                System.out.println("An exception occurred while executing " + c.getClass() + " cannot be executed. " + e.getMessage() + ". Reverting all changes.");
                revert();
            }
        }
    }

    @Override
    public void revert() {
        for (Command c : commands) {
            c.revert();
        }
    }

    private void load() {
        if (degree != null) {
            commands.add(new UpdateStudentDataDegreeCommand(user, studentData, degree));
        }

        if (faculty != null) {
            commands.add(new UpdateStudentDataFacultyCommand(user, studentData,faculty));
        }

        if (semester != null) {
            commands.add(new UpdateStudentDataSemesterCommand(user, studentData,semester));
        }

        if (facultyNumber != null && facultyNumber.length() >= 8) {
            commands.add(new UpdateStudentDataFacultyNumberCommand(user, studentData,facultyNumber));
        }
    }
}
