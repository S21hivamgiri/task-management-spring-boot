package com.example.taskflow.Controller;

import com.example.taskflow.Model.Dev;
import com.example.taskflow.Model.Project;
import com.example.taskflow.Repository.DevRepository;
import com.example.taskflow.Repository.ProjectRepository;
import com.example.taskflow.Service.KeycloakAdminService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
public class DevController {

    private final DevRepository devRepository;
    private final ProjectRepository projectRepository;
    private final KeycloakAdminService keycloakAdminService;

    public DevController(DevRepository devRepository,
            ProjectRepository projectRepository,
            KeycloakAdminService keycloakAdminService) {
        this.devRepository = devRepository;
        this.projectRepository = projectRepository;
        this.keycloakAdminService = keycloakAdminService;
    }

    @QueryMapping
    public List<Dev> devs() {
        return devRepository.findAll();
    }

    @QueryMapping
    public Dev devById(@Argument UUID id) {
        UUID nonNullId = Objects.requireNonNull(id, "id must not be null");
        return devRepository.findById(nonNullId)
            .orElseThrow(() -> new IllegalArgumentException("Dev not found: " + nonNullId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    @Transactional
    public Dev createDev(@Argument String name,
            @Argument String email,
            @Argument UUID projectId) {

        // 1. Fail fast: Validate Project exists BEFORE calling external Keycloak API
        UUID nonNullProjectId = Objects.requireNonNull(projectId, "projectId must not be null");
        Project project = projectRepository.findById(nonNullProjectId)
            .orElseThrow(() -> new IllegalArgumentException("Project not found: " + nonNullProjectId));

        // 2. Create Keycloak user
        String keycloakId = keycloakAdminService.createUser(name, email);

        // 3. Persist local Dev record safely
        try {
            Dev dev = new Dev();
            dev.setKeycloakId(keycloakId);
            dev.setName(name);
            dev.setEmail(email);
            dev.setProject(project);

            return devRepository.save(dev);
        } catch (Exception ex) {
            // Optional: delete keycloak user if DB save fails to keep systems in sync
            // keycloakAdminService.deleteUser(keycloakId);
            throw new RuntimeException("Failed to persist developer to database: " + ex.getMessage(), ex);
        }
    }
}