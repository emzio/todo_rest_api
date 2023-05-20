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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

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

    ///TODO why intelliJ ask for public method?
    @Transactional
    @ResponseBody
    @PatchMapping( path = "{id}")
    public ResponseEntity<String> toggleGroup(@PathVariable int id){
        service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{id}/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<List<Task>> tasksForGroup(@PathVariable int id){
        return ResponseEntity.ok(repository.findAllByGroup_Id(id));
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    String showGroups(Model model){
        model.addAttribute("groupWrite", new GroupWriteModel());
        return "/groups";
    }

    @PostMapping(params = {"addTask"}, produces = MediaType.TEXT_HTML_VALUE)
    String addTaskToGroups(@ModelAttribute ("groupWrite") GroupWriteModel groupWrite){
        groupWrite.getTasks().add(new GroupTaskWriteModel());
        return "/groups";
    }

    @PostMapping(produces = MediaType.TEXT_HTML_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String addGroup(@ModelAttribute ("groupWrite") @Valid GroupWriteModel groupWrite,
                    BindingResult bindingResult,
                    Model model){
        if (!bindingResult.hasErrors()) {
            GroupReadModel result = service.create(groupWrite);
            model.addAttribute("message", "Dodano Grupę");
            model.addAttribute("groups", getGroups());
            model.addAttribute("groupWrite", new GroupWriteModel());
        }
        return "/groups";
    }

    @ModelAttribute("groups")
    List<GroupReadModel> getGroups(){
        return service.readAll();
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
