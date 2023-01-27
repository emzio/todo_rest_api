package com.emzio.todo_api.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task extends TaskAuditable{
    private LocalDateTime deadline;
    @ManyToOne()
    @JoinColumn(name = "TASK_GROUPS_ID")
    private TaskGroups group;

    public Task() {
    }

    public Task(String description, LocalDateTime deadline) {
        this.description = description;
        this.deadline = deadline;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public TaskGroups getGroup() {
        return group;
    }

    public void setGroup(TaskGroups group) {
        this.group = group;
    }

    public void updateFrom(final Task source){
        description = source.description;
        done = source.done;
        deadline = source.deadline;
//        group = source.group;
    }

}
