package com.emzio.todo_api.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.emzio.todo_api.model.Task;
import com.emzio.todo_api.model.TaskRepository;

import java.util.List;

@Repository
interface SQLTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {

//    @Override
//    @Query(nativeQuery = true, value = "select count(*) > 0 from TASKS where id=?1")
//    boolean existsById(Integer id);

    @Override
    @Query(nativeQuery = true, value = "select count(*) > 0 from TASKS where id=:id")
    boolean existsById(@Param("id") Integer id);

    @Override
    boolean existsByDoneIsFalseAndGroupId(Integer groupId);

    @Override
    List<Task> findAllByGroup_Id(int groupId);
}
