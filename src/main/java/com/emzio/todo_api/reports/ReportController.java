package com.emzio.todo_api.reports;

import com.emzio.todo_api.model.Task;
import com.emzio.todo_api.model.TaskRepository;
import com.emzio.todo_api.model.event.TaskDone;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {
    private final TaskRepository taskRepository;
    private final PersistedEventTaskRepository eventRepository;

    public ReportController(TaskRepository taskRepository, PersistedEventTaskRepository eventRepository) {
        this.taskRepository = taskRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping("/count/{id}")
    ResponseEntity<TaskWithChangesCount> count(@PathVariable int id){
        return taskRepository.findById(id)
                .map(task -> new TaskWithChangesCount(task, eventRepository.findByTaskId(id)))
                .map(body -> ResponseEntity.ok(body))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/doneBeforeDeadline/{id}")
    ResponseEntity<TaskDoneBeforeDeadline> find(@PathVariable int id){
        return taskRepository.findById(id)
                .map(task -> new TaskDoneBeforeDeadline(task, eventRepository.findByTaskId(id)))
                .map(body -> ResponseEntity.ok(body))
                .orElse(ResponseEntity.notFound().build());
    }

    private static class TaskWithChangesCount {
        public String description;
        public boolean done;
        public int changesCount;

        TaskWithChangesCount(Task task, List<PersistedTaskEvents> events) {
            description = task.getDescription();
            done = task.isDone();
            changesCount = events.size();
        }
    }

    private static class TaskDoneBeforeDeadline {
        public String description;
        public boolean doneBeforeDeadline;
        public boolean done;
        public TaskDoneBeforeDeadline(Task task, List<PersistedTaskEvents> events) {
            description = task.getDescription();
            done = task.isDone();
            if (task.getDeadline() == null) {
//                doneBeforeDeadline = false;
                doneBeforeDeadline = task.isDone();
            } else {
//                doneBeforeDeadline = events.stream()
//                        .filter(event -> event.occurrence.isBefore(task.getDeadline()))
//                        .anyMatch(event -> event.name.equals(TaskDone.class.getSimpleName()));
                doneBeforeDeadline = events.stream()
                        .filter(event -> event.occurrence.isBefore(task.getDeadline()))
                        .max((e1, e2) -> e1.occurrence.compareTo(e2.occurrence))
                        .map(event -> event.name.equals(TaskDone.class.getSimpleName()))
                        .orElse(false);

            }
        }
    }
}
