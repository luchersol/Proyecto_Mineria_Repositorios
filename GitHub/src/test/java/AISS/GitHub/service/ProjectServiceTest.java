package AISS.GitHub.service;

import AISS.GitHub.model.Project.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProjectServiceTest {

    @Autowired
    ProjectService service;

    @Test
    @DisplayName("Get project by id")
    void getProject() {
        Project c = service.getProject("spring-projects", "spring-framework", 2, 20, 2);
        assertTrue(c!=null, "The project is null");
        System.out.println(c);

    }
}