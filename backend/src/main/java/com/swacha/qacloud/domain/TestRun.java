package com.swacha.qacloud.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*; import lombok.Getter; import lombok.NoArgsConstructor; import lombok.Setter;
import java.time.Instant; import java.util.ArrayList; import java.util.List;
@Entity @Table(name="test_runs") @Getter @Setter @NoArgsConstructor
public class TestRun {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @JsonIgnore
 @ManyToOne(fetch=FetchType.LAZY, optional=false) private Project project;
 @Column(nullable=false) private Instant startedAt;
 private Instant completedAt;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private TestStatus status=TestStatus.QUEUED;
 private String commitHash;
 @JsonIgnore
 @OneToMany(mappedBy="run", cascade=CascadeType.ALL, orphanRemoval=true) private List<TestResult> results=new ArrayList<>();
}
