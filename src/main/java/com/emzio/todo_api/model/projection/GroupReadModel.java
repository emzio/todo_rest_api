package com.emzio.todo_api.model.projection;

import com.emzio.todo_api.model.TaskGroups;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupReadModel {
    private int id;
    @NotBlank(message = "Group description must not be empty")
    private String description;
    private LocalDateTime deadline;
    private Set<GroupTaskReadModel> tasks;

    public GroupReadModel(TaskGroups source) {
        id = source.getId();
        description = source.getDescription();
        source.getTasks().stream()
                .map(task -> task.getDeadline())
                .filter(Objects::nonNull)
                .max((localDateTime, t1) -> localDateTime.compareTo(t1))
                .ifPresent(localDateTime -> deadline = localDateTime);
        tasks = source.getTasks().stream()
                .map(task -> new GroupTaskReadModel(task))
                .collect(Collectors.toSet());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Set<GroupTaskReadModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<GroupTaskReadModel> tasks) {
        this.tasks = tasks;
    }
}
