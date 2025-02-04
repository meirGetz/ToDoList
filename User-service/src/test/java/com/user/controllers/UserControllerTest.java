package com.user.controllers;

import com.DTO.LoginRequest;
import com.DTO.UserDto;
import com.user.auth.Security.SecurityConfig;
import com.user.entities.Users;
import com.user.repositories.UserRepository;
import com.user.auth.Security.JwtUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(SecurityConfig.class)
@SpringBootTest(classes = com.user.UserApplication.class)

public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    private UserDto testUser;

    private Users user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userRepository, passwordEncoder,jwtUtil);
        testUser = new UserDto();
        user = new Users();
        testUser.setId(1L);
        testUser.setUsername("meir");
        testUser.setPhone("0446622349");
        testUser.setEmail("test@example.com");
        testUser.setPassword("Pa1ssword#");
        testUser.setRole("USER");
    }

@Test
@Order(1)
public void testCreateUser() {
    when(passwordEncoder.encode(testUser.getPassword())).thenReturn("encodedPassword");
    when(userRepository.save(any(Users.class))).thenAnswer(invocation -> {
        Users user = invocation.getArgument(0);
        user.setId(1L);
        return user;
    });
    ResponseEntity<Users> response = userController.createUser(testUser);
    user = response.getBody();
    assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    assertNotNull(response.getBody());
    assertEquals("test@example.com", response.getBody().getEmail());
    assertEquals("encodedPassword", response.getBody().getPassword());
    verify(userRepository, times(1)).save(any(Users.class));
}

    @Test
    @Order(2)
    public void testLoginUser_Success() {
        Users loginRequest = new Users();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        Users user = new Users();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole("USER");

        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString())).thenReturn("jwtToken"); // Mock עבור generateToken
        ResponseEntity<?> response = userController.login(loginRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwtToken", ((java.util.Map<?, ?>) response.getBody()).get("token"));
    }



    @Test
    @Order(3)
    public void testLoginUser_InvalidCMail() {
        user.copyFromDto(testUser);
        user.setEmail("invalidgmail.com");
        ResponseEntity<?> response = userController.login(user);
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    @Order(4)
    public void testLoginUser_InvalidCPassword() {
        user.copyFromDto(testUser);
        user.setPassword(passwordEncoder.encode("password"));
        ResponseEntity<?> response = userController.login(user);
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    @Order(5)
    public void testDeleteUser() {
        Users userToDelete = new Users();
        userToDelete.setId(1L);
        userToDelete.setEmail("test@example.com");

        when(userRepository.existsById(userToDelete.getId())).thenReturn(true);
        doNothing().when(userRepository).deleteById(userToDelete.getId());

        ResponseEntity<Void> response = userController.deleteUser(userToDelete.getId());

        assertEquals(204, response.getStatusCodeValue());

        verify(userRepository, times(1)).deleteById(userToDelete.getId());
    }


    @Test
    @Order(6)
    public void testCreateUserInvalidPhone() {
        testUser.setPhone("000000000000");

        when(userRepository.save(any(Users.class))).thenThrow(new IllegalArgumentException("Invalid phone number"));

        ResponseEntity<Users> response = userController.createUser(testUser);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
    }
    @Test
    @Order(7)
    public void testCreateUserInvalidPassword() {
        testUser.setPassword("password");
        when(userRepository.save(any(Users.class))).thenThrow(new IllegalArgumentException("Invalid password"));
        ResponseEntity<Users> response = userController.createUser(testUser);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
    }

    @Test
    @Order(8)
    public void testCreateUserInvalidEmail() {
        testUser.setEmail("testUser.com@");
        when(userRepository.save(any(Users.class))).thenThrow(new IllegalArgumentException("Invalid email"));
        ResponseEntity<Users> response = userController.createUser(testUser);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());

    }


}