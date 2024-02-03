package AISS.GitHub.service;

import AISS.GitHub.model.Commit.Commit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommitServiceTest {

    @Autowired
    CommitService service;

    @Test
    @DisplayName("Get all commit")
    void getCommits() {
        List<Commit> c= service.getCommits("spring-projects", "spring-framework", 2, 2);
        assertTrue(!c.isEmpty(), "The list of commits is empty");
        c.forEach(e -> System.out.println(c.indexOf(e) + ": " + e));
    }

}