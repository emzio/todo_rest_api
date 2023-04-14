package com.emzio.todo_api.model.projection;

import com.emzio.todo_api.model.Task;
import com.emzio.todo_api.model.TaskGroups;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

public class GroupTaskWriteModel {
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime deadline;
    @NotBlank(message = "Task description must not be empty")
    private String description;

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Task toTask(TaskGroups group){
        return new Task(description, deadline, group);
    }
}
