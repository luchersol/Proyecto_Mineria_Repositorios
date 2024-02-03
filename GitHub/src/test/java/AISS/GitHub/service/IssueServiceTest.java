package AISS.GitHub.service;

import AISS.GitHub.model.Issue.Issue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class IssueServiceTest {

    @Autowired
    IssueService service;

    @Test
    @DisplayName("Get all issues")
    void findAll() {
        List<Issue> issues = service.getIssues("spring-projects", "spring-framework", 20, 2);
        issues.forEach(issue -> System.out.println(issue));
    }
}