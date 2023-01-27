package com.emzio.todo_api.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.emzio.todo_api.model.Project;
import com.emzio.todo_api.model.ProjectRepository;

import java.util.List;

@Repository
public interface SQLProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {

    @Override
    @Query("select distinct p from Project p join fetch p.steps")
    List<Project> findAll();

}
