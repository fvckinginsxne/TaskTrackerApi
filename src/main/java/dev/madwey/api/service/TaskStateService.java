package dev.madwey.api.service;

import dev.madwey.api.contoller.ControllerHelper;
import dev.madwey.api.dto.TaskStateDto;
import dev.madwey.api.exception.BadRequestException;
import dev.madwey.api.factories.TaskStateDtoFactory;
import dev.madwey.store.entity.ProjectEntity;
import dev.madwey.store.entity.TaskStateEntity;
import dev.madwey.store.repository.TaskStateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TaskStateService {

    private final TaskStateRepository taskStateRepository;

    private final ControllerHelper controllerHelper;

    private final TaskStateDtoFactory taskStateDtoFactory;

    @Transactional
    public List<TaskStateDto> getTaskStates(Long projectId) {

        ProjectEntity project = controllerHelper.getProjectOrThrowException(projectId);

        return project
                .getTaskStates()
                .stream()
                .map(taskStateDtoFactory::makeTaskStateDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TaskStateDto createTaskState(Long projectId, String taskStateName) {

        if (taskStateName.isBlank()) {
            throw new BadRequestException("Task state name can't be empty.");
        }

        ProjectEntity project = controllerHelper.getProjectOrThrowException(projectId);

        Optional<TaskStateEntity> optionalAnotherTaskState = Optional.empty();

        for (TaskStateEntity taskState: project.getTaskStates()) {

            if (taskState.getName().equalsIgnoreCase(taskStateName)) {
                throw new BadRequestException(String.format("Task state %s already exists", taskStateName));
            }

            if (taskState.getRightTaskState().isEmpty()) {
                optionalAnotherTaskState = Optional.of(taskState);
            }
        }

        TaskStateEntity taskState = taskStateRepository.saveAndFlush(
                TaskStateEntity.builder()
                        .name(taskStateName)
                        .project(project)
                        .build()
        );

        optionalAnotherTaskState
                .ifPresent(anotherTaskState -> {

                    taskState.setLeftTaskStateEntity(anotherTaskState);

                    anotherTaskState.setRightTaskStateEntity(taskState);

                    taskStateRepository.saveAndFlush(anotherTaskState);
                });

        TaskStateEntity persistentTaskState = taskStateRepository.saveAndFlush(taskState);

        return taskStateDtoFactory.makeTaskStateDto(persistentTaskState);

    }

}
