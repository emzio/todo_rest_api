package com.emzio.todo_api.model.event;

import com.emzio.todo_api.model.Task;

import java.time.Clock;

public class TaskDone extends TaskEvent {
    TaskDone(Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
