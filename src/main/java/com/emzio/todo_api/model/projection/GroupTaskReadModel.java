package com.emzio.todo_api.model.projection;

import com.emzio.todo_api.model.Task;

import javax.validation.constraints.NotBlank;

public class GroupTaskReadModel {
    @NotBlank(message = "Task description must not be empty")
    private String description;
    private boolean done;

    public GroupTaskReadModel(Task source) {
        description = source.getDescription();
        done = source.isDone();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
