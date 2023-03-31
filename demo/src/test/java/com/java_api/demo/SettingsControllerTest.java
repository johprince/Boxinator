package com.java_api.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.is;


import com.java_api.demo.User.AccountType;

import java.util.HashMap;
import java.util.Map;

//import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SettingsController.class)
public class SettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DatabaseManager databaseManager;

    private String authToken;

    @BeforeEach
    public void setUp() {
        authToken = "dummyAuthToken";
    }

    @Test
    public void testGetCountryMultipliers() throws Exception {
        // Set up the mocked user and country multipliers
        User user = new User(0, "testUser", authToken, authToken, 
                     authToken, null, authToken, authToken, authToken,
                    AccountType.ADMINISTRATOR, authToken);
        Map<String, Double> countryMultipliers = new HashMap<>();
        countryMultipliers.put("US", 1.0);
        countryMultipliers.put("UK", 1.2);

        when(databaseManager.getUserByAuthToken(authToken)).thenReturn(user);
        when(databaseManager.getCountries()).thenReturn(countryMultipliers);

        mockMvc.perform(get("/settings/countries")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.US").value(1.0))
                .andExpect(jsonPath("$.UK").value(1.2));
    }

    @Test
    public void testAddCountryUnauthorizedUser() throws Exception {
        // Set up the mocked user
        User user = new User(0, "testUser", authToken, authToken, 
            authToken, null, authToken, authToken, authToken,
            AccountType.REGISTERED_USER, authToken);
        String country = "CA";
        double multiplier = 1.5;

        when(databaseManager.getUserByAuthToken(authToken)).thenReturn(user);

        mockMvc.perform(post("/settings/countries/{country_id}", country)
                .header("Authorization", authToken)
                .param("multiplier", String.valueOf(multiplier))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


    @Test
    public void testGetCountryMultipliersSuccess() throws Exception {
    // Set up the mocked user and country multipliers
        User user = new User(0, "testUser", authToken, authToken, 
                authToken, null, authToken, authToken, authToken,
                AccountType.REGISTERED_USER, authToken);
                Map<String, Double> countryMultipliers = new HashMap<>();
                countryMultipliers.put("US", 1.0);
                countryMultipliers.put("CA", 1.5);
            
                when(databaseManager.getUserByAuthToken(authToken)).thenReturn(user);
                when(databaseManager.getCountries()).thenReturn(countryMultipliers);
            
                mockMvc.perform(get("/settings/countries")
                        .header("Authorization", authToken))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.US", is(1.0)))
                        .andExpect(jsonPath("$.CA", is(1.5)));
            }
    
    @Test
    public void testAddCountrySuccess() throws Exception {
        // Set up the mocked user
        User user = new User(0, "testUser", authToken, authToken, 
        authToken, null, authToken, authToken, authToken,
       AccountType.ADMINISTRATOR, authToken);
        String country = "CA";
        double multiplier = 1.5;

        when(databaseManager.getUserByAuthToken(authToken)).thenReturn(user);
        when(databaseManager.addCountry(country, multiplier)).thenReturn(true);

        mockMvc.perform(post("/settings/countries/{country_id}", country)
            .header("Authorization", authToken)
            .param("multiplier", String.valueOf(multiplier))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().string("Country added successfully"));
    }


    @Test
    public void testUpdateCountrySuccess() throws Exception {
        // Set up the mocked user
        User user = new User(0, "testUser", authToken, authToken, 
            authToken, null, authToken, authToken, authToken,
            AccountType.ADMINISTRATOR, authToken);
        String country = "CA";
        double newMultiplier = 2.0;

        when(databaseManager.getUserByAuthToken(authToken)).thenReturn(user);
        when(databaseManager.updateCountryMultiplier(country, newMultiplier)).thenReturn(true);

        mockMvc.perform(put("/settings/countries/{country_id}", country)
                .header("Authorization", authToken)
            .param("multiplier", String.valueOf(newMultiplier))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("Country updated successfully"));
    }


}
