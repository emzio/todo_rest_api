package com.emzio.todo_api.logic;

import com.emzio.todo_api.TaskConfigurationProperties;
import com.emzio.todo_api.model.*;
import com.emzio.todo_api.model.projection.GroupReadModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//@Service
//@RequestScope
public class ProjectService {
    private final ProjectRepository repository;
    private final TaskGroupsRepository taskGroupsRepository;
    private final TaskConfigurationProperties configuration;

    public ProjectService(ProjectRepository repository, TaskGroupsRepository taskGroupsRepository, TaskConfigurationProperties configuration) {
        this.repository = repository;
        this.taskGroupsRepository = taskGroupsRepository;
        this.configuration = configuration;
    }

    public List<Project> readAll(){
        return repository.findAll();
    }

    public Project save(Project source){
        return repository.save(source);
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime deadline){
        if (!configuration.getTemplate().isAllowMultipleTasks()&&taskGroupsRepository.existsByDoneIsFalseAndProjectId(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        TaskGroups result = repository.findById(projectId).map(project -> {
            TaskGroups targetGroup = new TaskGroups();
            targetGroup.setDescription(project.getDescription());
            targetGroup.setTasks(project.getSteps().stream()
                    .map(projectStep ->
                            new Task(projectStep.getDescription(), deadline.plusDays(projectStep.getDaysToDeadline()))
                    ).collect(Collectors.toSet()));
            targetGroup.setProject(project);
            return taskGroupsRepository.save(targetGroup);
        }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
            return new GroupReadModel(result);
    }

}
