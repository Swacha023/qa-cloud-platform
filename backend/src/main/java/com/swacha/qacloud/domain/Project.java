package com.swacha.qacloud.domain;

import com.swacha.qacloud.user.UserAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter; import lombok.NoArgsConstructor; import lombok.Setter;
import java.time.Instant; import java.util.ArrayList; import java.util.List;

@Entity @Table(name="projects") @Getter @Setter @NoArgsConstructor
public class Project {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private String name;
 @Column(length=2000) private String description;
 @Column(nullable=false) private Instant createdAt;
 @JsonIgnore
 @ManyToOne(fetch=FetchType.LAZY, optional=false) private UserAccount createdBy;
 @JsonIgnore
 @OneToMany(mappedBy="project", cascade=CascadeType.ALL, orphanRemoval=true) private List<TestSuite> suites=new ArrayList<>();
}
