package GitLabMiner.restGitLab.service;

import GitLabMiner.restGitLab.models.Commit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class CommitService {
    @Autowired
    RestTemplate restTemplate;

    private String baseUri ="https://gitlab.com/api/v4";
    public List<Commit> getCommitsByProject(String projectId,Integer days , Integer pages){
        String  date = LocalDate.now().minusDays(days).toString();
        List<Commit> res = new ArrayList<>();
        for(int i = 1 ; i <= pages ; i++){
            List<Commit> commits = Arrays.stream(restTemplate.getForObject(baseUri+"/projects/"+ projectId + "/repository/commits?page="+i+"&since="+date,Commit[].class)).toList();
            if(commits.isEmpty()) break;
            res.addAll(commits);
        }
        return res;
    }

}
