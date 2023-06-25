package com.grandp.data.entity.subject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.grandp.data.entity.enumerated.Semester;
import com.grandp.data.entity.curriculum.Curriculum;
import com.grandp.data.entity.subjectname.SubjectName;
import com.grandp.data.entity.user.User;
import com.grandp.data.entity.student_data.StudentData;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Getter
@Table(name = "subject", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@JsonIgnoreProperties("hibernateLazyInitializer")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "name")
    private SubjectName name;

    @ManyToOne
    @JoinColumn(name = "curriculum_id", referencedColumnName = "id")
    @JsonIgnore
    private Curriculum curriculum; //TODO consider following - creating curriculum for whole faculty

    @Column(name = "passed")
    private Boolean passed;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "semester")
    @Enumerated(EnumType.STRING)
    private Semester semester;

    public Subject() {
    	
    }

    public Subject(SubjectName name, User user, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, Semester semester) throws Exception {
        this.name = name;

        if (user.isStudent()) {
            StudentData studentData = (StudentData) user.getStudentData(); //make this safe

            curriculum = studentData.getCurricula().stream()
                    .filter(curriculum1 -> curriculum1.getSemester().equals(semester))
                    .findFirst()
                    .orElseThrow(() -> new Exception("User: '" + studentData.getFacultyNumber() + "' does not have Curriculum for semester: '" + semester + "'."));
        } else {
            curriculum = null;
        }

        this.passed = false;
        this.grade = 1;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.semester = semester;
    }

    private Subject(Subject copySubject, User destinationUser) {
        this.name = copySubject.name;
        this.passed = false;
        this.grade = 1;
        this.startTime = copySubject.startTime;
        this.endTime = copySubject.endTime;
        this.dayOfWeek = copySubject.dayOfWeek;
        this.semester = copySubject.semester;

        this.curriculum = destinationUser.getStudentData().getCurricula()
                .stream()
                .filter(curriculum1 -> curriculum1.getSemester().equals(this.semester))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("User @" + destinationUser.getPersonalId() + " does not have Curriculum for Semester: " + semester));
    }

    public Curriculum getCurriculum() {
        return curriculum;
    }

    public String getNameAsString() {
        return this.name.getName();
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }

    public void setGrade(Integer grade) {
        if (grade < 1 || grade > 6) {
            throw new IllegalArgumentException("Given grade either too small or too high. Possible grades: [2-6] or [1]:not applied.");
        }
        this.grade = grade;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        if(dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY)) {
            throw new IllegalArgumentException("Cannot schedule a Subject for Saturday or Sunday.");
        }

        this.dayOfWeek = dayOfWeek;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public void setCurriculum(Curriculum curriculum) {
        this.curriculum = curriculum;
    }

    public void setName(SubjectName subjectName) {
        this.name = subjectName;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "name=" + name +
                ", passed=" + passed +
                ", grade=" + grade +
                ", dayOfWeek=" + dayOfWeek +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(id, subject.id) && Objects.equals(name, subject.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    //TODO: think of static method "copyOf" which will be used to assign a subject to many users, but each user will have his own subject

    /**
     * Method to be used for assigning a subject to multiple Students at once.
     * Usage: Create a Subject object. Call it for each student you want to copy the Subject to. Assign the received Subject to the Student.
     * @param copyOf: Original Subject.
     * @param copyTo: Student to receive the subject.
     * @return: Subject object to be assigned to the Student
     */
    public static Subject copyOf(Subject copyOf, User copyTo) {
        if (! copyTo.isStudent()) {
            throw new IllegalArgumentException("User @" + copyOf.hashCode() + " is not a Student.");
        }

        StudentData studentData = copyTo.getStudentData();

        Subject copySubject = new Subject(copyOf, copyTo);
        return copySubject;
    }
}