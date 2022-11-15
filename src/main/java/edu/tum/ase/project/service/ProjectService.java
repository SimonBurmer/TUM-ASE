package edu.tum.ase.project.service;

import edu.tum.ase.project.model.Project;
import edu.tum.ase.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public Project createProject(Project project) {
        // DONE: implement
        return projectRepository.save(project);
    }

    public Project findByName(String name) {
        // DONE: implement
        return projectRepository.findByName(name);
    }

    public List<Project> getAllProjects() {
        // DONE: implement
        return projectRepository.findAll();
    }
}