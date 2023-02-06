package com.emzio.todo_api.model;


import javax.persistence.*;

@Entity
@Table(name = "project_steps")
public class ProjectStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    private String description;
    @Column(name = "days_to_deadline")
    private Long daysToDeadline;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public Long getDaysToDeadline() {
        return daysToDeadline;
    }

    public void setDaysToDeadline(Long daysToDeadline) {
        this.daysToDeadline = daysToDeadline;
    }

    public Project getProject() {
        return project;
    }

    void setProject(Project project) {
        this.project = project;
    }


}
