package GitLabMiner.restGitLab.service;
import GitLabMiner.restGitLab.models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class ProjectService {

    @Autowired
    RestTemplate restTemplate;
    public List<Project> getProjects(Integer pages){
        String  baseUri ="https://gitlab.com/api/v4/projects";
        List<Project> res = new ArrayList<>();
        for(int i = 1 ; i <= pages ; i++){
           List<Project> projects = Arrays.stream(restTemplate.getForObject(baseUri+"?page="+i,Project[].class)).toList();
           res.addAll(projects);
        }
        return res;
    }
    public Project getProjectById(String projectId){
        String  baseUri ="https://gitlab.com/api/v4/projects";
        Project project = restTemplate.getForObject(baseUri+"/"+projectId,Project.class);
        return project;
    }
}
