package com.swacha.qacloud.domain;

import com.swacha.qacloud.user.UserAccount;
import jakarta.persistence.*; import lombok.Getter; import lombok.NoArgsConstructor; import lombok.Setter;
import java.time.Instant;
@Entity @Table(name="bugs") @Getter @Setter @NoArgsConstructor
public class Bug {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY, optional=false) private Project project;
 @Column(nullable=false) private String title;
 @Column(length=5000) private String description;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private Severity severity=Severity.MEDIUM;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private BugStatus status=BugStatus.OPEN;
 @ManyToOne(fetch=FetchType.LAZY) private UserAccount assignedTo;
 @Column(nullable=false) private Instant createdAt;
 private Instant resolvedAt;
}
