package com.emzio.todo_api.model;

import java.util.List;
import java.util.Optional;

public interface TaskGroupsRepository {

    List<TaskGroups> findAll();

    Optional<TaskGroups> findById(Integer integer);

    TaskGroups save(TaskGroups taskGroups);

    boolean existsByDoneIsFalseAndProjectId(int projectId);

    boolean existsByDescription(String description);
}
