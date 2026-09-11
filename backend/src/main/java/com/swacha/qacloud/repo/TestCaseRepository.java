package com.swacha.qacloud.repo;

import com.swacha.qacloud.domain.TestCaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TestCaseRepository extends JpaRepository<TestCaseEntity, Long> {
    List<TestCaseEntity> findBySuiteId(Long suiteId);
    @Query("select c from TestCaseEntity c where c.suite.project.id = :projectId order by c.id")
    List<TestCaseEntity> findByProjectId(@Param("projectId") Long projectId);
}
