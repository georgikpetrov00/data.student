package com.grandp.data.entity.curriculum;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.grandp.data.entity.enumerated.Semester;
import com.grandp.data.entity.subject.Subject;
import com.grandp.data.entity.user.User;
import com.grandp.data.entity.student_data.StudentData;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
public class Curriculum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "semester")
    @Enumerated(EnumType.STRING)
    private Semester semester;

    @OneToMany(mappedBy = "curriculum")
    private Set<Subject> subjects = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "studentData")
    @JsonBackReference
    private StudentData studentData;

    public Curriculum() {
    }

    // Do not call from code. Only invoke via web request.
    @JsonCreator
    public Curriculum(@JsonProperty("semester") String semester, @JsonProperty("user") User user) {
        this.semester = Semester.valueOf(semester);
        this.studentData = user.getStudentData();
        this.subjects = new HashSet<>();
    }

    public Curriculum(Semester semester, @JsonProperty("user") User user) {
        this.semester = semester;
        this.studentData = user.getStudentData();
        this.subjects = new HashSet<>();
    }


    public Curriculum(@NotNull Semester semester, @NotNull StudentData studentData) {
        this.semester = semester;
        this.subjects = new HashSet<>();
        this.studentData = studentData;
    }

    public Set<Subject> getSubjects() {
        return Collections.unmodifiableSet(this.subjects);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Curriculum ID: '").append(Id).append("'. Subjects: {");
        if (this.subjects != null) {
            for (Subject subject : this.subjects) {
                sb.append("'" + subject.toString() + "',");
            }
        }

        sb.append("}.");

        return sb.toString();
    }

    public boolean hasSubject(String subjectName) {
        if (this.subjects == null) {
            return false;
        }

        return this.subjects.stream().anyMatch(subject -> subject.getNameAsString().equals(subjectName));
    }

    public Subject getSubject(String subjectName) {
        if (this.subjects == null) {
            return null;
        }

        return this.subjects.stream().filter(subject -> subject.getNameAsString().equals(subjectName)).findFirst().orElse(null);
    }

    public void setUser(User user) {
        this.studentData = user.getStudentData();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Curriculum that = (Curriculum) o;
        return Objects.equals(Id, that.Id) && Objects.equals(semester, that.semester) && Objects.equals(subjects, that.subjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, semester, subjects);
    }
}
