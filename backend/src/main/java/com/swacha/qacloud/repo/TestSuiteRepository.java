package com.swacha.qacloud.repo;

import com.swacha.qacloud.domain.TestSuite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TestSuiteRepository extends JpaRepository<TestSuite, Long> {
    List<TestSuite> findByProjectId(Long projectId);
}
