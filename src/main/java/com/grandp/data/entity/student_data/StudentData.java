package com.grandp.data.entity.student_data;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.grandp.data.entity.curriculum.Curriculum;
import com.grandp.data.entity.enumerated.Degree;
import com.grandp.data.entity.enumerated.Semester;
import com.grandp.data.entity.subject.Subject;
import com.grandp.data.entity.subjectname.SubjectName;
import com.grandp.data.entity.faculty.Faculty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class StudentData implements SimpleData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    private Faculty faculty;

    @Enumerated(EnumType.STRING)
    private Degree degree;

    @Enumerated(EnumType.STRING)
    private Semester semester;

    @OneToMany(mappedBy = "studentData")
    @JsonManagedReference
    private Set<Curriculum> curricula;

    @Column(name = "faculty_number")
    private String facultyNumber;

    public StudentData() {

    }

    public StudentData(Faculty faculty, Degree degree, Semester semester, Set<Curriculum> curricula, String facultyNumber) {
        this.faculty = faculty;
        this.degree = degree;
        this.semester = semester;
        this.curricula = curricula;
        this.facultyNumber = facultyNumber;
    }

    public boolean hasSubject(SubjectName subjectName) {
        return this.hasSubject(subjectName.getName());
    }

    public boolean hasSubject(String subjectName) {
        if (this.curricula == null) {
            //TODO trace that Student has no curricula

            return false;
        }

        for (Curriculum c : this.curricula) {
            if (c.hasSubject(subjectName)) {
                return true;
            }
        }

        return false;
    }

    public Subject getSubject(String subjectName) {
        if (this.curricula == null) {
            //trace that curricula is null

            return null;
        }

        for (Curriculum c : this.curricula) {
            if (c.hasSubject(subjectName)) {
                return c.getSubject(subjectName);
            }
        }

        return null;
    }

//    public boolean updateSubject(Subject updatedSubject) {
//        if (this.curricula == null) {
//            //TODO trace that Student has no curricula
//
//            return false;
//        }
//
//
//        for (Curriculum c : this.curricula) {
//            if (c.hasSubject(updatedSubject.getNameAsString())) {
//                Subject oldSubject = c.getSubjects().
//                        stream().
//                        filter(subject -> subject.getName().equals(updatedSubject.getName())).
//                        findFirst().
//                        orElse(null); //cannot be null, because we check on previous line
//
//                c.getSubjects().remove(oldSubject);
//                c.getSubjects().add(updatedSubject);
//
//                return true;
//            }
//        }
//
//        return false;
//    }

    @Override
    public String toString() {
        return "StudentData{" +
                "faculty=" + faculty +
                ", degree=" + degree +
                ", semester=" + semester +
                ", curriculums=" + curricula +
                ", facultyNumber='" + facultyNumber + '\'' +
                '}';
    }
}
