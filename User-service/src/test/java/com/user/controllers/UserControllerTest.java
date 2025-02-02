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
        // Arrange
        Users loginRequest = new Users();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        Users user = new Users();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole("USER");

        // Mocking
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString())).thenReturn("jwtToken"); // Mock עבור generateToken

        // Act
        ResponseEntity<?> response = userController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwtToken", ((java.util.Map<?, ?>) response.getBody()).get("token"));
    }



    @Test
    @Order(3)
    public void testLoginUser_InvalidCMail() {
        user.copyFromDto(testUser);
        user.setEmail("invalidgmail.com");

        // When
        ResponseEntity<?> response = userController.login(user);

        // Then
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    @Order(4)
    public void testLoginUser_InvalidCPassword() {
        user.copyFromDto(testUser);
        user.setPassword(passwordEncoder.encode("password"));


        // When
        ResponseEntity<?> response = userController.login(user);

        // Then
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    @Order(5)
    public void testDeleteUser() {
        // Given
        Users userToDelete = new Users();  // יצירת משתמש למחיקה
        userToDelete.setId(1L);  // הגדרת מזהה המשתמש
        userToDelete.setEmail("test@example.com");

        // Mocking המחיקה מתוך ה-Repository
        when(userRepository.existsById(userToDelete.getId())).thenReturn(true); // בדוק אם המשתמש קיים
        doNothing().when(userRepository).deleteById(userToDelete.getId()); // בדוק שמבוצעת המחיקה

        // Performing the deletion through the controller
        ResponseEntity<Void> response = userController.deleteUser(userToDelete.getId());

        // Then
        // מאמתים שהתשובה חזרה עם סטטוס 204
        assertEquals(204, response.getStatusCodeValue());

        verify(userRepository, times(1)).deleteById(userToDelete.getId());
    }


    @Test
    @Order(6)
    public void testCreateUserInvalidPhone() {
        // Given
        testUser.setPhone("000000000000");

        when(userRepository.save(any(Users.class))).thenThrow(new IllegalArgumentException("Invalid phone number"));

        // When
        ResponseEntity<Users> response = userController.createUser(testUser);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
    }
    @Test
    @Order(7)
    public void testCreateUserInvalidPassword() {
        // Given
        testUser.setPassword("password");
        when(userRepository.save(any(Users.class))).thenThrow(new IllegalArgumentException("Invalid password"));

        // When
        ResponseEntity<Users> response = userController.createUser(testUser);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
    }

    @Test
    @Order(8)
    public void testCreateUserInvalidEmail() {
        // Given
        testUser.setEmail("testUser.com@"); // דוא"ל לא תקין
        when(userRepository.save(any(Users.class))).thenThrow(new IllegalArgumentException("Invalid email"));

        // When
        ResponseEntity<Users> response = userController.createUser(testUser);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());

    }


}