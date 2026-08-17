package com.example.taskflow.Controller;

import java.util.UUID;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import com.example.taskflow.Model.Project;
import com.example.taskflow.Repository.ProjectRepository;

@Controller
public class ProjectController {
    
    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @QueryMapping
    public Project project(@Argument UUID id) {
        System.out.println(id);
        return projectRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<Project> projects() {
        return projectRepository.findAll();
    }
}
