package com.emzio.todo_api.model.event;

import com.emzio.todo_api.model.Task;

import java.time.Clock;
import java.time.Instant;

public abstract class TaskEvent {
    private int taskId;
    private Instant instant;

    public static TaskEvent changed(Task source){
        return source.isDone() ? new TaskDone(source) : new TaskUndone(source);
    }

    TaskEvent(final int taskId, Clock clock) {
        this.taskId = taskId;
        this.instant = Instant.now(clock);
    }

    public int getTaskId() {
        return taskId;
    }

    public Instant getInstant() {
        return instant;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "taskId=" + taskId +
                ", instant=" + instant +
                '}';
    }
}
