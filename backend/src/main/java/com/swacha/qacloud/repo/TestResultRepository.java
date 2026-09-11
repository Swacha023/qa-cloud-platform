package com.swacha.qacloud.repo;

import com.swacha.qacloud.domain.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findByRunId(Long runId);
}
