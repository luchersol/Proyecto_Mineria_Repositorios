package GitLabMiner.restGitLab.service;

import GitLabMiner.restGitLab.models.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class IssueService {
    @Autowired
    RestTemplate restTemplate;

    private String baseUri= "https://gitlab.com/api/v4/projects/";
    public List<Issue> getIssues(String projectId, Integer days , Integer pages){
        String  date = LocalDate.now().minusDays(days).toString();
        List<Issue> res = new ArrayList<>();
        for(int i = 1 ; i <= pages ; i++){
            List<Issue> issues = Arrays.stream(restTemplate.getForObject(baseUri+projectId+"/issues?created_after="+date+"&page="+i,Issue[].class)).toList();
            if(issues.isEmpty()) break;
            res.addAll(issues);
        }
        return res;

    }

    /*
    public Issue getIssueById (String projectId, String issueId){
        Issue i = restTemlplate.getForObject(baseUri+projectId+"/issues/"+issueId,Issue.class);
        return i;
    }
    public List<Issue> getIssueByAuthor(String projectId , String userId ,String pages ){
        Integer p = Integer.parseInt(pages);
        List<Issue> res = new ArrayList<>();
        for(int i = 1 ; i <= p ; i++){
            List<Issue>  is = Arrays.stream(restTemlplate.getForObject(baseUri+projectId+"/issues/?author_id="+userId+"&page="+i,Issue[].class)).toList();
            res.addAll(is);
        }
        return res;
    }
    public List<Issue> getIssueByAssignee(String projectId , String userId ,String pages ){
        Integer p = Integer.parseInt(pages);
        List<Issue> res = new ArrayList<>();
        System.out.println(baseUri+projectId+"/issues/?assignee_id="+userId+"&page="+pages);
        for(int i = 1 ; i <= p ; i++){
            List<Issue>  is = Arrays.stream(restTemlplate.getForObject(baseUri+projectId+"/issues/?assignee_id="+userId+"&page="+i,Issue[].class)).toList();
            res.addAll(is);
        }
        return res;
    }
 */
    
}
