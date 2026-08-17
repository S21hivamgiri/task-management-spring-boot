package com.example.taskflow.Controller;

import java.util.UUID;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import com.example.taskflow.Model.Project;
import com.example.taskflow.Model.Task;
import com.example.taskflow.Repository.ProjectRepository;
import com.example.taskflow.Repository.TaskRepository;

@Controller
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public ProjectController(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository =taskRepository;
    }

    @QueryMapping
    public Project project(@Argument UUID id) {
        return projectRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<Project> projects() {
        return projectRepository.findAll();
    }

    @QueryMapping
    public List<Task> tasksByStatus(@Argument Task.TaskStatus status) {
        return taskRepository.findAll().stream()
                .filter(t -> t.getStatus() == status)
                .toList();
    }

    @MutationMapping
    public Project createProject(@Argument String name, @Argument String description) {
        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        return projectRepository.save(project);
    }

    @MutationMapping
    public Boolean deleteProject(@Argument UUID id) {
        if (!projectRepository.existsById(id)) {
            throw new IllegalArgumentException("Project not found: " + id);
        }
        projectRepository.deleteById(id);
        return true;
    }
}
