package com.emzio.todo_api.model.projection;

import com.emzio.todo_api.model.Project;
import com.emzio.todo_api.model.TaskGroups;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupWriteModel {
    public GroupWriteModel() {
        tasks.add(new GroupTaskWriteModel());
    }
    @NotBlank(message = "Group description must not be empty")
    private String description;
    @Valid
    private List<GroupTaskWriteModel> tasks = new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<GroupTaskWriteModel> getTasks() {
        return tasks;
    }

    public void setTasks(List<GroupTaskWriteModel> tasks) {
        this.tasks = tasks;
    }

    public TaskGroups toGroup(Project project){
        TaskGroups result = new TaskGroups();
        result.setDescription(description);
        result.setProject(project);
        result.setTasks(
                tasks.stream()
                        .map(source -> source.toTask(result))
                        .collect(Collectors.toSet())
        );
        return result;
    }
}
