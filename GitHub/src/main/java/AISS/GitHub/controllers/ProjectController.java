package AISS.GitHub.controllers;


import AISS.GitHub.model.Project.Project;
import AISS.GitHub.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/github")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/{owner}/{repo}")
    public Project getProject(@PathVariable String owner,
                              @PathVariable String repo,
                              @RequestParam(defaultValue = "2") Integer sinceCommits,
                              @RequestParam(defaultValue = "20") Integer sinceIssues,
                              @RequestParam(defaultValue = "2") Integer maxPages){
        return projectService.getProject(owner, repo, sinceCommits, sinceIssues, maxPages);
    }

    @PostMapping("/{owner}/{repo}")
    @ResponseStatus(HttpStatus.CREATED)
    public Project postProject(@PathVariable String owner,
                            @PathVariable String repo,
                            @RequestParam(defaultValue = "2") Integer sinceCommits,
                            @RequestParam(defaultValue = "20")  Integer sinceIssues,
                            @RequestParam(defaultValue = "2") Integer maxPages){
        Project project = projectService.getProject(owner, repo, sinceCommits, sinceIssues, maxPages);
        String uri = "http://localhost:8080/gitminer/projects";
        return restTemplate.postForObject(uri, project, Project.class);
    }
}
