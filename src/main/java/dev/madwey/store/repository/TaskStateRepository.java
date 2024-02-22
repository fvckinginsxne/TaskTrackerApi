package dev.madwey.store.repository;

import dev.madwey.store.entity.TaskStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long> {

    Optional<TaskStateEntity> findTaskStateEntityByRightTaskStateEntityIdIsNullAndProjectId(Long projectId);
}
