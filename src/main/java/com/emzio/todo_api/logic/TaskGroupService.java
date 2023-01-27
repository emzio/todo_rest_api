package com.emzio.todo_api.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import com.emzio.todo_api.model.TaskGroups;
import com.emzio.todo_api.model.TaskGroupsRepository;
import com.emzio.todo_api.model.TaskRepository;
import com.emzio.todo_api.model.projection.GroupReadModel;
import com.emzio.todo_api.model.projection.GroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class TaskGroupService {
    private final Logger logger = LoggerFactory.getLogger(TaskGroupService.class);
    private final TaskGroupsRepository repository;
    private final TaskRepository taskRepository;

    public TaskGroupService(TaskGroupsRepository repository, TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }


    public GroupReadModel create(GroupWriteModel source){
        TaskGroups result = repository.save(source.toGroup());
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
