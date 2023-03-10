package com.emzio.todo_api.model.projection;

import com.emzio.todo_api.model.TaskGroups;

import java.util.Set;
import java.util.stream.Collectors;

public class GroupWriteModel {
    private String description;
    private Set<GroupTaskWriteModel> tasks;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<GroupTaskWriteModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<GroupTaskWriteModel> tasks) {
        this.tasks = tasks;
    }

    public TaskGroups toGroup(){
        TaskGroups result = new TaskGroups();
        result.setDescription(description);

        /*
        Moje stare Rozwiązanie:
        result.setTasks(tasks.stream()
                .map(groupTaskWriteModel -> new Task(groupTaskWriteModel.getDescription(), groupTaskWriteModel.getDeadline()))
                .collect(Collectors.toSet()));
        */

        result.setTasks(
                tasks.stream()
                        .map(GroupTaskWriteModel::toTask)
                        .collect(Collectors.toSet())
        );
        return result;
    }
}
