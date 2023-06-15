package com.example.hibernate.startup;

import com.example.hibernate.model.Department;
import com.example.hibernate.model.Grade;
import com.example.hibernate.model.Student;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class ApplicationReadyEventListener {

  @Autowired
  private  EntityManager entityManager;

  @Autowired
  private JobLauncher jobLauncher;

  @Autowired
  private Job job;

  @EventListener(ApplicationStartedEvent.class)
  @Transactional
  public void initDatabase() {

    // generate 5 classes
    for(int i = 0; i < 5; i++) {
     Department department = Department
       .builder()
       .departmentName(String.format("%s-name",i))
       .build();
      entityManager.persist(department);
    }

    // generate 10 students
    for(int i = 0; i < 10; i++) {
      Set<Grade> grades = new HashSet<>();
      for(int j = 0; j < 5; j++) {
        Grade grade = Grade.builder()
          .studentName(String.format("%s-name",i))
          .score(String.format("%s-score",j))
          .subject(String.format("%s-subject",j))
          .build();
        grades.add(grade);
        entityManager.persist(grade);
      }
      Student student = Student.builder()
        .studentName(String.format("%s-name",i))
        .grades(grades)
        .departmentId(Integer.toUnsignedLong(i%2+ 1))
        .build();
      log.info(student.toString());
      entityManager.persist(student);
    }
    entityManager.flush();

  }

  @EventListener(ApplicationReadyEvent.class)
  public void startJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
    jobLauncher.run(job, new JobParameters());
  }
}
