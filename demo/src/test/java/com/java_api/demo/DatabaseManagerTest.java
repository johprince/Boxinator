package com.java_api.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.java_api.demo.User.AccountType;

import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerTest {
    private DatabaseManager databaseManager;

    @BeforeEach
    void setUp() {
        databaseManager = new DatabaseManager();
    }

    // ... other test cases ...

    @Test
    void testAddUser() {
        String firstName = "testing";
        String lastName = "some";
        String email = "fadee@fadeeee";
        String password = "secret";
        LocalDate dob = LocalDate.of(1990, 1, 1);
        String country = "USA";
        String zipCode = "12345";
        String contactNumber = "555-1234";
        AccountType accountType = AccountType.REGISTERED_USER;
        databaseManager.addUser(1, firstName, lastName, email, password, dob, country, zipCode, contactNumber, accountType.getValue(), "authToken");
        User user = databaseManager.getUserById(1);
        assertNotNull(user, "User should not be null after adding");
        assertEquals(firstName, user.getFirstName(), "First name should match the added value");
    }

    @Test
    void testGetUserById() {
        String firstName = "testing";
        String lastName = "some";
        String email = "fade@fade";
        String password = "secret";
        LocalDate dob = LocalDate.of(1990, 1, 1);
        String country = "USA";
        String zipCode = "12345";
        String contactNumber = "555-1234";
        AccountType accountType = AccountType.REGISTERED_USER;
        databaseManager.addUser(1, firstName, lastName, email, password, dob, country, zipCode, contactNumber, accountType.getValue(), "authToken");
        User user = databaseManager.getUserById(1);
        assertNotNull(user, "User should not be null");
        assertEquals(1, user.getId(), "User ID should match the requested ID");
    }

    // ... other test cases ...
}
