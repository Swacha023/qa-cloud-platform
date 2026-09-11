package com.swacha.qacloud.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*; import lombok.Getter; import lombok.NoArgsConstructor; import lombok.Setter;
@Entity @Table(name="test_results") @Getter @Setter @NoArgsConstructor
public class TestResult {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @JsonIgnore
 @ManyToOne(fetch=FetchType.LAZY, optional=false) private TestRun run;
 @JsonIgnore
 @ManyToOne(fetch=FetchType.LAZY, optional=false) private TestCaseEntity testCase;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private TestStatus status;
 private Long durationMs;
 @Column(length=5000) private String errorMessage;
}
