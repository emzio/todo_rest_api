package com.emzio.todo_api.controller;

import com.emzio.todo_api.logic.TaskGroupService;
import com.emzio.todo_api.model.Task;
import com.emzio.todo_api.model.TaskRepository;
import com.emzio.todo_api.model.projection.GroupReadModel;
import com.emzio.todo_api.model.projection.GroupTaskWriteModel;
import com.emzio.todo_api.model.projection.GroupWriteModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/groups")
public class TaskGroupController {

    private final TaskGroupService service;
    private final TaskRepository repository;

    public TaskGroupController(TaskGroupService service, TaskRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel sourceGroup){
        GroupReadModel result = service.create(sourceGroup);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<List<GroupReadModel>> readAllGroups(){
        return ResponseEntity.ok(service.readAll());
    }

    @Transactional
    @ResponseBody
    @PatchMapping( path = "{id}")
    ResponseEntity<String> toggleGroup(@PathVariable int id){
        service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{id}/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<List<Task>> tasksForGroup(@PathVariable int id){
        return ResponseEntity.ok(repository.findAllByGroup_Id(id));
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
//    @GetMapping()
    String showGroups(Model model){
        List<GroupReadModel> groupReadModels = service.readAll();
        model.addAttribute("groups", groupReadModels);
        GroupWriteModel groupWriteModel = new GroupWriteModel();
        GroupTaskWriteModel groupTaskWriteModel = new GroupTaskWriteModel();
        groupTaskWriteModel.setDeadline(LocalDateTime.now());
        groupTaskWriteModel.setDescription("foo");
        groupWriteModel.setTasks(Set.of(groupTaskWriteModel));
        model.addAttribute("groupWrite", groupWriteModel);
        return "/groups";
    }

    @PostMapping(params = {"addTask"}, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    String addTaskToGroups(Model model){
//        List<GroupReadModel> groupReadModels = service.readAll();
//        model.addAttribute("groups", groupReadModels);
//        GroupWriteModel groupWriteModel = new GroupWriteModel();
//        GroupTaskWriteModel groupTaskWriteModel = new GroupTaskWriteModel();
//        groupTaskWriteModel.setDeadline(LocalDateTime.now());
//        groupTaskWriteModel.setDescription("foo");
//        groupWriteModel.setTasks(Set.of(groupTaskWriteModel));
//        model.addAttribute("groupWrite", groupWriteModel);
//        return "/groups";
        return "addTask param Found";
    }


    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalState(IllegalStateException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
