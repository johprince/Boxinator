package com.java_api.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;;
import java.util.logging.*; 
import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import com.java_api.demo.User;
import com.java_api.demo.User.AccountType;

import java.util.HashMap;
import java.util.Map;
//import com.java_api.demo.Shipment;
//import java.util.List;
import java.util.Random;

@RestController
public class UserController {
    private final DatabaseManager db;
    Logger logger = Logger.getLogger(ShipmentsController.class.getName());
     
    @Autowired
    public UserController(DatabaseManager db) {
        this.db = db;
    }


/* 
@GetMapping("/user/{user_id}")
public ResponseEntity<User> getUser(@PathVariable("user_id") int userId, @RequestHeader("Authorization") String authToken) {
    User user = db.getUserById(userId);
    if (user == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    // Check if the user is authorized to access this resource
    if (!authToken.equals(user.getAuthToken())) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    return new ResponseEntity<>(user, HttpStatus.OK);
} */


// Retrieve the account information of a specific user.

/*@GetMapping("/account/{account_id}") // works
public ResponseEntity<Map<String, Object>> getAccount(@PathVariable("account_id") String accountId, 
                                                      @RequestHeader("Authorization") String authToken) {
    User user = db.getUserById(Integer.parseInt(accountId));

    // Check if user exists
    if (user == null) {
        logger.info("User not found");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    User authTokenUser = db.getUserByAuthToken(authToken);
    // Check if the user has permission to access the account information
    if (!authToken.equals(user.getAuthToken()) && 
    (authTokenUser == null || !authTokenUser.getAccountType().getValue().equals("ADMINISTRATOR"))) {
            logger.info("Unauthorized access");
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    // Create a map to store the user information
    logger.info("User account information retrieved successfully");
    Map<String, Object> userData = new HashMap<>();
    userData.put("firstName", user.getFirstName());
    userData.put("lastName", user.getLastName());
    userData.put("email", user.getEmail());

    return new ResponseEntity<>(userData, HttpStatus.OK);
}*/


@GetMapping("/account/{account_id}")
public ResponseEntity<Map<String, Object>> getAccount(@RequestHeader("Authorization") String authToken, 
            @PathVariable("account_id") int accountId) {
     
    User user = db.getUserById(accountId);

    // Check if user exists
    if (user == null) {
        logger.info("User not found");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    String[] parts = authToken.split(" ");
    String token = " ";
    
    if (parts.length == 2 && parts[0].equalsIgnoreCase("Bearer")) {
            token = parts[1];
    }else {
        // Handle the case when the authorization header format is incorrect.
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    logger.info("Token: " + token);
    User authTokenUser = db.getUserByAuthToken(token);
    // Check if the user has permission to access the account information
    if (!token.equals(user.getAuthToken()) &&
    (authTokenUser == null || !authTokenUser.getAccountType().getValue().equals("ADMINISTRATOR"))) {
        logger.info("Unauthorized access");
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    // Create a map to store the user information
    logger.info("User account information retrieved successfully");
    Map<String, Object> userData = new HashMap<>();
    userData.put("firstName", user.getFirstName());
    userData.put("lastName", user.getLastName());
    userData.put("email", user.getEmail());
    userData.put("accountType", user.getAccountType().getValue());
    userData.put("dateOfBirth", user.getDateOfBirth());
    userData.put("countryOfResidence", user.getCountryOfResidence());
    
    

    return new ResponseEntity<>(userData, HttpStatus.OK);
}



@PutMapping("/account/{account_id}")
public ResponseEntity<String> updateUserAccount(@PathVariable int account_id ,@RequestBody RequestData requestData, 
@RequestHeader("Authorization") String authToken) {
    int accountId = account_id;
    String firstName = requestData.getFirstName();
    String lastName = requestData.getLastName();
    String email = requestData.getEmail();



    String[] parts = authToken.split(" ");
    String token = "";
    if (parts.length == 2 && parts[0].equalsIgnoreCase("Bearer")) {
            token = parts[1];
    }else {
        // Handle the case when the authorization header format is incorrect.
         return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    User currentUser = db.getUserById(accountId);

    // Check if user exists
    if (currentUser == null) {
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    // Check if the user has permission to update the account
    if (db.getUserByAuthToken(token) == null || !token.equals(currentUser.getAuthToken())) {
        return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
    }

    // Update the user account information
    boolean success = db.updateUser(accountId, token, firstName, lastName, email);
    if (success) {
        return new ResponseEntity<>("Account updated successfully", HttpStatus.OK);
    } else {
        return new ResponseEntity<>("Failed to update account", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}





// First Name - required
// Last Name - required
// E-mail - required, validate on client side
// Password - required, strong, and must be confirmed by repetition
//  Date of birth
// Country of residence
// Zip code / postal code
// Contact number


/*@PostMapping("/account")
public ResponseEntity<String> createUser(@RequestParam String firstName, @RequestParam String LastName, @RequestParam String email,
 @RequestParam String password, @RequestParam LocalDate dateOfBirth,
@RequestParam String countryResidence, @RequestParam String zipcode , @RequestParam String phonenumnber){
    try{ //TODO: modify, not good enough
        if (db.getUserByEmail(email) != -1) {
            return new ResponseEntity<>("Email already taken", HttpStatus.CONFLICT);
        }
        Random random = new Random();
        int randomNumber = random.nextInt(100000);
        // Generate the password hash
        String username = firstName + LastName + randomNumber;
        int hash = username.hashCode();
        int userId = Math.abs(hash);        
        String authToken = DatabaseManager.generateAuthToken(userId, password);
        AccountType accountType = AccountType.REGISTERED_USER; // TODO: set so user can be a guest or user

        // Create the user with the hashed password
        db.addUser(userId, firstName, LastName, email, password, dateOfBirth , countryResidence, zipcode, phonenumnber, accountType.getValue(), authToken);
        // Return a success response with the ID of the created user
        return new ResponseEntity<>("Created user with ID: " + userId, HttpStatus.CREATED); 

    
    } catch(Exception e){
        logger.log(Level.SEVERE, "Error creating user: " + e.getMessage());
        return new ResponseEntity<>("Error creating user", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}*/



@PostMapping("/account")
public ResponseEntity<String> createUser(@RequestBody RequestData requestData) {
    String firstName = requestData.getFirstName();
    String lastName = requestData.getLastName();
    String email = requestData.getEmail();
    String password = requestData.getPassword();
    LocalDate dateOfBirth = requestData.getDateOfBirth();
    String countryResidence = requestData.getCountryResidence();
    String zipcode = requestData.getZipcode();
    String phoneNumber = requestData.getPhoneNumber();

    try {
        if (db.getUserByEmail(email) != -1) {
            return new ResponseEntity<>("Email already taken", HttpStatus.CONFLICT);
        }
        Random random = new Random();
        int randomNumber = random.nextInt(100000);
        String username = firstName + lastName + randomNumber;
        int hash = username.hashCode();
        int userId = Math.abs(hash);
        String authToken = DatabaseManager.generateAuthToken(userId, password);
        AccountType accountType = AccountType.REGISTERED_USER;

        db.addUser(userId, firstName, lastName, email, password, dateOfBirth, countryResidence, zipcode, phoneNumber, accountType.getValue(), authToken);
        return new ResponseEntity<>("Created user with ID: " + userId, HttpStatus.CREATED);

    } catch (Exception e) {
        logger.log(Level.SEVERE, "Error creating user: " + e.getMessage());
        return new ResponseEntity<>("Error creating user", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}





@DeleteMapping("/account/{account_id}")
public ResponseEntity<String> deleteUser(@PathVariable("account_id") String accountId, 
                                    @RequestHeader("Authorization") String authToken) {
    User user = db.getUserById(Integer.parseInt(accountId));

    // Check if user exists
    if (user == null) {
        logger.info("User not found");
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    // Check if the user has permission to delete the account

    String[] parts = authToken.split(" ");
    String token = "";

            if (parts.length == 2 && parts[0].equalsIgnoreCase("Bearer")) {
                token = parts[1];
             }else {
                // Handle the case when the authorization header format is incorrect.
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
    User admin = db.getUserByAuthToken(token);
    if (admin == null ) {
        logger.info("Admin not found");
        return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
    }
    logger.info("Admin found " + " " + admin.getAccountType().getValue() );
    if (!admin.getAccountType().getValue().equals("ADMINISTRATOR"))   {
        logger.info("Unauthorized access");
        return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
    }

    // Delete the user account
    boolean success = db.deleteUser(user.getId(), token);

    if (success) {
        logger.info("User account deleted successfully");
        return new ResponseEntity<>("Account deleted successfully", HttpStatus.OK);
    } else {
        logger.info("Failed to delete account");
        return new ResponseEntity<>("Failed to delete account", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    }
    


    static class RequestData {
        private String accountId;
        private String firstName;
        private String lastName;
        private String email;
        private String authToken;
        private String password;
        private LocalDate dateOfBirth;
        private String countryResidence;
        private String zipcode;
        private String phoneNumber;
    
        // Getters and setters

        public LocalDate getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getCountryResidence() {
            return countryResidence;
        }

        public void setCountryResidence(String countryResidence) {
            this.countryResidence = countryResidence;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        
        
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }


        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAuthToken() {
            return authToken;
        }


    }
    
   

}