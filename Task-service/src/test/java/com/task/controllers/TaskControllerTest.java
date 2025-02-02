package com.task.controllers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import com.DTO.UserDto;
import com.task.DTO.ListObjectRequest;
import com.task.controllers.TaskActions;
import com.task.entities.ListObject;
import com.task.repositories.TaskRepository;
import com.task.service.AuthService;
import com.task.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskControllerTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskActions taskActions;

    private ListObjectRequest testTaskRequest;
    private ListObject existingTask;
    private String fakeToken;
    private UserDto testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        fakeToken = "Bearer faketoken123";

        testTaskRequest = new ListObjectRequest();
        testTaskRequest.setTitle("Updated Title");
        testTaskRequest.setDescription("Updated Description");
        testTaskRequest.setStatus("Completed");
        testTaskRequest.setStartTime(LocalDateTime.now());

        existingTask = new ListObject();
        existingTask.setId(1L);
        existingTask.setTitle("Original Title");
        existingTask.setDescription("Original Description");
        existingTask.setStatus("Pending");
        existingTask.setUserId(1L); // משתמש בעל id 1

        testUser = new UserDto();
        testUser.setId(1L);
        testUser.setUsername("meir");
        testUser.setPhone("0446622349");
        testUser.setEmail("user@example.com");
        testUser.setPassword("Pa1ssword#");
        testUser.setRole("USER");
    }

    @Test
    @Order(1)
    public void testEditTitle_Success() throws Exception {
        // מדמה שהטוקן תקף והמשתמש מורשה
        when(authService.validateToken(anyString())).thenReturn(true);
        when(userService.getEmailFromToken(anyString())).thenReturn("user@example.com");
        when(userService.getUserByToken(anyString())).thenReturn(testUser);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        ResponseEntity<?> response = taskActions.edit(fakeToken, 1L, "Title", testTaskRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Title", ((ListObject) response.getBody()).getTitle());
    }

    @Test
    @Order(2)
    public void testEditDescription_Success() throws Exception {
        // מדמה שהטוקן תקף והמשתמש מורשה
        when(authService.validateToken(anyString())).thenReturn(true);
        when(userService.getEmailFromToken(anyString())).thenReturn("user@example.com");
        when(userService.getUserByToken(anyString())).thenReturn(testUser);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        ResponseEntity<?> response = taskActions.edit(fakeToken, 1L, "Description", testTaskRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Description", ((ListObject) response.getBody()).getDescription());
    }

    @Test
    @Order(3)
    public void testEditStatus_Success() throws Exception {
        // מדמה שהטוקן תקף והמשתמש מורשה
        when(authService.validateToken(anyString())).thenReturn(true);
        when(userService.getEmailFromToken(anyString())).thenReturn("user@example.com");
        when(userService.getUserByToken(anyString())).thenReturn(testUser);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        ResponseEntity<?> response = taskActions.edit(fakeToken, 1L, "Status", testTaskRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Completed", ((ListObject) response.getBody()).getStatus());
    }

    @Test
    @Order(4)
    public void testEditTime_Success() throws Exception {
        // מדמה שהטוקן תקף והמשתמש מורשה
        when(authService.validateToken(anyString())).thenReturn(true);
        when(userService.getEmailFromToken(anyString())).thenReturn("user@example.com");
        when(userService.getUserByToken(anyString())).thenReturn(testUser);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        testTaskRequest.setEndTime(testTaskRequest.getStartTime(),4,4,4);
        ResponseEntity<?> response = taskActions.edit(fakeToken, 1L, "Time", testTaskRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testTaskRequest.getStartTime(), ((ListObject) response.getBody()).getStartTime());
    }

    @Test
    @Order(5)
    public void testEdit_InvalidToken() throws Exception {
        when(authService.validateToken(anyString())).thenReturn(false);

        ResponseEntity<?> response = taskActions.edit(fakeToken, 1L, "Title", testTaskRequest);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid or expired token", response.getBody());
    }

    @Test
    @Order(6)
    public void testEdit_UnauthorizedUser() throws Exception {
        // מדמה שהטוקן תקף אבל המשתמש לא מורשה לערוך את המשימה
        when(authService.validateToken(anyString())).thenReturn(true);
        when(userService.getEmailFromToken(anyString())).thenReturn("user@example.com");
        UserDto testUser2 = new UserDto();
        testUser2.setId(1L);
        testUser2.setUsername("meir2");
        testUser2.setPhone("0446622249");
        testUser2.setEmail("user2@example.com");
        testUser2.setPassword("Pa1ssword#");
        testUser2.setRole("USER");
        when(userService.getUserByToken(anyString())).thenReturn(testUser2);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        ResponseEntity<?> response = taskActions.edit(fakeToken, 1L, "Title", testTaskRequest);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("email not found", response.getBody());
    }

    @Test
    @Order(7)
    public void testEdit_TaskNotFound() throws Exception {
        // מדמה שהמשימה לא נמצאה
        when(authService.validateToken(anyString())).thenReturn(true);
        when(userService.getEmailFromToken(anyString())).thenReturn("user@example.com");
        when(userService.getUserByToken(anyString())).thenReturn(testUser);
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = taskActions.edit(fakeToken, 1L, "Title", testTaskRequest);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(8)
    public void testCreateTask() throws Exception {
        String fakeToken = "Bearer faketoken123";

        // הגדר את ההתנהגות של AuthService ו-UserService
        when(authService.validateToken(anyString())).thenReturn(true);
        when(userService.getUserId(anyString())).thenReturn(1L);


        ResponseEntity<?> response = taskActions.createTask(fakeToken, testTaskRequest);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(9)
    public void testDeleteTask_Success() throws Exception {
        // מדמה שהטוקן תקף והמשתמש מורשה
        when(authService.validateToken(anyString())).thenReturn(true);
        when(userService.getEmailFromToken(anyString())).thenReturn("user@example.com");
        when(userService.getUserByToken(anyString())).thenReturn(testUser);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        ResponseEntity<?> response = taskActions.deleteTask(fakeToken, 1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Order(10)
    public void testDeleteTask_TaskNotFound() throws Exception {
        // מדמה שהמשימה לא נמצאה
        when(authService.validateToken(anyString())).thenReturn(true);
        when(userService.getEmailFromToken(anyString())).thenReturn("user@example.com");
        when(userService.getUserByToken(anyString())).thenReturn(testUser);
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = taskActions.deleteTask(fakeToken, 1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(11)
    public void testDeleteTask_UnauthorizedUser() throws Exception {
        // מדמה שהטוקן תקף אבל המשתמש לא מורשה למחוק את המשימה
        when(authService.validateToken(anyString())).thenReturn(true);
        when(userService.getEmailFromToken(anyString())).thenReturn("user@example.com");
        UserDto testUser2 = new UserDto();
        testUser2.setId(2L); // משתמש אחר
        testUser2.setUsername("meir2");
        testUser2.setPhone("0446622249");
        testUser2.setEmail("user2@example.com");
        testUser2.setPassword("Pa1ssword#");
        testUser2.setRole("USER");
        when(userService.getUserByToken(anyString())).thenReturn(testUser2);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        ResponseEntity<?> response = taskActions.deleteTask(fakeToken, 1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(12)
    public void testDeleteTask_InvalidToken() throws Exception {
        when(authService.validateToken(anyString())).thenReturn(false);

        ResponseEntity<?> response = taskActions.deleteTask(fakeToken, 1L);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid or expired token", response.getBody());
    }

    @Test
    @Order(13)
    public void testGetTasks_Success() throws Exception {
        // מדמה שהטוקן תקף והמשתמש מורשה
        when(authService.validateToken(anyString())).thenReturn(true);
        when(userService.getEmailFromToken(anyString())).thenReturn("user@example.com");
        when(userService.getUserId(anyString())).thenReturn(1L);
        when(taskRepository.findByUserId(1L)).thenReturn(Arrays.asList(existingTask));

        ResponseEntity<?> response = taskActions.getTasks(fakeToken);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, ((List<?>) response.getBody()).size());
    }

    @Test
    @Order(14)
    public void testGetTasks_NoTasksFound() throws Exception {
        // מדמה שהטוקן תקף אבל אין משימות למשתמש
        when(authService.validateToken(anyString())).thenReturn(true);
        when(userService.getEmailFromToken(anyString())).thenReturn("user@example.com");
        when(userService.getUserId(anyString())).thenReturn(1L);
        when(taskRepository.findByUserId(1L)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = taskActions.getTasks(fakeToken);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Order(15)
    public void testGetTasks_InvalidToken() throws Exception {
        // מדמה שהטוקן לא תקף
        when(authService.validateToken(anyString())).thenReturn(false);

        ResponseEntity<?> response = taskActions.getTasks(fakeToken);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid or expired token", response.getBody());
    }
}
