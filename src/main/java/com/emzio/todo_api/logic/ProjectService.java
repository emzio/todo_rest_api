package com.emzio.todo_api.logic;

import com.emzio.todo_api.TaskConfigurationProperties;
import com.emzio.todo_api.model.*;
import com.emzio.todo_api.model.projection.GroupReadModel;
import com.emzio.todo_api.model.projection.GroupTaskWriteModel;
import com.emzio.todo_api.model.projection.GroupWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
    private final ProjectRepository repository;
    private final TaskGroupsRepository taskGroupsRepository;
    private TaskGroupService taskGroupService;
    private final TaskConfigurationProperties configuration;

    public ProjectService(ProjectRepository repository, TaskGroupsRepository taskGroupsRepository, TaskGroupService taskGroupService, TaskConfigurationProperties configuration) {
        this.repository = repository;
        this.taskGroupsRepository = taskGroupsRepository;
        this.taskGroupService = taskGroupService;
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
        return repository.findById(projectId)
                .map(project -> {
                    GroupWriteModel targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps().stream()
                                .map(projectStep ->{
                                    GroupTaskWriteModel task = new GroupTaskWriteModel();
                                    task.setDescription(projectStep.getDescription());
                                    task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                    return task;
                                }).collect(Collectors.toSet())
                    );
                    return taskGroupService.create(targetGroup);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));

//        TaskGroups result = repository.findById(projectId)
//                .map(project -> {
//                    TaskGroups targetGroup = new TaskGroups();
//                    targetGroup.setDescription(project.getDescription());
//                    targetGroup.setTasks(project.getSteps().stream()
//                            .map(projectStep ->
//                                    new Task(projectStep.getDescription(), deadline.plusDays(projectStep.getDaysToDeadline()))
//                            ).collect(Collectors.toSet()));
//                    targetGroup.setProject(project);
//                    return taskGroupsRepository.save(targetGroup);
//                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
//        return new GroupReadModel(result);
    }

}
