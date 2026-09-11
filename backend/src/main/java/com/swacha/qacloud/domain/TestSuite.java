package com.swacha.qacloud.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*; import lombok.Getter; import lombok.NoArgsConstructor; import lombok.Setter;
import java.util.ArrayList; import java.util.List;
@Entity @Table(name="test_suites") @Getter @Setter @NoArgsConstructor
public class TestSuite {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private String name;
 @Column(length=2000) private String description;
 @JsonIgnore
 @ManyToOne(fetch=FetchType.LAZY, optional=false) private Project project;
 @JsonIgnore
 @OneToMany(mappedBy="suite", cascade=CascadeType.ALL, orphanRemoval=true) private List<TestCaseEntity> testCases=new ArrayList<>();
}
