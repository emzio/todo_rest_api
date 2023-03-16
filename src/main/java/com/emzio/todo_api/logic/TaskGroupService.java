package com.emzio.todo_api.logic;

import com.emzio.todo_api.model.Project;
import com.emzio.todo_api.model.TaskGroups;
import com.emzio.todo_api.model.TaskGroupsRepository;
import com.emzio.todo_api.model.TaskRepository;
import com.emzio.todo_api.model.projection.GroupReadModel;
import com.emzio.todo_api.model.projection.GroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;

public class TaskGroupService {
    private final TaskGroupsRepository repository;
    private final TaskRepository taskRepository;

    public TaskGroupService(TaskGroupsRepository repository, TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }


    public GroupReadModel create(GroupWriteModel source){
        return create(source, null);
    }

    public GroupReadModel create(GroupWriteModel source, Project project) {
        TaskGroups result = repository.save(source.toGroup(project));
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll(){
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(Integer groupId){
        if (taskRepository.existsByDoneIsFalseAndGroupId(groupId)) {
            throw new IllegalStateException("Group has undone tasks. Done all the tasks first.");
        }
        TaskGroups result = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found."));
        result.setDone(!result.isDone());
        repository.save(result);
    }

}
