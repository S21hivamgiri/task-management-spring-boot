package com.example.taskflow.Controller;

import java.util.UUID;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;
import com.example.taskflow.Model.Dev;
import com.example.taskflow.Repository.DevRepository;
@Controller
public class DevController {
     private final DevRepository devRepository;

    public DevController(DevRepository devRepository) {
        this.devRepository = devRepository;
    }

    @QueryMapping
    public List<Dev> devs() {
        return devRepository.findAll();
    }

    @QueryMapping
    public Dev devById(@Argument UUID id) {
        return devRepository.findById(id).orElse(null);
    }
}
