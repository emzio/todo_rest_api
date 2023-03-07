package com.emzio.todo_api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.emzio.todo_api.model.Task;
import com.emzio.todo_api.model.TaskRepository;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/tasks")
class TaskController {
    private final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository taskRepository;

    TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    @GetMapping(params = {"!size","!page", "!sort"})
    ResponseEntity<?> readAllTasks(){
        logger.warn("Exposing all Tasks!");
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @GetMapping
    ResponseEntity<List<Task>> readAllTasks(Pageable page){
        logger.warn("Exposing all Tasks!");
        return ResponseEntity.ok(taskRepository.findAll(page).getContent());
    }

    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state){
        return ResponseEntity.ok(
                taskRepository.findByDoneIs(state)
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate){
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.findById(id)
                .ifPresent(task -> {
                    task.updateFrom(toUpdate);
                    taskRepository.save(task);
                });
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id){
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.findById(id)
                .ifPresent(task ->
                    task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id){
        return taskRepository.findById(id)
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
}

    @PostMapping
    ResponseEntity<Task> createTask(@RequestBody @Valid Task task){
        Task resultTask = taskRepository.save(task);
        return ResponseEntity.created(URI.create("/" + resultTask.getId())).body(resultTask);
    }

    @GetMapping("search/today")
    ResponseEntity<List<Task>> readTasksForToday(){
        LocalDateTime tomorrow = LocalDate.now().atStartOfDay().plusDays(1);
        List<Task> result = taskRepository.findAllByDoneIsFalseAndDeadlineIsBefore(tomorrow);
        return ResponseEntity.ok(result);
    }
}
