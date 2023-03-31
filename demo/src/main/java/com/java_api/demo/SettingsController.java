package com.java_api.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//import java.util.HashMap;
//import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class SettingsController {
    private final DatabaseManager db;
    private static final Logger logger = Logger.getLogger(SettingsController.class.getName());

    @Autowired
    public SettingsController(DatabaseManager db) {
        this.db = db;
    }
    
    @GetMapping("/settings/countries")
    public ResponseEntity<Map<String, Double>> getCountryMultipliers(@RequestHeader("Authorization") String authToken) {
        // Check if the user has permission to access the country multipliers
        User user = db.getUserByAuthToken(authToken);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            // Retrieve the list of countries from the database
            Map<String, Double> countryMultipliers = db.getCountries();
            // Return the country multipliers in the response
            return new ResponseEntity<>(countryMultipliers, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @PostMapping("/settings/countries/{country_id}")
    public ResponseEntity<String> addCountry(@PathVariable("country_id") String country,
                                             @RequestParam("multiplier") double multiplier,
                                             @RequestHeader("Authorization") String authToken) {
        // Check if the user has permission to add a new country
        User user = db.getUserByAuthToken(authToken);
        if (user == null || !user.getAccountType().getValue().equals("ADMINISTRATOR")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Add the new country and its multiplier to the database
        boolean success = db.addCountry(country, multiplier);

        if (success) {
            logger.log(Level.INFO, "Country added successfully: " + country);
            return new ResponseEntity<>("Country added successfully", HttpStatus.CREATED);
        } else {
            logger.log(Level.SEVERE, "Error adding country: " + country);
            return new ResponseEntity<>("Error adding country", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/settings/countries/{country_id}")
    public ResponseEntity<String> updateCountry(@PathVariable("country_id") String country,
                                                @RequestParam("multiplier") double multiplier,
                                                @RequestHeader("Authorization") String authToken) {
        // Check if the user has permission to update a country
        User user = db.getUserByAuthToken(authToken);
        if (user == null || !user.getAccountType().getValue().equals("ADMINISTRATOR")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Update the country multiplier in the database
        boolean success = db.updateCountryMultiplier(country, multiplier);

        if (success) {
            logger.log(Level.INFO, "Country updated successfully: " + country);
            return new ResponseEntity<>("Country updated successfully", HttpStatus.OK);
        } else {
            logger.log(Level.SEVERE, "Error updating country: " + country);
            return new ResponseEntity<>("Error updating country", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
