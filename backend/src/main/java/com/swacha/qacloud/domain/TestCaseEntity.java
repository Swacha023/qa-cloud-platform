package com.swacha.qacloud.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*; import lombok.Getter; import lombok.NoArgsConstructor; import lombok.Setter;
import java.time.Instant;
@Entity @Table(name="test_cases") @Getter @Setter @NoArgsConstructor
public class TestCaseEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private String title;
 @Column(length=5000) private String description;
 @Column(length=5000) private String preconditions;
 @Column(length=8000) private String steps;
 @Column(length=5000) private String expectedResult;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private Priority priority=Priority.MEDIUM;
 @Column(nullable=false) private Instant createdAt;
 @JsonIgnore
 @ManyToOne(fetch=FetchType.LAZY, optional=false) private TestSuite suite;
}
