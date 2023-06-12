package com.grandp.data.entity.curriculum;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.grandp.data.entity.enumerated.Semester;
import com.grandp.data.entity.subject.Subject;
import com.grandp.data.entity.user.User;
import com.grandp.data.entity.student_data.StudentData;
import jakarta.persistence.*;
import lombok.Getter;

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

    @JsonCreator
    public Curriculum(@JsonProperty("semester") String semester, @JsonProperty("user") User user) {
        this.semester = Semester.valueOf(semester);
        this.studentData = user.getUserData();
        this.subjects = new HashSet<>();
    }


    public Curriculum(String semester, Subject... subjects) {
        this.semester = Semester.valueOf(semester);
        this.subjects = new HashSet<>();

        for (Subject subject : subjects) {
            if (subject == null) {
                continue;
            }

            this.subjects.add(subject);
        }
    }

    public Set<Subject> getSubjects() {
        return subjects;
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
            //TODO trace that subjects are null or has no subjects

            return false;
        }

        return this.subjects.stream().anyMatch(subject -> subject.getNameAsString().equals(subjectName));
    }

    public Subject getSubject(String subjectName) {
        if (this.subjects == null) {
            //TODO trace that subjects collection is null

            return null;
        }

        return this.subjects.stream().filter(subject -> subject.getNameAsString().equals(subjectName)).findFirst().orElse(null);
    }

    public void setUser(User user) {
        this.studentData = user.getUserData();
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
