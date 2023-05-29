package com.grandp.data.curriculum.subject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.grandp.data.curriculum.Curriculum;
import com.grandp.data.curriculum.subjectname.SubjectName;
import com.grandp.data.user.User;
import com.grandp.data.user.student.Semester;
import com.grandp.data.user.student_data.StudentData;
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

    public Subject(SubjectName name, Curriculum curriculum, Boolean passed, Integer grade, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, Semester semester) {
        this.name = name;
        this.curriculum = curriculum;
        this.passed = passed;
        this.grade = grade;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.semester = semester;
    }

    public Subject(SubjectName name, User user, Boolean passed, Integer grade, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, Semester semester) throws Exception {
        this.name = name;

        if (user.isStudent()) {
            StudentData studentData = (StudentData) user.getUserData(); //make this safe

            curriculum = studentData.getCurricula().stream()
                    .filter(curriculum1 -> curriculum1.getSemester().equals(semester))
                    .findFirst()
                    .orElseThrow(() -> new Exception("User: '" + studentData.getFacultyNumber() + "' does not have Curriculum for semester: '" + semester + "'."));
        } else {
            curriculum = null;
        }

        this.curriculum = curriculum;
        this.passed = passed;
        this.grade = grade;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.semester = semester;
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
        if (grade < 2 || grade > 6) {
            throw new IllegalArgumentException("Given grade either too small or too high. Possible grades: [2-6].");
        }
        this.grade = grade;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
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
}