package edu.tum.ase.project.controller;

import edu.tum.ase.project.model.Project;
import edu.tum.ase.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @GetMapping("")
    public List<Project> getAllProject() {
        // DONE: Implement
        return projectService.getAllProjects();
    }

    // DONE: Implement a POST Endpoint to create a project with a given name
    @PostMapping("")
    public Project createProject(@RequestBody String name) {
        return projectService.createProject(new Project(name));
    }

    // DONE: Implement an Endpoint to find a project with a given name
    @GetMapping("{name}")
    public Project getProjectByName(@PathVariable String name) {
        return projectService.findByName(name);
    }

}
