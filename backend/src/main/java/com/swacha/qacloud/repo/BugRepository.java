package com.swacha.qacloud.repo;

import com.swacha.qacloud.domain.Bug;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BugRepository extends JpaRepository<Bug, Long> {
    List<Bug> findByProjectIdOrderByCreatedAtDesc(Long projectId);
}
