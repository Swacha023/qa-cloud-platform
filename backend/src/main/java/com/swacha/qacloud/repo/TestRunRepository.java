package com.swacha.qacloud.repo;

import com.swacha.qacloud.domain.TestRun;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TestRunRepository extends JpaRepository<TestRun, Long> {
    List<TestRun> findTop10ByProjectIdOrderByStartedAtDesc(Long projectId);
}
