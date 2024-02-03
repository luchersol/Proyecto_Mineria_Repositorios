package AISS.GitHub.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService service;
    @Test
    @DisplayName("Get user by username")
    void getUserByUserName() {
        System.out.println(service.getUserByUsername("luchersol"));
    }
}