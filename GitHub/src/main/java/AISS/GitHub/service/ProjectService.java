package AISS.GitHub.service;

import AISS.GitHub.model.Commit.Commit;
import AISS.GitHub.model.Issue.Issue;
import AISS.GitHub.model.Project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CommitService commitService;

    @Autowired
    IssueService issueService;

    @Value("${token}")
    private String token;

    public Project getProject(String owner, String repo, Integer sinceCommits, Integer sinceIssues, Integer maxPages){
        String uri="https://api.github.com/repos/"+ owner + "/" +repo ;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Project> request = new HttpEntity<>(null, headers);
        ResponseEntity<Project> response = restTemplate.exchange(uri, HttpMethod.GET, request, Project.class);

        Project project = response.getBody();

        List<Commit> commits = commitService.getCommits(owner, repo, sinceCommits, maxPages);
        List<Issue> issues = issueService.getIssues(owner, repo, sinceIssues, maxPages);

        project.setCommits(commits);
        project.setIssues(issues);

        return project;
    }

}
