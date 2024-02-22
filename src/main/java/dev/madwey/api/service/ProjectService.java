package dev.madwey.api.service;

import dev.madwey.api.contoller.ControllerHelper;
import dev.madwey.api.dto.AckDto;
import dev.madwey.api.dto.ProjectDto;
import dev.madwey.api.exception.BadRequestException;
import dev.madwey.api.exception.NotFoundException;
import dev.madwey.api.factories.ProjectDtoFactory;
import dev.madwey.store.entity.ProjectEntity;
import dev.madwey.store.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final ControllerHelper controllerHelper;

    private final ProjectDtoFactory projectDtoFactory;

    @Transactional
    public List<ProjectDto> fetchProjects(Optional<String> optionalPrefixName) {

        optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.isBlank());

        Stream<ProjectEntity> projectStream = optionalPrefixName
                .map(projectRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(projectRepository::streamAllBy);

        return projectStream
                .map(projectDtoFactory::makeProjectDto)
                .collect(Collectors.toList());

    }

    @Transactional
    public ProjectDto createOrUpdateProject(Optional<Long> optionalProjectId,
                                            Optional<String> optionalProjectName) {

        optionalProjectName = optionalProjectName.filter(projectName -> !projectName.isBlank());

        boolean isCreate = optionalProjectId.isEmpty();

        if (isCreate && optionalProjectName.isEmpty()) {
            throw new BadRequestException("Project name can't be empty.");
        }

        final ProjectEntity project = optionalProjectId
                .map(controllerHelper::getProjectOrThrowException)
                .orElseGet(() -> ProjectEntity.builder().build());

        optionalProjectName
                .ifPresent(projectName -> {

                    projectRepository
                            .findByName(projectName)
                            .ifPresent(anotherProject -> {
                                throw new BadRequestException(String.format("Project %s already exists.", projectName));
                            });

                    project.setName(projectName);
                });

        final ProjectEntity savedProject = projectRepository.saveAndFlush(project);

        return projectDtoFactory.makeProjectDto(project);

    }


    @Transactional
    public AckDto deleteProject(Long projectId) {

        controllerHelper.getProjectOrThrowException(projectId);

        projectRepository.deleteById(projectId);

        return AckDto.makeDefault(true);

    }
}
