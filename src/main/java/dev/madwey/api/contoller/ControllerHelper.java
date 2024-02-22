package dev.madwey.api.contoller;

import dev.madwey.api.exception.NotFoundException;
import dev.madwey.store.entity.ProjectEntity;
import dev.madwey.store.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ControllerHelper {

    private final ProjectRepository projectRepository;

    public ProjectEntity getProjectOrThrowException(Long projectId) {

        return projectRepository
                .findById(projectId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(
                                "Project with id %s doesn't exists.",
                                projectId
                        )
                ));
    }
}
