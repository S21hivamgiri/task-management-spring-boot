package com.example.taskflow.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "devs")
@Getter
@Setter
@NoArgsConstructor
public class Dev {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String email;
    
    // Links this Dev to the Keycloak user record. This is Keycloak's
    // 'sub' claim — used to resolve "who is making this request" from
    // an incoming JWT, since the app itself never handles passwords.
    @Column(nullable = false, unique = true)
    private String keycloakId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "dev")
    private List<Task> tasks = new ArrayList<>();

}