package com.emzio.todo_api.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    private String description;
    @OneToMany(mappedBy = "project")
    private Set<TaskGroups> groups;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Set<ProjectStep> steps;

    public Project() {
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    Set<TaskGroups> getGroups() {
        return groups;
    }

    void setGroups(Set<TaskGroups> groups) {
        this.groups = groups;
    }

    public Set<ProjectStep> getSteps() {
        return steps;
    }

    void setSteps(Set<ProjectStep> steps) {
        this.steps = steps;
    }
}
