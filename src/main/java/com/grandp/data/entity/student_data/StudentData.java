package com.grandp.data.entity.student_data;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.grandp.data.entity.curriculum.Curriculum;
import com.grandp.data.entity.enumerated.Degree;
import com.grandp.data.entity.enumerated.Semester;
import com.grandp.data.entity.subject.Subject;
import com.grandp.data.entity.subjectname.SubjectName;
import com.grandp.data.entity.faculty.Faculty;
import com.grandp.data.entity.user.User;
import com.grandp.data.exception.notfound.runtime.SemesterNotFoundException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashSet;
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
    private Semester semester; //current semester

    @Enumerated
    private Semester takenSemester;

    private String potok;

    private String groupName;

    @OneToMany(mappedBy = "studentData", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Curriculum> curricula;

    @Column(name = "faculty_number")
    private String facultyNumber;

    private Long userId;

    private String iban;

    public StudentData() {

    }

    public StudentData(
      @NotNull User user,
      @NotNull Faculty faculty,
      @NotNull Degree degree,
      @NotNull String facultyNumber,
      @NotNull String potok,
      @NotNull String groupName) {
        this.faculty = faculty;
        this.degree = degree;
        this.semester = Semester.FIRST;
        this.takenSemester = Semester.NONE;
        this.userId = user.getId();
        this.potok = potok;
        this.groupName = groupName;
        user.setStudentData(this);
        this.curricula = new HashSet<>();

        for (Semester s : Semester.values()) {
            curricula.add(new Curriculum(s, this));
        }

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

    @Deprecated
    public Subject getSubject(String subjectName) {
        if (this.curricula == null) {
            throw new IllegalStateException("Student @" + this.facultyNumber + " does not have curricula.");
        }

        for (Curriculum c : this.curricula) {
            if (c.hasSubject(subjectName)) {
                return c.getSubject(subjectName);
            }
        }

        return null;
    }

    public Curriculum getCurriculum(Semester semester) {
        return this.curricula.stream().filter(curriculum -> curriculum.getSemester().equals(semester)).findFirst().orElse(null);
    }

    public Curriculum getCurriculum(String semester) {
        Semester semObj = Semester.of(semester);
        return getCurriculum(semObj);
    }

    public Curriculum getCurriculum(int semester) {
        Semester semObj = Semester.of(semester);
        return getCurriculum(semObj);
    }

    public int getLowGradesNum() {
        int lowGradesCount = -1;

        if (this.curricula == null) {
            return lowGradesCount;
        }

        lowGradesCount = 0;

        for (Curriculum c : curricula) {
            Set<Subject> subjects = c.getSubjects();
            if (subjects == null) {
                continue;
            }

            for (Subject subject : c.getSubjects()) {
                int grade = subject.getGrade();

                if (grade == 1 || grade == 2) {
                    lowGradesCount++;
                }
            }
        }

        return lowGradesCount;
    }

    public double getAverageGrade() {
        double averageGrade = -1;
        int totalSubjects = 0;

        if (this.curricula == null) {
            return averageGrade;
        }

        averageGrade = 0;

        for (Curriculum c : curricula) {
            Set<Subject> subjects = c.getSubjects();
            if (subjects == null) {
                continue;
            }

            for (Subject subject : c.getSubjects()) {
                int grade = subject.getGrade();

                if (grade == 1) { // nepolojen
                    averageGrade += 2;
                } else {
                    averageGrade += grade;
                }

                totalSubjects++;
            }
        }

        averageGrade /= totalSubjects;
        return averageGrade;
    }

    public double getAvgGradeLast2Semesters() {
        int currentSemester = this.semester.getIntValue();
        double result = 0;

        if (currentSemester != 1) {
            double currentSemGrade = getAvgForSem(currentSemester);
            double prevSemGrade = getAvgForSem(currentSemester - 1);
             result = (currentSemGrade + prevSemGrade) / 2;
        } else {
            double currentSemGrade = getAvgForSem(currentSemester);
            result = currentSemGrade;
        }

        return result;
    }

    private double getAvgForSem(int semester) {
        Curriculum curriculum = curricula
          .stream()
          .filter(curriculum1 -> curriculum1.getSemester().getIntValue() == semester)
          .findFirst()
          .orElse(null);

        double avgGrade = 0;
        if (curriculum != null) {
            int numSubjects = curriculum.getSubjects().size();

            if (numSubjects == 0) {
                return 2;
            }

            for (Subject s : curriculum.getSubjects()) {
                int grade = s.getGrade();

                if (grade == 1) {
                    avgGrade += 2;
                } else {
                    avgGrade += grade;
                }
            }

            return avgGrade / numSubjects;
        }

        return 2;
    }

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
