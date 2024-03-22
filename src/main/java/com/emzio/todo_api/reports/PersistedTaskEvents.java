package com.emzio.todo_api.reports;

import com.emzio.todo_api.model.event.TaskEvent;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "task_events")
public class PersistedTaskEvents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    int id;
    int taskId;
    LocalDateTime occurrence;
    String name;

    public PersistedTaskEvents() {
    }

    public PersistedTaskEvents(TaskEvent source) {
        taskId=source.getTaskId();
        name = source.getClass().getSimpleName();
        occurrence = LocalDateTime.ofInstant(source.getInstant(), ZoneId.systemDefault());
    }
}
