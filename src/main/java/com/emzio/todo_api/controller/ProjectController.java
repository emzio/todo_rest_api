package com.emzio.todo_api.controller;

import com.emzio.todo_api.logic.ProjectService;
import com.emzio.todo_api.model.Project;
import com.emzio.todo_api.model.ProjectStep;
import com.emzio.todo_api.model.projection.ProjectWriteModel;
import io.micrometer.core.annotation.Timed;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping
    String showProjects(Model model){
        model.addAttribute("project", new ProjectWriteModel());
        return "projects";
    }

    @PostMapping
    String addProject(
            @ModelAttribute ("project") @Valid ProjectWriteModel current,
            BindingResult bindingResult,
            Model model){
        if (!bindingResult.hasErrors()) {
            service.save(current);
            model.addAttribute("project", new ProjectWriteModel());
            model.addAttribute("projects", getProjects());
            model.addAttribute("message", "Dodano projekt");
        }
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current){
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @PostMapping("/{id}")
    @Timed(value = "project.create.group", histogram = true, percentiles = {.5, .95, .99})
    String createGroup(
            @PathVariable int id,
            @ModelAttribute("project") ProjectWriteModel current,
            Model model,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline
    ){
        try {
            service.createGroup(id, deadline);
            model.addAttribute("message", "Dodano grupę!");
        } catch (IllegalArgumentException | IllegalStateException e){
            model.addAttribute("message", e.getMessage());
        }
        return "/projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects(){
        return service.readAll();
    }
}
