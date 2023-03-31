package com.java_api.demo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.java_api.demo.User.AccountType;

public class ShipmentsControllerTest {
    private DatabaseManager db;
    private ShipmentsController shipmentsController;
    private User user;
    private User admin;

    @BeforeEach
    public void setUp() {
        db = mock(DatabaseManager.class);
        shipmentsController = new ShipmentsController(db);
       
        user = new User(1, "John", "Doe", "john@example.com", "password", LocalDate.of(1990, 1, 1),
                "USA", "12345", "123-456-7890", AccountType.REGISTERED_USER, "auth-token");

        admin = new User(3, "boss", "Admin", "admin@example.com", "password", LocalDate.of(1985, 5, 10),
                "USA", "67890", "987-654-3210", AccountType.ADMINISTRATOR, "admin-auth-token");
    }

    @Test
    public void testGetShipmentsAsUser() {
        // Mock behavior
        when(db.getUserByAuthToken(user.getAuthToken())).thenReturn(user);
        List<String> userShipments = Arrays.asList("shipment1", "shipment2", "shipment3");
        when(db.getShipmentsByUserId(user.getId(), user.getAuthToken())).thenReturn(userShipments);

        // Call getShipments method
        ResponseEntity<String> response = shipmentsController.getShipments("Bearer " + user.getAuthToken());

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userShipments, response.getBody());
    }

    @Test
    public void testGetShipmentsAsAdmin() {
        // Mock behavior
        when(db.getUserByAuthToken(admin.getAuthToken())).thenReturn(admin);
        List<String> adminShipments = Arrays.asList("shipment1", "shipment2", "shipment3", "shipment4");
        when(db.getAllShipmentsAdmin()).thenReturn(adminShipments);

        // Call getShipments method
        ResponseEntity<String> response = shipmentsController.getShipments("Bearer " + admin.getAuthToken());

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(adminShipments, response.getBody());
    }

    @Test
    public void testGetShipmentsUnauthorized() {
        // Mock behavior
        when(db.getUserByAuthToken(user.getAuthToken())).thenReturn(null);

        // Call getShipments method
        ResponseEntity<String> response = shipmentsController.getShipments("Bearer " + user.getAuthToken());

        // Assertions
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testGetShipmentsInvalidAuthToken() {
        // Call getShipments method with invalid authorization header
        ResponseEntity<String> response = shipmentsController.getShipments("Invalid token");

        // Assertions
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }



    @Test
    public void testGetShipmentsCompleteAsUser() {
        // Set up test data
        List<String> completedShipments = Arrays.asList("shipment1", "shipment2", "shipment3");

        // Mock necessary methods
        when(db.getUserByAuthToken(user.getAuthToken())).thenReturn(user);
        when(db.getAllCurrentShipments(user.getId())).thenReturn(completedShipments);

        // Call the API and test the result
        ResponseEntity<String> response = shipmentsController.getShipments_complete("Bearer " + user.getAuthToken());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(completedShipments, response.getBody());
    }

    @Test
    public void testGetShipmentsCompleteAsAdmin() {
        // Set up test data
        List<String> completedShipments = Arrays.asList("shipment1", "shipment2", "shipment3");

        // Mock necessary methods
        when(db.getUserByAuthToken(admin.getAuthToken())).thenReturn(admin);
        when(db.getAllCurrentShipments(admin.getId(), admin.getAuthToken())).thenReturn(completedShipments);

        // Call the API and test the result
        ResponseEntity<String> response = shipmentsController.getShipments_complete("Bearer " + admin.getAuthToken());
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(completedShipments, response.getBody());
    }

}
