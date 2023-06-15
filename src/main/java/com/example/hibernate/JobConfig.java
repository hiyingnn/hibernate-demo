package com.example.hibernate;

import com.example.hibernate.model.Student;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@Slf4j
public class JobConfig {
  /**
   * Note the JobRepository is typically autowired in and not needed to be explicitly
   * configured
   */
  @Bean
  @Qualifier("job")
  public Job job(JobRepository jobRepository, Step sampleStep) {
    return new JobBuilder("job", jobRepository)
      .start(sampleStep)
      .build();
  }

  /**
   * Note the TransactionManager is typically autowired in and not needed to be explicitly
   * configured
   */
  @Bean
  public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager, JpaPagingItemReader itemReader) {
    return new StepBuilder("sampleStep", jobRepository)
      .<Student, Student>chunk(10, transactionManager)
      .reader(itemReader)
      .processor((ItemProcessor<Student, Student>) student -> {
       log.info(student.toString());
        return student;
      })
      .writer((ItemWriter<Student>) chunk -> {
        // do nothing
      }).build();
  }

  @Bean
  public JpaPagingItemReader<Student> itemReader(EntityManagerFactory entityManagerFactory) {
    return new JpaPagingItemReaderBuilder<Student>()
      .name("studentReader")
      .entityManagerFactory(entityManagerFactory)
      .queryString("select s from Student s  " +
        "join Department d on d.departmentId = s.departmentId " +
        "order by s.studentId")
      .pageSize(5)
      .build();
  }
}
