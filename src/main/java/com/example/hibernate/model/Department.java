package com.example.hibernate.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Department {
  @Id
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private Long departmentId;
  @Column(columnDefinition = "VARCHAR2(20)")
  private String departmentName;
}
