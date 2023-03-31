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
import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import java.util.logging.*; 
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONObject;

@RestController
public class ShipmentsController {
    private final DatabaseManager db;
    private static final PolicyFactory POLICY = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
    private static final Logger logger = Logger.getLogger(ShipmentsController.class.getName());
    List <String> shipmentsForUserCache;

 
     
    @Autowired
    public ShipmentsController(DatabaseManager db) {
        this.db = db;
    }

    private Map<String, JSONObject> listToMap(List<String> shipments) {
        Map<String, JSONObject> shipmentsMap = new LinkedHashMap<>();
        int index = 1;
        for (String shipmentString : shipments) {
            shipmentsMap.put("shipment" + index, stringToJsonObject(shipmentString));
            index++;
        }
        return shipmentsMap;
    }
    
    private JSONObject stringToJsonObject(String shipmentString) {
        String[] keyValuePairs = shipmentString.split(", ");
        JSONObject jsonObject = new JSONObject();
        for (String keyValuePair : keyValuePairs) {
            String[] keyValue = keyValuePair.split(": ");
            jsonObject.put(keyValue[0], keyValue[1]);
        }
        return jsonObject;
    }
    
    private String mapToJson(Map<String, JSONObject> shipmentsMap) {
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, JSONObject> entry : shipmentsMap.entrySet()) {
            jsonObject.put(entry.getKey(), entry.getValue());
        }
        return jsonObject.toString();
    }


    
    // This returns all shipments that are either by user or by admin
    @GetMapping("/shipments")
    @Cacheable(value = "shipments", key = "#authToken")
    public ResponseEntity<String> getShipments(@RequestHeader("Authorization") String authToken) {
        String[] parts = authToken.split(" ");
            String token = "";

            if (parts.length == 2 && parts[0].equalsIgnoreCase("Bearer")) {
                token = parts[1];
             }else {
                // Handle the case when the authorization header format is incorrect.
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            
        User user = db.getUserByAuthToken(token);
        logger.info("Getting all shipments for user with auth token: " + token + "");
        if (user == null) {
            logger.info("User not found for auth token: " + token );
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (user.getAccountType().getValue().equals( "ADMINISTRATOR")) {
            List<String> shipmentsForAdmin = db.getAllShipmentsAdmin();
            
            Map<String, JSONObject> shipmentsMap = listToMap(shipmentsForAdmin);
            String shipmentsJson = mapToJson(shipmentsMap);
            logger.info("Returning all shipments for admin with auth token: " + token + "");
            return new ResponseEntity<String>(shipmentsJson, HttpStatus.OK);
        } else {
            List<String> shipmentsForUser = db.getShipmentsByUserId(user.getId(), token);
            //logger.info("Returning all shipments for user with auth token: " + token + "");
            //return new ResponseEntity<List<String>>(shipmentsForUser, HttpStatus.OK);
            Map<String, JSONObject> shipmentsMap = listToMap(shipmentsForUser);
            String shipmentsJson = mapToJson(shipmentsMap);
            return new ResponseEntity<String>(shipmentsJson, HttpStatus.OK);
        }
    }

   

    // Get all shipments that are complete
    // to filter out shipments that are not complete
    @Cacheable("shipments_complete")
    @GetMapping("/shipments/complete")
    public ResponseEntity<String> getShipments_complete(@RequestHeader("Authorization") String authToken) {
        String[] parts = authToken.split(" ");
            String token = "";

            if (parts.length == 2 && parts[0].equalsIgnoreCase("Bearer")) {
                token = parts[1];
             }else {
                // Handle the case when the authorization header format is incorrect.
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            
        User user = db.getUserByAuthToken(token);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (user.getAccountType().getValue() == "ADMINISTRATOR") {
            List<String> shipmentsForAdmin = db.getAllCurrentShipments(user.getId(),token);
            Map<String, JSONObject> shipmentsMap = listToMap(shipmentsForAdmin);
            String shipmentsJson = mapToJson(shipmentsMap);
            return new ResponseEntity<String>(shipmentsJson, HttpStatus.OK);
        } else {

            List <String> shipmentsForUserCompleted = db.getAllCurrentShipments(user.getId());
            
            Map<String, JSONObject> shipmentsMap = listToMap(shipmentsForUserCompleted);
            String shipmentsJson = mapToJson(shipmentsMap);
            return new ResponseEntity<String>(shipmentsJson, HttpStatus.OK);
            
           
        }
    }


    // Get all shipments that are cancelled

    @Cacheable("shipments_cancelled")
    @GetMapping("/shipments/cancelled")
    public ResponseEntity<String> getShipmentsCancelled(@RequestHeader("Authorization") String authToken) {
        try{
        logger.info("Getting all cancelled shipments for user with auth token: " + authToken + "");
        String[] parts = authToken.split(" ");
        String token = "";

        if (parts.length == 2 && parts[0].equalsIgnoreCase("Bearer")) {
            token = parts[1];
         }else {
            // Handle the case when the authorization header format is incorrect.
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        
        User user = db.getUserByAuthToken(token);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        List<String> cancelledShipments;
        if (user.getAccountType().getValue().equals("ADMINISTRATOR")) {
            cancelledShipments = db.gettAllcancelledShipList(-1); // Assuming -1 returns all cancelled shipments for admin
        } else {
            cancelledShipments = db.gettAllcancelledShipList(user.getId());
        }
        Map<String, JSONObject> shipmentsMap = listToMap(cancelledShipments);
        String shipmentsJson = mapToJson(shipmentsMap);
        return new ResponseEntity<String>(shipmentsJson, HttpStatus.OK);
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                logger.info("Error getting all cancelled shipments for user with auth token: " + authToken + "");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }else{
                logger.info("Error getting all cancelled shipments for user with auth token: " + authToken + "");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } 
    }



    
    
    


    @PostMapping("/shipments")
    public ResponseEntity<String> createShipment( @RequestHeader("Authorization") String authToken,
    @RequestBody Shipment shipment) {

        // Get request data from the Shipment object
            String senderName = shipment.getSenderName();
            String senderAddress = shipment.getSenderAddress();
            String senderCity = shipment.getSenderCity();
            String senderState = shipment.getSenderState();
            String senderZipCode = shipment.getSenderZipCode();
            String senderContactNumber = shipment.getSenderContactNumber();
            String receiverName = shipment.getReceiverName();
            String receiverAddress = shipment.getReceiverAddress();
            String receiverCity = shipment.getReceiverCity();
            String receiverState = shipment.getReceiverState();
            String receiverZipCode = shipment.getReceiverZipCode();
            String receiverContactNumber = shipment.getReceiverContactNumber();
            String weight = Double.toString(shipment.getWeight());
            String length = Double.toString(shipment.getLength());
            String width = Double.toString(shipment.getWidth());
            String height = Double.toString(shipment.getHeight());
            String shipmentType = shipment.getShipmentType();
            String shipmentStatus = shipment.getShipmentStatus();
            String deliveryTime = shipment.getDeliveryTime();
            String deliveryAddress = shipment.getDeliveryAddress();
            String deliveryCity = shipment.getDeliveryCity();
            String deliveryState = shipment.getDeliveryState();
            String deliveryZipCode = shipment.getDeliveryZipCode();
            String deliveryContactNumber = shipment.getDeliveryContactNumber();
            String deliveryInstructions = shipment.getDeliveryInstructions();
            String deliveryStatus = shipment.getDeliveryStatus();


            String[] parts = authToken.split(" ");
            String token = "";

            if (parts.length == 2 && parts[0].equalsIgnoreCase("Bearer")) {
                token = parts[1];
             }else {
                // Handle the case when the authorization header format is incorrect.
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            
            User user = db.getUserByAuthToken(token);
           

            if(user == null){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            try{     
                
                //LocalDate shipDate = LocalDate.parse(shipmentDate, formatter);
                Random random = new Random();
                int randomNumber = random.nextInt(100000);

                // Generate shipmentId
                String username = authToken + deliveryAddress + randomNumber;
                int hash = username.hashCode();
                int shipmentId = Math.abs(hash);
                String senderID = Integer.toString(user.getId());        
            // Add shipment to database
            if(db.addShipment(shipmentId, senderName, senderAddress, 
                senderCity, senderState, senderZipCode, senderContactNumber, receiverName, 
                receiverAddress, receiverCity, receiverState, receiverZipCode, receiverContactNumber, 
                weight, length, width, height, shipmentType, shipmentStatus, null,null, deliveryTime, 
                deliveryAddress, deliveryCity, deliveryState, deliveryZipCode, deliveryContactNumber, deliveryInstructions, deliveryStatus,
                senderID, token
            )){

                logger.log(Level.INFO, "Shipment created successfully");
                return new ResponseEntity<>("Shipment created successfully", HttpStatus.CREATED);
            }else{
                logger.log(Level.SEVERE, "Failed to create shipment");
                return new ResponseEntity<>("Failed to create shipment", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            }catch(Exception e){
                logger.log(Level.SEVERE, e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create shipment");
            }
    
    
    }







   @PutMapping("/shipments/{shipment_id}") // Needs work
    public ResponseEntity<String> updateShipment(@PathVariable("shipment_id") String shipmentId,
                                             @RequestHeader("Authorization") String authToken,
                                                @RequestBody Shipment requestData) {
        
        String[] parts = authToken.split(" ");
        String token = "";

        if (parts.length == 2 && parts[0].equalsIgnoreCase("Bearer")) {
            token = parts[1];
        }else {
        // Handle the case when the authorization header format is incorrect.
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String sanitized_shipmentId = sanitize(shipmentId);
        String sanitized_shipmentStatus = sanitize(requestData.getShipmentStatus());

        User user = db.getUserByAuthToken(token);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
 
        List <String> shipment = db.getShipmentById(Integer.parseInt(sanitized_shipmentId), token);
        String senderID = null;
        
        for (String info : shipment) {
            if (info.contains("senderID: ")) {
                int startIndex = info.indexOf("senderID: ") + "senderID: ".length();
                senderID = info.substring(startIndex);
                break;
            }
        }
        logger.info("senderid"+" "+senderID);
        logger.info("user id"+" "+user.getId());
        if (!user.getAccountType().getValue().equalsIgnoreCase("ADMINISTRATOR") && user.getId() != Integer.parseInt(senderID)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        try {
            db.updateShipment(Integer.parseInt(sanitized_shipmentId), sanitized_shipmentStatus);
            logger.log(Level.INFO, "Shipment updated successfully");
            return new ResponseEntity<>("Shipment updated successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update shipment");
        }
}


@GetMapping("/shipments/{shipment_id}")
public ResponseEntity<?> getShipmentById(@PathVariable("shipment_id") int shipmentId,
                                         @RequestHeader("Authorization") String authToken) {
    try {
        String[] parts = authToken.split(" ");
            String token = "";

            if (parts.length == 2 && parts[0].equalsIgnoreCase("Bearer")) {
                token = parts[1];
             }else {
                // Handle the case when the authorization header format is incorrect.
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            
            User currentUser = db.getUserByAuthToken(token);

        // Retrieve the shipment from the database
        //Shipment shipment = db.getShipmentById(shipmentId); // return list not shipment
        List<String> shipment = db.getShipmentById(shipmentId, token);
        // Check if the shipment exists
        if (shipment == null) {
            return new ResponseEntity<>("Shipment not found", HttpStatus.NOT_FOUND);
        }

        
        String senderID = null;

        for (String info : shipment) {
            if (info.contains("senderID: ")) {
                int startIndex = info.indexOf("senderID: ") + "senderID: ".length();
                senderID = info.substring(startIndex);
                break;
            }
        }
        
        // Check if the current user has access to the shipment or user is admin
        if (currentUser.getId() != Integer.parseInt(senderID) &&
                !currentUser.getAccountType().getValue().equals("ADMINISTRATOR")) {
            return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }

        Map<String, JSONObject> shipmentsMap = listToMap(shipment);
        String shipmentsJson = mapToJson(shipmentsMap);
        return new ResponseEntity<>(shipmentsJson, HttpStatus.OK);

    } catch (Exception e) {
        logger.log(Level.SEVERE, e.getMessage(), e);
        return new ResponseEntity<>("Error getting shipment", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


         
@GetMapping("/shipments/customer/{customer_id}")
@Cacheable(value = "shipments", key = "#authToken")
  public ResponseEntity<String> getShipmentsByCustomerId(
      @RequestHeader("Authorization") String authToken) {

        String[] parts = authToken.split(" ");
        String token = "";

        if (parts.length == 2 && parts[0].equalsIgnoreCase("Bearer")) {
            token = parts[1];
         }else {
            // Handle the case when the authorization header format is incorrect.
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        
             User user = db.getUserByAuthToken(token);
            
            if (user == null) {
                logger.info("user not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
    
            if (user.getAccountType().getValue().equals("ADMINISTRATOR")) {
                List<String> shipments = db.getAllShipmentsAdmin();
                Map<String, JSONObject> shipmentsMap = listToMap(shipments);
                String shipmentsJson = mapToJson(shipmentsMap);
                return new ResponseEntity<String> (shipmentsJson, HttpStatus.OK);
            }

        else if (user.getAccountType().getValue().equals("REGISTERED_USER") ||
                user.getAccountType().getValue().equals("GUEST")) {
            List<String> shipments = db.getShipmentsByUserId(user.getId(), token);
            Map<String, JSONObject> shipmentsMap = listToMap(shipments);
            String shipmentsJson = mapToJson(shipmentsMap);
            return new ResponseEntity<String>(shipmentsJson, HttpStatus.OK);
        }
        else {
         return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
}
    
  



  /**
 * @param shipmentId
 * @param authToken
 * @return
 * 
 * This method is used to delete a shipment by its id. The shipment id is passed in as a path variable.
 * Only administrators can delete shipments.
 */
@DeleteMapping("/shipments/{shipment_id}") // works
    public ResponseEntity<String> deleteShipment(@PathVariable("shipment_id") int shipmentId,
                                              @RequestHeader("Authorization") String authToken) {
    
    // Sanitize input
    if (shipmentId < 0) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Shipment ID must be a positive integer");
    }

    String temp = sanitize(Integer.toString(shipmentId));
    int shipmentIdInt = Integer.parseInt(temp);
    // Retrieve the current user from the auth token
    
    String[] parts = authToken.split(" ");
            String token = "";

            if (parts.length == 2 && parts[0].equalsIgnoreCase("Bearer")) {
                token = parts[1];
             }else {
                // Handle the case when the authorization header format is incorrect.
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            
            User user = db.getUserByAuthToken(token);
    if (user == null) {
        logger.info("user not found for auth token" + "  "+ token);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("user not found for auth token");
    }

    // Check if user is an administrator
    if ((!user.getAccountType().getValue().equals("ADMINISTRATOR"))) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only administrators can delete shipments");
    }
    
    // Attempt to delete the shipment
    boolean deleted = db.deleteShipment(shipmentIdInt);
    logger.info("shipment deleted returned" + "  "+ deleted);
    // Return appropriate response
    if (deleted) {
        return ResponseEntity.ok("Shipment with ID " + shipmentIdInt + " was successfully deleted");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Shipment with ID " + shipmentIdInt + " was not found");
    }
}
   
 public static String sanitize(String input) {
        return POLICY.sanitize(input);
        }
}

