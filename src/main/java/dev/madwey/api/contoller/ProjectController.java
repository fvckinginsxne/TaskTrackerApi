package dev.madwey.api.contoller;

import dev.madwey.api.dto.AckDto;
import dev.madwey.api.dto.ProjectDto;
import dev.madwey.api.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class ProjectController {

    private final ProjectService projectService;

    private static final String FETCH_PROJECTS = "/api/projects";
    private static final String CREATE_OR_UPDATE_PROJECT = "/api/projects";
    private static final String DELETE_PROJECT = "/api/projects/{project_id}";

    @GetMapping(FETCH_PROJECTS)
    public List<ProjectDto> fetchProjects(
            @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName) {

        return projectService.fetchProjects(optionalPrefixName);
    }

    @PutMapping(CREATE_OR_UPDATE_PROJECT)
    public ProjectDto createOrUpdateProject(
            @RequestParam(value = "project_id", required = false) Optional<Long> optionalProjectId,
            @RequestParam(value = "project_name", required = false) Optional<String> optionalProjectName
    ) {

        return projectService.createOrUpdateProject(optionalProjectId, optionalProjectName);
    }

    @DeleteMapping(DELETE_PROJECT)
    public AckDto deleteProject(@PathVariable("project_id") Long projectId) {

        return projectService.deleteProject(projectId);
    }


}
