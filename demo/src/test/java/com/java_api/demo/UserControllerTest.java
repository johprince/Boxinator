package com.java_api.demo;



import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.mockito.Mockito.when;
import com.java_api.demo.User.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.mockito.ArgumentMatchers.eq;
import java.util.Map;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.web.servlet.MockMvc;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import static org.mockito.ArgumentMatchers.any;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import com.java_api.demo.UserController;
//import com.java_api.demo.DatabaseManager;
//import com.java_api.demo.User;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;


public class UserControllerTest {
    private DatabaseManager db;
    private UserController userController;
    private User user;
    private User admin;
    private User unauthorizedUser;

   // @Autowired
    //private MockMvc mockMvc;

    

    @BeforeEach
    public void setUp() {
        db = mock(DatabaseManager.class);
        userController = new UserController(db);

        user = new User(1, "John", "Doe", "john@example.com", "password", LocalDate.of(1990, 1, 1),
                "USA", "12345", "123-456-7890", AccountType.REGISTERED_USER, "auth-token");

        admin = new User(3, "boss", "Admin", "admin@example.com", "password", LocalDate.of(1985, 5, 10),
        "USA", "67890", "987-654-3210", AccountType.ADMINISTRATOR, "admin-auth-token");

        unauthorizedUser = new User(2, "Jane", "Doe", "jane@example.com", "password", LocalDate.of(1990, 1, 1),
                "USA", "12345", "123-456-7890", AccountType.REGISTERED_USER, "wrong-auth-token");
    }

    @Test
    public void getAccountSuccess() {
        when(db.getUserById(1)).thenReturn(user);
        when(db.getUserByAuthToken("auth-token")).thenReturn(user);

        ResponseEntity<Map<String, Object>> response = userController.getAccount("Bearer auth-token", 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John", response.getBody().get("firstName"));
        assertEquals("Doe", response.getBody().get("lastName"));
        assertEquals("john@example.com", response.getBody().get("email"));
    }


    @Test
    public void getAccountOwnSuccess() {
        when(db.getUserById(eq(user.getId()))).thenReturn(user);
        when(db.getUserByAuthToken(eq("auth-token"))).thenReturn(user);
        String token = "Bearer " + user.getAuthToken();
        ResponseEntity<Map<String, Object>> response = userController.getAccount( token, user.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getFirstName(), response.getBody().get("firstName"));
        assertEquals(user.getLastName(), response.getBody().get("lastName"));
        assertEquals(user.getEmail(), response.getBody().get("email"));
}


    @Test
    public void getAccountNotFound() {
        when(db.getUserById(1)).thenReturn(null);

        ResponseEntity<Map<String, Object>> response = userController.getAccount( "Bearer auth-token", 1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getAccountUnauthorized() {
        when(db.getUserById(1)).thenReturn(user);
        when(db.getUserByAuthToken("wrong-auth-token")).thenReturn(unauthorizedUser);

        ResponseEntity<Map<String, Object>> response = userController.getAccount( "Bearer wrong-auth-token", 1);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }


    @Test
    public void deleteUserSuccess() {
        when(db.getUserById(1)).thenReturn(user);
        when(db.getUserByAuthToken("admin-auth-token")).thenReturn(admin);
        when(db.deleteUser(1, "admin-auth-token")).thenReturn(true);

        ResponseEntity<String> response = userController.deleteUser("1", "Bearer admin-auth-token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Account deleted successfully", response.getBody());
}

    @Test
    public void deleteUserUnauthorized() {
        when(db.getUserById(1)).thenReturn(user);
        when(db.getUserByAuthToken("wrong-auth-token")).thenReturn(unauthorizedUser);

        ResponseEntity<String> response = userController.deleteUser("1", "Bearer wrong-auth-token");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized access", response.getBody());
    }

    @Test
    public void deleteUserNotFound() {
        when(db.getUserById(1)).thenReturn(null);

        ResponseEntity<String> response = userController.deleteUser("1", "admin-auth-token");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    public void deleteUserFailed() {
        when(db.getUserById(1)).thenReturn(user);
        when(db.getUserByAuthToken("admin-auth-token")).thenReturn(admin);
        when(db.deleteUser(1, "admin-auth-token")).thenReturn(false);

        ResponseEntity<String> response = userController.deleteUser("1", "Bearer admin-auth-token");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to delete account", response.getBody());
}



}
