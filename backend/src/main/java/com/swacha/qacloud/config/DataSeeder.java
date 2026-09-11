package com.swacha.qacloud.config;

import com.swacha.qacloud.domain.*;
import com.swacha.qacloud.repo.*;
import com.swacha.qacloud.user.*;
import org.springframework.boot.CommandLineRunner; import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration; import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.Instant;

@Configuration
public class DataSeeder {
 @Bean CommandLineRunner seed(UserRepository users, ProjectRepository projects, TestSuiteRepository suites, TestCaseRepository cases, BugRepository bugs, PasswordEncoder encoder){
  return args -> {
   UserAccount user=users.findByEmail("demo@qacloud.local").orElseGet(()->{UserAccount u=new UserAccount();u.setEmail("demo@qacloud.local");u.setDisplayName("Demo QA Engineer");u.setPassword(encoder.encode("ChangeMe123!"));u.setRole("QA_ENGINEER");return users.save(u);});
   if(projects.count()==0){Project p=new Project();p.setName("QA Cloud Platform");p.setDescription("Demo project for automated quality engineering.");p.setCreatedAt(Instant.now());p.setCreatedBy(user);p=projects.save(p);
    TestSuite s=new TestSuite();s.setProject(p);s.setName("Authentication");s.setDescription("Login and session test coverage.");s=suites.save(s);
    for(int i=1;i<=8;i++){TestCaseEntity c=new TestCaseEntity();c.setSuite(s);c.setTitle("Login test #"+i);c.setDescription("Verify a login workflow.");c.setSteps("Open login page; enter credentials; submit form");c.setExpectedResult("User reaches dashboard");c.setPriority(i%3==0?Priority.HIGH:Priority.MEDIUM);c.setCreatedAt(Instant.now());cases.save(c);}
    Bug b=new Bug();b.setProject(p);b.setTitle("Expired token displays generic error");b.setDescription("Improve the user-facing message when an access token expires.");b.setSeverity(Severity.MEDIUM);b.setStatus(BugStatus.OPEN);b.setCreatedAt(Instant.now());bugs.save(b);
   }
  };
 }
}
