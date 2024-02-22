package dev.madwey.api.contoller;

import dev.madwey.api.dto.TaskStateDto;
import dev.madwey.api.service.TaskStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TaskStateController {

    private final TaskStateService taskStateService;

    private static final String GET_TASK_STATES = "/api/projects/{project_id}/task-states";
    private static final String CREATE_TASK_STATE = "/api/projects/{project_id}/task-states";

    @GetMapping(GET_TASK_STATES)
    public List<TaskStateDto> getTaskStates(@PathVariable("project_id") Long projectId) {

        return taskStateService.getTaskStates(projectId);
    }

    @PostMapping(CREATE_TASK_STATE)
    public TaskStateDto createTaskState(
            @PathVariable("project_id") Long projectId,
            @RequestParam(name = "task_state_name") String taskStateName) {

        return taskStateService.createTaskState(projectId, taskStateName);
    }

}
