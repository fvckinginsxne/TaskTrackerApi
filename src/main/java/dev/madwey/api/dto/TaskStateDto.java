package dev.madwey.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskStateDto {

    private Long id;

    private String name;

    private Long leftTaskStateId;

    private Long rightTaskStateId;

    private Instant createdAt;

    private List<TaskDto> tasks;
}
