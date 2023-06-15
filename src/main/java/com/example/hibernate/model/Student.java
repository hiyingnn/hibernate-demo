package com.example.hibernate.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NaturalId;

import java.util.List;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Student {
  @Id
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long studentId;

  private String studentName;

  @OneToMany(fetch = FetchType.EAGER)
//  @Fetch(FetchMode.SUBSELECT)
//  @JoinColumn(referencedColumnName = "studentId", name = "studentId")
  @JoinColumn(referencedColumnName = "studentName", name = "studentName", insertable = false, updatable = false)
  @OrderColumn(name="gradeId")
  private Set<Grade> grades;

  private Long departmentId;
}
