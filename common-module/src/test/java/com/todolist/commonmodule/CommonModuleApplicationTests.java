package com.todolist.commonmodule;

import com.todolist.commonmodule.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
@ExtendWith(SpringExtension.class)
@SpringBootTest
class CommonModuleApplicationTests {

    @MockBean
    private UserRepository userRepository;
    @Test
    void contextLoads() {
    }

}
