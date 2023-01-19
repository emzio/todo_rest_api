package com.emzio.todo_api.model.projection;

import com.emzio.todo_api.model.Task;

import java.time.LocalDateTime;

public class GroupTaskWriteModel {
    private LocalDateTime deadline;
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

    public Task toTask(){
        return new Task(description, deadline);
    }
}
