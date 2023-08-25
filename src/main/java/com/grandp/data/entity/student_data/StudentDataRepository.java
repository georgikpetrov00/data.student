package com.grandp.data.entity.student_data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentDataRepository extends JpaRepository<StudentData, Long> {

  public StudentData findByUserId(Long id);

}