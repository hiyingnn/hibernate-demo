package com.example.hibernate.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Grade {
  @Id
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private Long gradeId;
  private String studentName;
  private Long studentId;
  private String subject;
  private String score;

}
