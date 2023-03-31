package com.java_api.demo;


import java.sql.*;
import java.time.LocalDate;
import java.util.Random;
import java.util.logging.*;
import org.springframework.stereotype.Repository;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.java_api.demo.User.AccountType;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import org.apache.http.cookie.SM;

@Repository
public class DatabaseManager {
    private static final String USER_DB_URL = "jdbc:sqlite:users.db";
    private static final String SHIPMENT_DB_URL = "jdbc:sqlite:shipments.db";
    private static final String COUNTRIES_DB_URL = "jdbc:sqlite:countries.db";
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    //private static final int basePrice = 200;
    private static final Logger logger = Logger .getLogger(DatabaseManager.class.getName()); 
    Map<String, Double> countries = new HashMap<String, Double>();
    
    public DatabaseManager() {
        // Create the user and shipment tables if they don't exist
        logger.info("Executing DatabaseManager constructor and creating tables(if they don't exist))");
        createUserTable();
        createShipmentTable();
        createCountriesTable();
    }

    // To use non-static methods in a static context
    public DatabaseManager(String msg){
        logger.info(msg);
    }

    public Map<String,Double> getCountries(){
        Map<String,Double> countries_multipliers = new HashMap<String,Double>();
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Connection conn = DriverManager.getConnection(COUNTRIES_DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT * FROM countries";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                countries_multipliers.put(rs.getString("country"),
                 rs.getDouble("multiplier"));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error: " + e.getMessage());
        }

        return countries_multipliers;
    }


    public void createUserTable() {
        // SQLite connection string 
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {

            logger.log(Level.SEVERE, "Error: " + e.getMessage());
        
        }                
        try (Connection conn = DriverManager.getConnection(USER_DB_URL);
             Statement stmt = conn.createStatement()) {
          // Create the user table if it doesn't exist
          logger.log(Level.INFO, "Creating user table if it doesn't exist");
          String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                     + "id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                     + "firstName TEXT  NULL,\n"
                     + "lastName TEXT  NULL,\n"
                     + "email TEXT UNIQUE NOT NULL,\n"
                     + "password TEXT NOT NULL,\n"
                     + "dob DATE NOT NULL,\n"
                     + "country TEXT NOT NULL,\n"
                     + "zipCode TEXT  NULL,\n"
                     + "contactNumber TEXT  NULL,\n"
                     + "accountType TEXT NOT NULL,\n"
                     + "authToken  TEXT NOT NULL\n" 
                     + ");";
          stmt.execute(sql);
          logger.log(Level.INFO, "User table created successfully!");
        } catch (SQLException e) {
          logger.log(Level.SEVERE, "Error: " + e.getMessage());
        }
      }


    public void createShipmentTable() {
        // SQLite connection string 
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {        
            e.printStackTrace();

        } 
        try (Connection conn = DriverManager.getConnection(SHIPMENT_DB_URL);
             Statement stmt = conn.createStatement()) {
          // Create the shipment table if it doesn't exist
          logger.log(Level.INFO, "Creating shipment table if it doesn't exist");
          String sql = "CREATE TABLE IF NOT EXISTS shipments (\n"
                     + "id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                     + "senderName TEXT NOT NULL,\n"
                     + "senderAddress TEXT  NULL,\n"
                     + "senderCity TEXT NOT NULL,\n"
                     + "senderState TEXT  NULL,\n"
                     + "senderZipCode TEXT  NULL,\n"
                     + "senderContactNumber TEXT  NULL,\n"
                     + "receiverName TEXT  NULL,\n"
                     + "receiverAddress TEXT  NULL,\n"
                     + "receiverCity TEXT  NULL,\n"
                     + "receiverState TEXT  NULL,\n"
                     + "receiverZipCode TEXT NOT NULL,\n"
                     + "receiverContactNumber TEXT NOT NULL,\n"
                     + "weight TEXT NOT NULL,\n"
                     + "length TEXT  NULL,\n"
                     + "width TEXT NOT NULL,\n"
                     + "height TEXT NOT NULL,\n"
                     + "shipmentType TEXT  NULL,\n"
                     + "shipmentStatus TEXT NOT NULL,\n"
                     + "shipmentDate DATE  NULL,\n"
                     + "deliveryDate DATE  NULL,\n"
                     + "deliveryTime TEXT  NULL,\n"
                     + "deliveryAddress TEXT  NULL,\n"
                     + "deliveryCity TEXT  NULL,\n"
                     + "deliveryState TEXT  NULL,\n"
                     + "deliveryZipCode TEXT  NULL,\n"
                     + "deliveryContactNumber TEXT  NULL,\n"
                     + "deliveryInstructions TEXT NULL,\n"                 
                     + "deliveryStatus TEXT  NULL,\n"
                     + "senderID TEXT NOT NULL,\n"
                     + "senderAUTH TEXT NOT NULL\n"  
                    + ");";
            stmt.execute(sql);
            logger.log(Level.INFO, "Shipment table created successfully!");
        } catch (SQLException e) {          
            logger.log(Level.SEVERE, "Error: " + e.getMessage());
        }
    }

    

    // Create countries table

    public void createCountriesTable(){
        //Map<String, Double> countries = new HashMap<String, Double>();
        countries.put("Albania", 2.2777);
        countries.put("Andorra", 1.3623);
        countries.put("Austria", 1.2564);
        countries.put("Belarus", 1.4061);
        countries.put("Belgium", 1.1879);
        countries.put("Bosnia and Herzegovina", 2.0298);
        countries.put("Bulgaria", 2.7889);
        countries.put("Croatia", 1.8395);
        countries.put("Cyprus", 3.2386);
        countries.put("Czech Republic", 1.2651);
        countries.put("Estonia", 6.466);
        countries.put("Finland", 1.0407);
        countries.put("France", 1.7345);
        countries.put("Germany", 1.2564);
        countries.put("Greece", 3.1639);
        countries.put("Hungary", 1.7298);
        countries.put("Iceland", 1.1187);
        countries.put("Ireland", 1.4493);
        countries.put("Italy", 2.1247);
        countries.put("Kosovo", 2.1416);
        countries.put("Latvia", 5.238);
        countries.put("Liechtenstein", 1.1477);
        countries.put("Lithuania", 7.961);
        countries.put("Luxembourg", 1.1777);
        countries.put("Malta", 2.3624);
        countries.put("Moldova", 1.9805);
        countries.put("Monaco", 1.5849);
        countries.put("Montenegro", 1.9851);
        countries.put("Netherlands", 1.0539);
        countries.put("North Macedonia", 2.1612);
        countries.put("Poland", 1.1187);
        countries.put("Portugal", 2.6775);
        countries.put("Romania", 2.0316);
        countries.put("Russia", 2.2276);
        countries.put("San Marino", 1.7236);
        countries.put("Serbia", 1.8912);
        countries.put("Slovakia", 1.3905);
        countries.put("Slovenia", 1.5791);
        countries.put("Spain", 2.2284);
        countries.put("Switzerland", 1.3544);
        countries.put("Ukraine", 1.5171);
        countries.put("United Kingdom", 1.1592);
        countries.put("Vatican City", 1.7240);
        
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {        
            e.printStackTrace();

        } 
        try (Connection conn = DriverManager.getConnection(COUNTRIES_DB_URL);
             Statement stmt = conn.createStatement()) {
          // Create the shipment table if it doesn't exist
          logger.log(Level.INFO, "Creating countries table if it doesn't exist");
          String sql = "CREATE TABLE IF NOT EXISTS countries (\n"
                     + "id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                     + "country TEXT NOT NULL,\n"
                     + "multiplier TEXT NOT NULL\n"
                     + ");";
            stmt.execute(sql);
            logger.log(Level.INFO, "Countries table created successfully!");
            conn.close();
            stmt.close();

        } catch (SQLException e) {          
            logger.log(Level.SEVERE, "Error: " + e.getMessage());
        } 
        // Insert countries data into the table
        try (Connection conn = DriverManager.getConnection(COUNTRIES_DB_URL);
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO countries(country, multiplier) VALUES(?, ?)")) {
                for (Map.Entry<String, Double> entry : countries.entrySet()) {
                    String country = entry.getKey();
                    double multiplier = entry.getValue();
                    pstmt.setString(1, country);
                    pstmt.setDouble(2, multiplier);
                    pstmt.executeUpdate();
                }
                logger.log(Level.INFO, "Countries data inserted successfully!");
                conn.close();
                pstmt.close();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error: " + e.getMessage());
            } 

           
        
    }   
   


    // Add country and multiplier to the countries table

    public boolean addCountry(String country, double multiplier){
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean added = false;
        try{
            conn = DriverManager.getConnection(COUNTRIES_DB_URL);
            stmt = conn.prepareStatement("INSERT INTO countries(country, multiplier) VALUES(?, ?)");
            stmt.setString(1, country);
            stmt.setDouble(2, multiplier);
            stmt.executeUpdate();
            added = true;
            logger.log(Level.INFO, "Country added successfully!");
        }catch(SQLException e){
            logger.log(Level.SEVERE, "Error: " + e.getMessage());
        }finally{
            try{
                if(stmt != null){
                    stmt.close();
                }
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                logger.log(Level.SEVERE, "Error: " + e.getMessage());
            }
        }
        return added;
    }

    // Update country multiplier
    public boolean updateCountryMultiplier(String country, double multiplier){
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean updated = false;
        try{
            conn = DriverManager.getConnection(COUNTRIES_DB_URL);
            stmt = conn.prepareStatement("UPDATE countries SET multiplier = ? WHERE country = ?");
            stmt.setDouble(1, multiplier);
            stmt.setString(2, country);
            stmt.executeUpdate();
            updated = true;
            logger.log(Level.INFO, "Country multiplier updated successfully!");
        }catch(SQLException e){
            logger.log(Level.SEVERE, "Error: " + e.getMessage());
        }finally{
            try{
                if(stmt != null){
                    stmt.close();
                }
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                logger.log(Level.SEVERE, "Error: " + e.getMessage());
            }
        }
        return updated;
    }

    // Get countries muliplier 
    public double getCountryMultiplier(String toCountry){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        double country_multiplier= -1;
        try{
            conn = DriverManager.getConnection(COUNTRIES_DB_URL);
            stmt = conn.prepareStatement("SELECT * FROM countries WHERE country = ?");
            stmt.setString( 1, toCountry);
            rs = stmt.executeQuery();
            
            if(rs.next()){
                country_multiplier = rs.getDouble("multiplier");
            }


        }catch(SQLException e){
            logger.log(Level.SEVERE, "Error: " + e.getMessage());
        }finally{
            try{
                if(rs != null){
                    rs.close();
                }
                if(stmt != null){
                    stmt.close();
                }
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                logger.log(Level.SEVERE, "Error: " + e.getMessage());
            }
        }
        return country_multiplier;

    }

    public int getCountriesInfo(String fromCountry, String toCountry, int weight) {
        double countryMultiplier = getCountryMultiplier(toCountry);
        int cost = calculateShipmentCost(fromCountry, toCountry, weight, countryMultiplier);
        return cost;
    }

    private int calculateShipmentCost(String fromCountry, String toCountry, int weight, double countryMultiplier) {
        int flatFee = 200;

        if (fromCountry.equalsIgnoreCase("Norway") && (toCountry.equalsIgnoreCase("Sweden") || toCountry.equalsIgnoreCase("Denmark"))
                || fromCountry.equalsIgnoreCase("Sweden") && (toCountry.equalsIgnoreCase("Norway") || toCountry.equalsIgnoreCase("Denmark"))
                || fromCountry.equalsIgnoreCase("Denmark") && (toCountry.equalsIgnoreCase("Norway") || toCountry.equalsIgnoreCase("Sweden"))) {
            return flatFee;
        } else {
            double additionalFee = weight * countryMultiplier;
            return flatFee + (int) Math.round(additionalFee);
        }
    }



    

    public boolean addCountry(String country, Double multiplier) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            conn = DriverManager.getConnection(COUNTRIES_DB_URL);
            stmt = conn.prepareStatement("INSERT INTO countries(country, multiplier) VALUES(?, ?)");
            stmt.setString(1, country);
            stmt.setDouble(2, multiplier);
            stmt.executeUpdate();
            result = true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error: " + e.getMessage());
            }
        }
        return result;
    }

    public void addUser(int userID,String firstName, String lastName, String email, String password, LocalDate dob,
                        String country, String zipCode, String contactNumber, String accountType, String authToken) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int method_result = getUserByEmail(email);
        logger.info("getUserByEmail returned: " + method_result);               
        if(method_result == -1){
            
            
       
        
          logger.info("Adding user to database With the values: " + userID + " " + firstName + " " + lastName + " " + email + " " + password + " " + dob + " " + country + " " + zipCode + " " + contactNumber + " " + accountType + " " + authToken);
        try {
           

            // Open database connection
            conn = DriverManager.getConnection(USER_DB_URL);
            logger.log(Level.INFO, "Connection to database established");
            // Prepare SQL statement
            stmt = conn.prepareStatement("INSERT OR IGNORE INTO users (id ,firstname, lastname, email, password, dob,"
            + "country, zipCode, contactNumber, accountType, authToken) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            stmt.setInt(1, userID);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, email);
            stmt.setString(5, password);
            stmt.setDate(6, Date.valueOf(dob));
            stmt.setString(7, country);
            stmt.setString(8, zipCode);
            stmt.setString(9, contactNumber);
            stmt.setString(10, accountType);
            stmt.setString(11, authToken);
            
            // Execute SQL statement
            logger.log(Level.INFO, "Executing SQL statement: " + stmt.toString());  
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "Error: " + e.getMessage());
        } finally {
            // Close statement and connection
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    }
    
    public User getUserById(int id) {
        logger.info("Getting user from database with id: " + id);
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;
        
        try {
            // Open database connection
            conn = DriverManager.getConnection(USER_DB_URL);
            
            // Prepare SQL statement
            stmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
            stmt.setInt(1, id);
            
            // Execute SQL statement and get result set
            rs = stmt.executeQuery();
            
            // Extract user information from result set
            if (rs.next()) {
                int userId = rs.getInt("id");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String email = rs.getString("email");
                String password = rs.getString("password");
                LocalDate dob = rs.getDate("dob").toLocalDate();
                String country = rs.getString("country");
                String zipCode = rs.getString("zipCode");
                String contactNumber = rs.getString("contactNumber");
                String accountType = rs.getString("accountType");
                AccountType acc_type = AccountType.fromValue(accountType);
                String authToken = rs.getString("authToken");
                logger.info("User found with the values: " + userId + " " + firstName + " " + lastName + " " + email + " " + password + " " + dob + " " + country + " " + zipCode + " " + contactNumber + " " + accountType + " " + authToken);
                user = new User(userId, firstName, lastName, email, password, dob, country, zipCode, contactNumber, acc_type, authToken);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error: " + e.getMessage());
            
        } finally {
            // Close result set, statement, and connection
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return user;
    }


 
    public boolean addShipment(int id,String senderName, String senderAddress, 
    String senderCity, String senderState, String senderZipCode, String senderContactNumber,
                                String receiverName, String receiverAddress, String receiverCity, String receiverState, String receiverZipCode, String receiverContactNumber,
                                String weight, String length, String width, String height, String shipmentType, String shipmentStatus, LocalDate shipmentDate, LocalDate deliveryDate, String deliveryTime,
                                String deliveryAddress, String deliveryCity, String deliveryState, String deliveryZipCode, String deliveryContactNumber, String deliveryInstructions, String deliveryStatus, String senderID, String senderAUTH) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            // Open database connection
            conn = DriverManager.getConnection(SHIPMENT_DB_URL);
            logger.log(Level.INFO, "Connection to database established");
            // Prepare SQL statement
            stmt = conn.prepareStatement("INSERT INTO shipments ( senderName, senderCity, receiverZipCode, receiverContactNumber, weight, width, height, shipmentStatus, senderID, senderAUTH) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            
            stmt.setString(1, senderName);
            stmt.setString(2, senderCity);
            stmt.setString(3, receiverZipCode);
            stmt.setString(4, receiverContactNumber);
            stmt.setString(5, weight);
            stmt.setString(6, width);
            stmt.setString(7, height);
            stmt.setString(8, shipmentStatus);
            stmt.setString(9, senderID);
            stmt.setString(10, senderAUTH);

            // Execute SQL statement
            stmt.executeUpdate();
            logger.log(Level.INFO, "Executing SQL statement: " + stmt.toString());
            success = true;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error: " + e.getMessage());
        } finally {
            // Close statement and connection
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error: " + e.getMessage());
            }
        }
        return success;
    }

   


    // Find shipment by id and update shipment status
    public boolean updateShipment(int shipmentID, String new_status){
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        try {
            // Open database connection
            conn = DriverManager.getConnection(SHIPMENT_DB_URL);
            logger.log(Level.INFO, "Connection to database established");
            // Prepare SQL statement
            stmt = conn.prepareStatement("UPDATE shipments SET shipmentStatus = ? WHERE id = ?");
            stmt.setString(1, new_status);
            stmt.setInt(2, shipmentID);
            // Execute SQL statement
            stmt.executeUpdate();
            logger.log(Level.INFO, "Executing SQL statement: " + stmt.toString());
            success = true;

        }catch(SQLException e){
            logger.log(Level.SEVERE, "Error: " + e.getMessage());
        }finally{
            // Close statement and connection
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error: " + e.getMessage());
            }
        }
        return success;
    }

    // TODO: implement getAllCurrentShipments method to extract all shipments from the database 
    // if user is admin, return all shipments, else return unauthorized
    public List<String>getAllCurrentShipments(int id,String adminToken){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<String> shipmentsList = new ArrayList<String>();
        //User user = null;
        //Shipment shipment = null;

        try{
            conn = DriverManager.getConnection(SHIPMENT_DB_URL);
            logger.log(Level.INFO, "Connection to database established");
           
            stmt = conn.prepareStatement("SELECT * FROM shipments WHERE shipmentStatus = 'IN_TRANSIT' OR shipmentStatus = 'CREATED'");
            rs = stmt.executeQuery();

          
                // User is an admin          
                while(rs.next()){
                    StringBuilder shipmentInfo = new StringBuilder();
                    shipmentInfo.append("id: ").append(rs.getInt("id")).append(", ");
                    shipmentInfo.append("senderName: ").append(rs.getString("senderName")).append(", ");
                    shipmentInfo.append("senderAddress: ").append(rs.getString("senderAddress") != null ? rs.getString("senderAddress") : "NULL").append(", ");
                    shipmentInfo.append("senderCity: ").append(rs.getString("senderCity")).append(", ");
                    shipmentInfo.append("senderState: ").append(rs.getString("senderState") != null ? rs.getString("senderState") : "NULL").append(", ");
                    shipmentInfo.append("senderZipCode: ").append(rs.getString("senderZipCode") != null ? rs.getString("senderZipCode") : "NULL").append(", ");
                    shipmentInfo.append("senderContactNumber: ").append(rs.getString("senderContactNumber") != null ? rs.getString("senderContactNumber") : "NULL").append(", ");
                    shipmentInfo.append("receiverName: ").append(rs.getString("receiverName") != null ? rs.getString("receiverName") : "NULL").append(", ");
                    shipmentInfo.append("receiverAddress: ").append(rs.getString("receiverAddress") != null ? rs.getString("receiverAddress") : "NULL").append(", ");
                    shipmentInfo.append("receiverCity: ").append(rs.getString("receiverCity") != null ? rs.getString("receiverCity") : "NULL").append(", ");
                    shipmentInfo.append("receiverState: ").append(rs.getString("receiverState") != null ? rs.getString("receiverState") : "NULL").append(", ");
                    shipmentInfo.append("receiverZipCode: ").append(rs.getString("receiverZipCode")).append(", ");
                    shipmentInfo.append("receiverContactNumber: ").append(rs.getString("receiverContactNumber")).append(", ");
                    shipmentInfo.append("weight: ").append(rs.getString("weight")).append(", ");
                    shipmentInfo.append("length: ").append(rs.getString("length") != null ? rs.getString("length") : "NULL").append(", ");
                    shipmentInfo.append("width: ").append(rs.getString("width")).append(", ");
                    shipmentInfo.append("height: ").append(rs.getString("height")).append(", ");
                    shipmentInfo.append("shipmentType: ").append(rs.getString("shipmentType") != null ? rs.getString("shipmentType") : "NULL").append(", ");
                    shipmentInfo.append("shipmentStatus: ").append(rs.getString("shipmentStatus")).append(", ");
                    shipmentInfo.append("shipmentDate: ").append(rs.getString("shipmentDate") != null ? rs.getString("shipmentDate") : "NULL").append(", ");
                    shipmentInfo.append("deliveryDate: ").append(rs.getString("deliveryDate") != null ? rs.getString("deliveryDate") : "NULL").append(", ");
                    shipmentInfo.append("deliveryTime: ").append(rs.getString("deliveryTime") != null ? rs.getString("deliveryTime") : "NULL").append(", ");
                    shipmentInfo.append("deliveryAddress: ").append(rs.getString("deliveryAddress") != null ? rs.getString("deliveryAddress") : "NULL").append(", ");
                    shipmentInfo.append("deliveryCity: ").append(rs.getString("deliveryCity") != null ? rs.getString("deliveryCity") : "NULL").append(", ");
                    shipmentInfo.append("deliveryState: ").append(rs.getString("deliveryState") != null ? rs.getString("deliveryState") : "NULL").append(", ");
                    shipmentInfo.append("deliveryZipCode: ").append(rs.getString("deliveryZipCode") != null ? rs.getString("deliveryZipCode") : "NULL").append(", ");
                    shipmentInfo.append("deliveryContactNumber: ").append(rs.getString("deliveryContactNumber") != null ? rs.getString("deliveryContactNumber") : "NULL").append(", ");
                    shipmentInfo.append("deliveryInstructions: ").append(rs.getString("deliveryInstructions") != null ? rs.getString("deliveryInstructions") : "NULL").append(", ");
                    shipmentInfo.append("deliveryStatus: ").append(rs.getString("deliveryStatus") != null ? rs.getString("deliveryStatus") : "NULL").append(", ");
                    shipmentInfo.append("senderID: ").append(rs.getString("senderID") != null ? rs.getString("senderID") : "NULL").append(", "); 
                // Add the shipmentInfo string to the shipmentsList
                shipmentsList.add(shipmentInfo.toString());
                
            }
            
            
        }catch(SQLException e){
            logger.log(Level.SEVERE, "Error: " + e.getMessage());
        }finally{
            try{
                if(rs != null){
                    rs.close();
                }
                if(stmt != null){
                    stmt.close();
                }
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                logger.log(Level.SEVERE, "Error: " + e.getMessage());
            }
        }
        return shipmentsList;

    }

    public int getUserByEmail(String email){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        //boolean userExists = false;
        logger.info("Getting user by email"+ " " + email + "...");
        int userID = -1;
        try{
            conn = DriverManager.getConnection(USER_DB_URL);
            logger.log(Level.INFO, "Connection to database established");
            stmt = conn.prepareStatement("SELECT * FROM users WHERE email = ?");
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            logger.log(Level.INFO, "Executing SQL statement: " + stmt.toString());
            if(rs.next()){
                userID = rs.getInt("id");
                logger.log(Level.INFO, "User found with ID: " + userID);
                return userID;
                //userExists = true;
            }
        }catch(SQLException e){
            logger.log(Level.SEVERE, "Error: " + e.getMessage());
        }finally{
            try{
                if(rs != null){
                    rs.close();
                }
                if(stmt != null){
                    stmt.close();
                }
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                logger.log(Level.SEVERE, "Error: " + e.getMessage());
            }
        }
        return userID;
    }


    public boolean createUser(String username, String password, String accountType, String authToken){
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean created = false;
        try{
            conn = DriverManager.getConnection(USER_DB_URL);
            logger.log(Level.INFO, "Connection to database established");
            stmt = conn.prepareStatement("INSERT INTO users (username, password, accountType, authToken) VALUES (?, ?, ?, ?)");
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, accountType);
            stmt.setString(4, authToken);
        
            stmt.executeUpdate();
            logger.log(Level.INFO, "Executing SQL statement: " + stmt.toString());
            created = true;
        }catch(SQLException e){
            logger.log(Level.SEVERE, "Error: " + e.getMessage());
        }finally{
            try{
                if(stmt != null){
                    stmt.close();
                }
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                logger.log(Level.SEVERE, "Error: " + e.getMessage());
            }
        }
        return created;
    }

    public boolean deleteUser(int id, String authToken){
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean deleted = false;
        try{
            conn = DriverManager.getConnection(USER_DB_URL);
            logger.log(Level.INFO, "Connection to database established");
            stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
            logger.log(Level.INFO, "Executing SQL statement: " + stmt.toString());
            deleted = true;
        }catch(SQLException e){
            logger.log(Level.SEVERE, "Error: " + e.getMessage());
        }finally{
            try{
                if(stmt != null){
                    stmt.close();
                }
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                logger.log(Level.SEVERE, "Error: " + e.getMessage());
            }
        }
        return deleted;
    }

    public boolean getUserByUsername(String username){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean found = false;
        try{
            conn = DriverManager.getConnection(USER_DB_URL);
            logger.log(Level.INFO, "Connection to database established");
            stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            logger.log(Level.INFO, "Executing SQL statement: " + stmt.toString());
            if(rs.next()){
                found = true;
            }
        }catch(SQLException e){
            logger.log(Level.SEVERE, "Error: " + e.getMessage());
        }finally{
            try{
                if(rs != null){
                    rs.close();
                }
                if(stmt != null){
                    stmt.close();
                }
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                logger.log(Level.SEVERE, "Error: " + e.getMessage());
            }
        }
        return found;
    }

    // Get all shipments for a user by userID that are in the cancelled state
    public List <String> gettAllcancelledShipList(int userID){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List <String> shipmentsList = new ArrayList <String>();
        try{
            conn = DriverManager.getConnection(SHIPMENT_DB_URL);
            logger.log(Level.INFO, "Connection to database established");
            if(userID == -1){
                logger.log(Level.INFO, "Admin user");
                stmt = conn.prepareStatement("SELECT * FROM shipments WHERE shipmentStatus = CANCELLED");
            }
            else{
                logger.log(Level.INFO, "Regular user");
                stmt = conn.prepareStatement("SELECT * FROM shipments WHERE  userID = ? and shipmentStatus = CANCELLED");
                stmt.setInt(1, userID);
            }
            //stmt = conn.prepareStatement("SELECT * FROM shipments WHERE  userID = ? and shipmentStatus = CANCELLED");
            stmt.setInt(1, userID);
            rs = stmt.executeQuery();
            logger.log(Level.INFO, "Executing SQL statement: " + stmt.toString());
            while(rs.next()){
                StringBuilder shipmentInfo = new StringBuilder();
    
                // Append the ResultSet (rs) values to the shipmentInfo string
                shipmentInfo.append("id: ").append(rs.getInt("id")).append(", ");
                shipmentInfo.append("senderName: ").append(rs.getString("senderName")).append(", ");
                shipmentInfo.append("senderAddress: ").append(rs.getString("senderAddress") != null ? rs.getString("senderAddress") : "NULL").append(", ");
                shipmentInfo.append("senderCity: ").append(rs.getString("senderCity")).append(", ");
                shipmentInfo.append("senderState: ").append(rs.getString("senderState") != null ? rs.getString("senderState") : "NULL").append(", ");
                shipmentInfo.append("senderZipCode: ").append(rs.getString("senderZipCode") != null ? rs.getString("senderZipCode") : "NULL").append(", ");
                shipmentInfo.append("senderContactNumber: ").append(rs.getString("senderContactNumber") != null ? rs.getString("senderContactNumber") : "NULL").append(", ");
                shipmentInfo.append("receiverName: ").append(rs.getString("receiverName") != null ? rs.getString("receiverName") : "NULL").append(", ");
                shipmentInfo.append("receiverAddress: ").append(rs.getString("receiverAddress") != null ? rs.getString("receiverAddress") : "NULL").append(", ");
                shipmentInfo.append("receiverCity: ").append(rs.getString("receiverCity") != null ? rs.getString("receiverCity") : "NULL").append(", ");
                shipmentInfo.append("receiverState: ").append(rs.getString("receiverState") != null ? rs.getString("receiverState") : "NULL").append(", ");
                shipmentInfo.append("receiverZipCode: ").append(rs.getString("receiverZipCode")).append(", ");
                shipmentInfo.append("receiverContactNumber: ").append(rs.getString("receiverContactNumber")).append(", ");
                shipmentInfo.append("weight: ").append(rs.getString("weight")).append(", ");
                shipmentInfo.append("length: ").append(rs.getString("length") != null ? rs.getString("length") : "NULL").append(", ");
                shipmentInfo.append("width: ").append(rs.getString("width")).append(", ");
                shipmentInfo.append("height: ").append(rs.getString("height")).append(", ");
                shipmentInfo.append("shipmentType: ").append(rs.getString("shipmentType") != null ? rs.getString("shipmentType") : "NULL").append(", ");
                shipmentInfo.append("shipmentStatus: ").append(rs.getString("shipmentStatus")).append(", ");
                shipmentInfo.append("shipmentDate: ").append(rs.getString("shipmentDate") != null ? rs.getString("shipmentDate") : "NULL").append(", ");
                shipmentInfo.append("deliveryDate: ").append(rs.getString("deliveryDate") != null ? rs.getString("deliveryDate") : "NULL").append(", ");
                shipmentInfo.append("deliveryTime: ").append(rs.getString("deliveryTime") != null ? rs.getString("deliveryTime") : "NULL").append(", ");
                shipmentInfo.append("deliveryAddress: ").append(rs.getString("deliveryAddress") != null ? rs.getString("deliveryAddress") : "NULL").append(", ");
                shipmentInfo.append("deliveryCity: ").append(rs.getString("deliveryCity") != null ? rs.getString("deliveryCity") : "NULL").append(", ");
                shipmentInfo.append("deliveryState: ").append(rs.getString("deliveryState") != null ? rs.getString("deliveryState") : "NULL").append(", ");
                shipmentInfo.append("deliveryZipCode: ").append(rs.getString("deliveryZipCode") != null ? rs.getString("deliveryZipCode") : "NULL").append(", ");
                shipmentInfo.append("deliveryContactNumber: ").append(rs.getString("deliveryContactNumber") != null ? rs.getString("deliveryContactNumber") : "NULL").append(", ");
                shipmentInfo.append("deliveryInstructions: ").append(rs.getString("deliveryInstructions") != null ? rs.getString("deliveryInstructions") : "NULL").append(", ");
                shipmentInfo.append("deliveryStatus: ").append(rs.getString("deliveryStatus") != null ? rs.getString("deliveryStatus") : "NULL").append(", ");
                shipmentInfo.append("senderID: ").append(rs.getString("senderID") != null ? rs.getString("senderID") : "NULL").append(", ");
                
                shipmentsList.add(shipmentInfo.toString());
            }
            
        }catch(SQLException e){
            logger.log(Level.SEVERE, "Error: " + e.getMessage());
        }finally{
            try{
                if(rs != null){
                    rs.close();
                }
                if(stmt != null){
                    stmt.close();
                }
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                logger.log(Level.SEVERE, "Error: " + e.getMessage());
            }
        }
        return shipmentsList;
    }


    
    // TODO: update stmt to get all shipments with status = IN TRANSIT, RECEIVED, DELIVERED
    public List <String> getAllCurrentShipments(int userID){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List <String> shipmentsList = new ArrayList <String>();
        try{
            conn = DriverManager.getConnection(SHIPMENT_DB_URL);
            logger.log(Level.INFO, "Connection to database established");
            stmt = conn.prepareStatement("SELECT * FROM shipments WHERE senderID = ? AND (shipmentStatus = 'IN_TRANSIT' OR shipmentStatus = 'RECEIVED' OR shipmentStatus = 'DELIVERED')");
            stmt.setInt(1, userID);

            rs = stmt.executeQuery();
            logger.log(Level.INFO, "Executing SQL statement: " + stmt.toString());
            while(rs.next()){
                StringBuilder shipmentInfo = new StringBuilder();
    
                // Append the ResultSet (rs) values to the shipmentInfo string
                shipmentInfo.append("id: ").append(rs.getInt("id")).append(", ");
                shipmentInfo.append("senderName: ").append(rs.getString("senderName")).append(", ");
                shipmentInfo.append("senderAddress: ").append(rs.getString("senderAddress") != null ? rs.getString("senderAddress") : "NULL").append(", ");
                shipmentInfo.append("senderCity: ").append(rs.getString("senderCity")).append(", ");
                shipmentInfo.append("senderState: ").append(rs.getString("senderState") != null ? rs.getString("senderState") : "NULL").append(", ");
                shipmentInfo.append("senderZipCode: ").append(rs.getString("senderZipCode") != null ? rs.getString("senderZipCode") : "NULL").append(", ");
                shipmentInfo.append("senderContactNumber: ").append(rs.getString("senderContactNumber") != null ? rs.getString("senderContactNumber") : "NULL").append(", ");
                shipmentInfo.append("receiverName: ").append(rs.getString("receiverName") != null ? rs.getString("receiverName") : "NULL").append(", ");
                shipmentInfo.append("receiverAddress: ").append(rs.getString("receiverAddress") != null ? rs.getString("receiverAddress") : "NULL").append(", ");
                shipmentInfo.append("receiverCity: ").append(rs.getString("receiverCity") != null ? rs.getString("receiverCity") : "NULL").append(", ");
                shipmentInfo.append("receiverState: ").append(rs.getString("receiverState") != null ? rs.getString("receiverState") : "NULL").append(", ");
                shipmentInfo.append("receiverZipCode: ").append(rs.getString("receiverZipCode")).append(", ");
                shipmentInfo.append("receiverContactNumber: ").append(rs.getString("receiverContactNumber")).append(", ");
                shipmentInfo.append("weight: ").append(rs.getString("weight")).append(", ");
                shipmentInfo.append("length: ").append(rs.getString("length") != null ? rs.getString("length") : "NULL").append(", ");
                shipmentInfo.append("width: ").append(rs.getString("width")).append(", ");
                shipmentInfo.append("height: ").append(rs.getString("height")).append(", ");
                shipmentInfo.append("shipmentType: ").append(rs.getString("shipmentType") != null ? rs.getString("shipmentType") : "NULL").append(", ");
                shipmentInfo.append("shipmentStatus: ").append(rs.getString("shipmentStatus")).append(", ");
                shipmentInfo.append("shipmentDate: ").append(rs.getString("shipmentDate") != null ? rs.getString("shipmentDate") : "NULL").append(", ");
                shipmentInfo.append("deliveryDate: ").append(rs.getString("deliveryDate") != null ? rs.getString("deliveryDate") : "NULL").append(", ");
                shipmentInfo.append("deliveryTime: ").append(rs.getString("deliveryTime") != null ? rs.getString("deliveryTime") : "NULL").append(", ");
                shipmentInfo.append("deliveryAddress: ").append(rs.getString("deliveryAddress") != null ? rs.getString("deliveryAddress") : "NULL").append(", ");
                shipmentInfo.append("deliveryCity: ").append(rs.getString("deliveryCity") != null ? rs.getString("deliveryCity") : "NULL").append(", ");
                shipmentInfo.append("deliveryState: ").append(rs.getString("deliveryState") != null ? rs.getString("deliveryState") : "NULL").append(", ");
                shipmentInfo.append("deliveryZipCode: ").append(rs.getString("deliveryZipCode") != null ? rs.getString("deliveryZipCode") : "NULL").append(", ");
                shipmentInfo.append("deliveryContactNumber: ").append(rs.getString("deliveryContactNumber") != null ? rs.getString("deliveryContactNumber") : "NULL").append(", ");
                shipmentInfo.append("deliveryInstructions: ").append(rs.getString("deliveryInstructions") != null ? rs.getString("deliveryInstructions") : "NULL").append(", ");
                shipmentInfo.append("deliveryStatus: ").append(rs.getString("deliveryStatus") != null ? rs.getString("deliveryStatus") : "NULL").append(", ");
                shipmentInfo.append("senderID: ").append(rs.getString("senderID") != null ? rs.getString("senderID") : "NULL").append(", ");
                
                shipmentsList.add(shipmentInfo.toString());
            }
        }catch(SQLException e){
            logger.log(Level.SEVERE, "Error: " + e.getMessage());
        }finally{
            try{
                if(rs != null){
                    rs.close();
                }
                if(stmt != null){
                    stmt.close();
                }
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                logger.log(Level.SEVERE, "Error: " + e.getMessage());
            }
        }
        return shipmentsList;
    }

    public boolean deleteShipment(int id){
        //TODO: implement deleteShipment method to delete a shipment from the database with the given id and authToken
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean deleted = false;
        try{
            conn = DriverManager.getConnection(SHIPMENT_DB_URL);
            logger.log(Level.INFO, "Connection to database established");
            stmt = conn.prepareStatement("DELETE FROM shipments WHERE id = ?");
            stmt.setInt(1, id);
            if(stmt.executeUpdate() > 0){
                deleted = true;
                logger.log(Level.INFO, "Statement is null for" + " " + id);         
            }else{
               logger.log(Level.INFO, "Executing SQL statement: " + stmt.toString());
                
              
            }
            
            
        }catch(SQLException e){
            logger.log(Level.SEVERE, "Error: " + e.getMessage());
        }finally{
            try{
                if(stmt != null){
                    stmt.close();
                }
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                logger.log(Level.SEVERE, "Error: " + e.getMessage());
            }
        }
        return deleted;
    }


    //NOTE, modify such that it returns a list of shipments that are completed
    public List<String> getShipmentsByUserId(int userID, String authToken) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<String> shipmentsList = new ArrayList<>();
    
        try {
            // Open database connection
            conn = DriverManager.getConnection(SHIPMENT_DB_URL);
            logger.log(Level.INFO, "Connection to database established");
    
            // Prepare SQL statement
            stmt = conn.prepareStatement("SELECT * FROM shipments where senderID = ?");
            stmt.setInt(1, userID);
    
            // Execute SQL statement and get result set
            rs = stmt.executeQuery();
            logger.log(Level.INFO, "Executing SQL statement: " + stmt.toString());
    
            // Iterate through result set and add shipments to list
            while (rs.next()) {
                StringBuilder shipmentInfo = new StringBuilder();
    
                // Append the ResultSet (rs) values to the shipmentInfo string
                shipmentInfo.append("id: ").append(rs.getInt("id")).append(", ");
                shipmentInfo.append("senderName: ").append(rs.getString("senderName")).append(", ");
                shipmentInfo.append("senderAddress: ").append(rs.getString("senderAddress") != null ? rs.getString("senderAddress") : "NULL").append(", ");
                shipmentInfo.append("senderCity: ").append(rs.getString("senderCity")).append(", ");
                shipmentInfo.append("senderState: ").append(rs.getString("senderState") != null ? rs.getString("senderState") : "NULL").append(", ");
                shipmentInfo.append("senderZipCode: ").append(rs.getString("senderZipCode") != null ? rs.getString("senderZipCode") : "NULL").append(", ");
                shipmentInfo.append("senderContactNumber: ").append(rs.getString("senderContactNumber") != null ? rs.getString("senderContactNumber") : "NULL").append(", ");
                shipmentInfo.append("receiverName: ").append(rs.getString("receiverName") != null ? rs.getString("receiverName") : "NULL").append(", ");
                shipmentInfo.append("receiverAddress: ").append(rs.getString("receiverAddress") != null ? rs.getString("receiverAddress") : "NULL").append(", ");
                shipmentInfo.append("receiverCity: ").append(rs.getString("receiverCity") != null ? rs.getString("receiverCity") : "NULL").append(", ");
                shipmentInfo.append("receiverState: ").append(rs.getString("receiverState") != null ? rs.getString("receiverState") : "NULL").append(", ");
                shipmentInfo.append("receiverZipCode: ").append(rs.getString("receiverZipCode")).append(", ");
                shipmentInfo.append("receiverContactNumber: ").append(rs.getString("receiverContactNumber")).append(", ");
                shipmentInfo.append("weight: ").append(rs.getString("weight")).append(", ");
                shipmentInfo.append("length: ").append(rs.getString("length") != null ? rs.getString("length") : "NULL").append(", ");
                shipmentInfo.append("width: ").append(rs.getString("width")).append(", ");
                shipmentInfo.append("height: ").append(rs.getString("height")).append(", ");
                shipmentInfo.append("shipmentType: ").append(rs.getString("shipmentType") != null ? rs.getString("shipmentType") : "NULL").append(", ");
                shipmentInfo.append("shipmentStatus: ").append(rs.getString("shipmentStatus")).append(", ");
                shipmentInfo.append("shipmentDate: ").append(rs.getString("shipmentDate") != null ? rs.getString("shipmentDate") : "NULL").append(", ");
                shipmentInfo.append("deliveryDate: ").append(rs.getString("deliveryDate") != null ? rs.getString("deliveryDate") : "NULL").append(", ");
                shipmentInfo.append("deliveryTime: ").append(rs.getString("deliveryTime") != null ? rs.getString("deliveryTime") : "NULL").append(", ");
                shipmentInfo.append("deliveryAddress: ").append(rs.getString("deliveryAddress") != null ? rs.getString("deliveryAddress") : "NULL").append(", ");
                shipmentInfo.append("deliveryCity: ").append(rs.getString("deliveryCity") != null ? rs.getString("deliveryCity") : "NULL").append(", ");
                shipmentInfo.append("deliveryState: ").append(rs.getString("deliveryState") != null ? rs.getString("deliveryState") : "NULL").append(", ");
                shipmentInfo.append("deliveryZipCode: ").append(rs.getString("deliveryZipCode") != null ? rs.getString("deliveryZipCode") : "NULL").append(", ");
                shipmentInfo.append("deliveryContactNumber: ").append(rs.getString("deliveryContactNumber") != null ? rs.getString("deliveryContactNumber") : "NULL").append(", ");
                shipmentInfo.append("deliveryInstructions: ").append(rs.getString("deliveryInstructions") != null ? rs.getString("deliveryInstructions") : "NULL").append(", ");
                shipmentInfo.append("deliveryStatus: ").append(rs.getString("deliveryStatus") != null ? rs.getString("deliveryStatus") : "NULL").append(", ");
                shipmentInfo.append("senderID: ").append(rs.getString("senderID") != null ? rs.getString("senderID") : "NULL").append(", ");

                
    
                // Add the shipmentInfo string to the shipmentsList
                shipmentsList.add(shipmentInfo.toString());
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error: " + e.getMessage());
            }
        }
        return shipmentsList;
    }
    

    
    public Shipment getShipmentById(int id){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Shipment shipment = null;
       // List<String> shipmentsList = new ArrayList<String>();
        try {
            // Open database connection
            conn = DriverManager.getConnection(SHIPMENT_DB_URL);
            logger.log(Level.INFO, "Connection to database established");
            
            // Prepare SQL statement
            stmt = conn.prepareStatement("SELECT * FROM shipments where id = ?");
            stmt.setInt(1, id);
            
            // Execute SQL statement and get result set
            rs = stmt.executeQuery();
            logger.log(Level.INFO, "Executing SQL statement: " + stmt.toString());
            // Extract user information from result set
            if (rs.next()) {
                int shipmentId = rs.getInt("id");
                String senderName = rs.getString("senderName");
                String senderAddress = rs.getString("senderAddress");
                String senderCity = rs.getString("senderCity");
                String senderState = rs.getString("senderState");
                String senderZipCode = rs.getString("senderZipCode");
                String senderContactNumber = rs.getString("senderContactNumber");
                String receiverName = rs.getString("receiverName");
                String receiverAddress = rs.getString("receiverAddress");
                String receiverCity = rs.getString("receiverCity");
                String receiverState = rs.getString("receiverState");
                String receiverZipCode = rs.getString("receiverZipCode");
                String receiverContactNumber = rs.getString("receiverContactNumber");
                String weight = rs.getString("weight");
                String length = rs.getString("length");
                String width = rs.getString("width");
                String height = rs.getString("height");
                String shipmentType = rs.getString("shipmentType");
                String shipmentStatus = rs.getString("shipmentStatus");
                LocalDate shipmentDate = rs.getDate("shipmentDate") != null ? rs.getDate("shipmentDate").toLocalDate() : LocalDate.now();
                LocalDate deliveryDate = rs.getDate("deliveryDate") != null ? rs.getDate("deliveryDate").toLocalDate() : null;
                String deliveryTime = rs.getString("deliveryTime");
                String deliveryAddress = rs.getString("deliveryAddress");
                String deliveryCity = rs.getString("deliveryCity");
                String deliveryState = rs.getString("deliveryState");
                String deliveryZipCode = rs.getString("deliveryZipCode");
                String deliveryContactNumber = rs.getString("deliveryContactNumber");
                String deliveryInstructions = rs.getString("deliveryInstructions");
                String deliveryStatus = rs.getString("deliveryStatus");
                String senderID = rs.getString("senderID");
                String authToken = rs.getString("senderAUTH");

                shipment = new Shipment(shipmentId, senderName, senderAddress, 
                senderCity, senderState, senderZipCode, senderContactNumber, receiverName, 
                receiverAddress, receiverCity, receiverState, receiverZipCode, receiverContactNumber, 
                weight, length, width, height, shipmentType, shipmentStatus, shipmentDate, deliveryDate,
                 deliveryTime, deliveryAddress, deliveryCity, deliveryState, deliveryZipCode, deliveryContactNumber, 
                 deliveryInstructions, deliveryStatus, senderID, authToken); 

                
        } 
    }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Error: " + e.getMessage()); 
        } finally {
            // Close result set, statement, and connection
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error: " + e.getMessage());
            }
        }
        
       
        
        
        return shipment;
    }





    public List<String> getShipmentById(int id, String some_String){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        //Shipment shipment = null;
        List<String> shipments = new ArrayList<String>();
        try {
            // Open database connection
            conn = DriverManager.getConnection(SHIPMENT_DB_URL);
            logger.log(Level.INFO, "Connection to database established");
            
            // Prepare SQL statement
            stmt = conn.prepareStatement("SELECT * FROM shipments where id = ?");
            stmt.setInt(1, id);
            
            // Execute SQL statement and get result set
            rs = stmt.executeQuery();
            logger.log(Level.INFO, "Executing SQL statement: " + stmt.toString());
            // Extract user information from result set
            while (rs.next()) {
                StringBuilder shipmentInfo = new StringBuilder();
    
                // Append the ResultSet (rs) values to the shipmentInfo string
                // Example: shipmentInfo.append("id: ").append(rs.getInt("id")).append(", ");
               // Append the ResultSet (rs) values to the shipmentInfo string
            shipmentInfo.append("id: ").append(rs.getInt("id")).append(", ");
            shipmentInfo.append("senderName: ").append(rs.getString("senderName")).append(", ");
            shipmentInfo.append("senderAddress: ").append(rs.getString("senderAddress") != null ? rs.getString("senderAddress") : "NULL").append(", ");
            shipmentInfo.append("senderCity: ").append(rs.getString("senderCity")).append(", ");
            shipmentInfo.append("senderState: ").append(rs.getString("senderState") != null ? rs.getString("senderState") : "NULL").append(", ");
            shipmentInfo.append("senderZipCode: ").append(rs.getString("senderZipCode") != null ? rs.getString("senderZipCode") : "NULL").append(", ");
            shipmentInfo.append("senderContactNumber: ").append(rs.getString("senderContactNumber") != null ? rs.getString("senderContactNumber") : "NULL").append(", ");
            shipmentInfo.append("receiverName: ").append(rs.getString("receiverName") != null ? rs.getString("receiverName") : "NULL").append(", ");
            shipmentInfo.append("receiverAddress: ").append(rs.getString("receiverAddress") != null ? rs.getString("receiverAddress") : "NULL").append(", ");
            shipmentInfo.append("receiverCity: ").append(rs.getString("receiverCity") != null ? rs.getString("receiverCity") : "NULL").append(", ");
            shipmentInfo.append("receiverState: ").append(rs.getString("receiverState") != null ? rs.getString("receiverState") : "NULL").append(", ");
            shipmentInfo.append("receiverZipCode: ").append(rs.getString("receiverZipCode")).append(", ");
            shipmentInfo.append("receiverContactNumber: ").append(rs.getString("receiverContactNumber")).append(", ");
            shipmentInfo.append("weight: ").append(rs.getString("weight")).append(", ");
            shipmentInfo.append("length: ").append(rs.getString("length") != null ? rs.getString("length") : "NULL").append(", ");
            shipmentInfo.append("width: ").append(rs.getString("width")).append(", ");
            shipmentInfo.append("height: ").append(rs.getString("height")).append(", ");
            shipmentInfo.append("shipmentType: ").append(rs.getString("shipmentType") != null ? rs.getString("shipmentType") : "NULL").append(", ");
            shipmentInfo.append("shipmentStatus: ").append(rs.getString("shipmentStatus")).append(", ");
            shipmentInfo.append("shipmentDate: ").append(rs.getString("shipmentDate") != null ? rs.getString("shipmentDate") : "NULL").append(", ");
            shipmentInfo.append("deliveryDate: ").append(rs.getString("deliveryDate") != null ? rs.getString("deliveryDate") : "NULL").append(", ");
            shipmentInfo.append("deliveryTime: ").append(rs.getString("deliveryTime") != null ? rs.getString("deliveryTime") : "NULL").append(", ");
            shipmentInfo.append("deliveryAddress: ").append(rs.getString("deliveryAddress") != null ? rs.getString("deliveryAddress") : "NULL").append(", ");
            shipmentInfo.append("deliveryCity: ").append(rs.getString("deliveryCity") != null ? rs.getString("deliveryCity") : "NULL").append(", ");
            shipmentInfo.append("deliveryState: ").append(rs.getString("deliveryState") != null ? rs.getString("deliveryState") : "NULL").append(", ");
            shipmentInfo.append("deliveryZipCode: ").append(rs.getString("deliveryZipCode") != null ? rs.getString("deliveryZipCode") : "NULL").append(", ");
            shipmentInfo.append("deliveryContactNumber: ").append(rs.getString("deliveryContactNumber") != null ? rs.getString("deliveryContactNumber") : "NULL").append(", ");
            shipmentInfo.append("deliveryInstructions: ").append(rs.getString("deliveryInstructions") != null ? rs.getString("deliveryInstructions") : "NULL").append(", ");
            shipmentInfo.append("deliveryStatus: ").append(rs.getString("deliveryStatus") != null ? rs.getString("deliveryStatus") : "NULL").append(", ");
            shipmentInfo.append("senderID: ").append(rs.getString("senderID"));
            

                // ... add the rest of the fields
    
                // Add the shipmentInfo string to the list
                shipments.add(shipmentInfo.toString());
            }
    }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Error: " + e.getMessage()); 
        } finally {
            // Close result set, statement, and connection
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error: " + e.getMessage());
            }
        }
        
       
        
        
        return shipments;
    }




    public List<String> getAllShipmentsAdmin() {
        List<String> shipments = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
    
        try {
            conn = DriverManager.getConnection(SHIPMENT_DB_URL);
            stmt = conn.createStatement();
            String sql = "SELECT * FROM shipments";
            rs = stmt.executeQuery(sql);
    
            while (rs.next()) {
                StringBuilder shipmentInfo = new StringBuilder();
    
                // Append the ResultSet (rs) values to the shipmentInfo string
                // Example: shipmentInfo.append("id: ").append(rs.getInt("id")).append(", ");
               // Append the ResultSet (rs) values to the shipmentInfo string
            shipmentInfo.append("id: ").append(rs.getInt("id")).append(", ");
            shipmentInfo.append("senderName: ").append(rs.getString("senderName")).append(", ");
            shipmentInfo.append("senderAddress: ").append(rs.getString("senderAddress") != null ? rs.getString("senderAddress") : "NULL").append(", ");
            shipmentInfo.append("senderCity: ").append(rs.getString("senderCity")).append(", ");
            shipmentInfo.append("senderState: ").append(rs.getString("senderState") != null ? rs.getString("senderState") : "NULL").append(", ");
            shipmentInfo.append("senderZipCode: ").append(rs.getString("senderZipCode") != null ? rs.getString("senderZipCode") : "NULL").append(", ");
            shipmentInfo.append("senderContactNumber: ").append(rs.getString("senderContactNumber") != null ? rs.getString("senderContactNumber") : "NULL").append(", ");
            shipmentInfo.append("receiverName: ").append(rs.getString("receiverName") != null ? rs.getString("receiverName") : "NULL").append(", ");
            shipmentInfo.append("receiverAddress: ").append(rs.getString("receiverAddress") != null ? rs.getString("receiverAddress") : "NULL").append(", ");
            shipmentInfo.append("receiverCity: ").append(rs.getString("receiverCity") != null ? rs.getString("receiverCity") : "NULL").append(", ");
            shipmentInfo.append("receiverState: ").append(rs.getString("receiverState") != null ? rs.getString("receiverState") : "NULL").append(", ");
            shipmentInfo.append("receiverZipCode: ").append(rs.getString("receiverZipCode")).append(", ");
            shipmentInfo.append("receiverContactNumber: ").append(rs.getString("receiverContactNumber")).append(", ");
            shipmentInfo.append("weight: ").append(rs.getString("weight")).append(", ");
            shipmentInfo.append("length: ").append(rs.getString("length") != null ? rs.getString("length") : "NULL").append(", ");
            shipmentInfo.append("width: ").append(rs.getString("width")).append(", ");
            shipmentInfo.append("height: ").append(rs.getString("height")).append(", ");
            shipmentInfo.append("shipmentType: ").append(rs.getString("shipmentType") != null ? rs.getString("shipmentType") : "NULL").append(", ");
            shipmentInfo.append("shipmentStatus: ").append(rs.getString("shipmentStatus")).append(", ");
            shipmentInfo.append("shipmentDate: ").append(rs.getString("shipmentDate") != null ? rs.getString("shipmentDate") : "NULL").append(", ");
            shipmentInfo.append("deliveryDate: ").append(rs.getString("deliveryDate") != null ? rs.getString("deliveryDate") : "NULL").append(", ");
            shipmentInfo.append("deliveryTime: ").append(rs.getString("deliveryTime") != null ? rs.getString("deliveryTime") : "NULL").append(", ");
            shipmentInfo.append("deliveryAddress: ").append(rs.getString("deliveryAddress") != null ? rs.getString("deliveryAddress") : "NULL").append(", ");
            shipmentInfo.append("deliveryCity: ").append(rs.getString("deliveryCity") != null ? rs.getString("deliveryCity") : "NULL").append(", ");
            shipmentInfo.append("deliveryState: ").append(rs.getString("deliveryState") != null ? rs.getString("deliveryState") : "NULL").append(", ");
            shipmentInfo.append("deliveryZipCode: ").append(rs.getString("deliveryZipCode") != null ? rs.getString("deliveryZipCode") : "NULL").append(", ");
            shipmentInfo.append("deliveryContactNumber: ").append(rs.getString("deliveryContactNumber") != null ? rs.getString("deliveryContactNumber") : "NULL").append(", ");
            shipmentInfo.append("deliveryInstructions: ").append(rs.getString("deliveryInstructions") != null ? rs.getString("deliveryInstructions") : "NULL").append(", ");
            shipmentInfo.append("deliveryStatus: ").append(rs.getString("deliveryStatus") != null ? rs.getString("deliveryStatus") : "NULL").append(", ");
            shipmentInfo.append("senderID: ").append(rs.getString("senderID") != null ? rs.getString("senderID") : "NULL").append(", ");


                // ... add the rest of the fields
    
                // Add the shipmentInfo string to the list
                shipments.add(shipmentInfo.toString());
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    
        return shipments;
    }
    
    

    public User getUserByAuthToken(String authToken) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
    
        try {
            conn = DriverManager.getConnection(USER_DB_URL);
            logger.info("Getting user by auth token, connection established");
            
            // Check if an existing auth token exists for the user
            stmt = conn.prepareStatement("SELECT id FROM users WHERE authToken = ?");
            stmt.setString(1, authToken);
            rs = stmt.executeQuery();
            if(rs.next()) {
                int id = rs.getInt("id");
                return getUserById(id);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
         finally {
            try{
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        
        return null;
    }

    
     
     // Now its return true or false randomly
     public static boolean authenticateUser(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean success = false;

        // Connect to user database to find user and check if password matches

        try {
            conn = DriverManager.getConnection(USER_DB_URL);
            stmt = conn.prepareStatement("SELECT * FROM users WHERE email = ?");
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String dbPassword = rs.getString("password");
                logger.info("users password"+" "+dbPassword + " " + "provided password" + " " + password);
                if (dbPassword.equals(password)) {
                    success = true;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error: " + e.getMessage());
        } finally {
            // Close result set, statement, and connection
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error: " + e.getMessage());
            }
        }
       return success;
    }

    // Update the users password
    public boolean updatePassword(int userid, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection(USER_DB_URL);

            // Update the user's password
            stmt = conn.prepareStatement("UPDATE users SET password = ? WHERE id = ?");
            stmt.setString(1, password);
            stmt.setInt(2, userid);
            stmt.executeUpdate();
            success = true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }

        return success;
    }

    // Update the user's firstName, lastName, email, and authToken
    public boolean updateUser(int userid, String authToken, String firstName, String lastName, String email) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection(USER_DB_URL);

            // Update the user's firstName, lastName, email, and authToken
            stmt = conn.prepareStatement("UPDATE users SET firstName = ?, lastName = ?, email = ?, authToken = ? WHERE id = ?");
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, authToken);
            stmt.setInt(5, userid);
            stmt.executeUpdate();
            success = true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return success;
    }


    // Update user or adds authtoken to user
    public boolean updateUser(int userid, String authtoken) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection(USER_DB_URL);

            // Update the authToken for the user
            stmt = conn.prepareStatement("UPDATE users SET authToken = ? WHERE id = ?");
            stmt.setString(1, authtoken);
            stmt.setInt(2, userid);
            int rowsAffected = stmt.executeUpdate();
            success = rowsAffected > 0;
            return success;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            try{
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return success;
    }

    

    // Get users email address from the database for 2FA
    public String getEmail(String authToken) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String email = null;
    
        try {
            conn = DriverManager.getConnection(USER_DB_URL);
            
            // Get the email address for the user
            stmt = conn.prepareStatement("SELECT email FROM users WHERE authToken = ?");
            stmt.setString(1, authToken);
            rs = stmt.executeQuery();
            if (rs.next()) {
                email = rs.getString("email");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            try{
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return email;
    }


    // Generates auth token for user and inserts it into the database
    public static String generateAuthToken(int userID, String password) throws NoSuchAlgorithmException {
        
        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        
        // Combine the salt and password
        String saltedPassword = salt + password;
        
        // Hash the salted password using SHA-256
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassword = md.digest(saltedPassword.getBytes());
        
        // Combine the username, hashed password, and salt to create the auth token
        String authToken = userID + ":" + bytesToHex(hashedPassword) + ":" + bytesToHex(salt);
        
        DatabaseManager db = new DatabaseManager("Adding auth token to database for user " + userID);
        db.updateUser(userID, authToken);

        return authToken;
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }


   
    public String getAccountType(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String accountType = null;

        try {
            conn = DriverManager.getConnection(USER_DB_URL);
            stmt = conn.prepareStatement("SELECT accountType FROM users WHERE username = ?");
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                accountType = rs.getString("accountType");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            try{
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
            
        }

       return accountType;
    }

   public static void main(String args[]){

        DatabaseManager testing = new DatabaseManager(); 
        
        Random random = new Random();
        /* 
        User currentUser = testing.getUserById(1217784801);
        System.out.println(currentUser.getFirstName());
        System.out.println(currentUser.getLastName());
        System.out.println(currentUser.getEmail());
        System.out.println(currentUser.getAccountType());
        System.out.println(currentUser.getAuthToken());
        System.out.println(currentUser.getId());

   
       
    */
       
        // create a test user
        String firstName = "testing";
        String lastName = "some";
        String email = "fade@fade";
        String password = "secret";
        LocalDate dob = LocalDate.of(1990, 1, 1);
        String country = "USA";
        String zipCode = "12345";
        String contactNumber = "555-1234";
        AccountType accountType = AccountType.REGISTERED_USER;

        // add the user to the database
       // Random random = new Random();
        int randomNumber = random.nextInt(100000);
        // Generate the password hash
        String username = firstName + lastName + randomNumber;
        int hash = username.hashCode();
        int userId = Math.abs(hash);
         
      //  System.out.println("getCountries" + " " + testing.getCountries());


        // get user by auth token

       //logger.info("authenticate" +" "+" " + DatabaseManager.authenticateUser(email, password));
        //testing.addUser(userId,firstName, lastName, email, password, dob, country, zipCode, contactNumber, accountType.getValue(), "authToken");
       // System.out.println("get_user_by_auth_token" + " " + testing.getUserByAuthToken("notes"));

        testing.updateUser(1, username, firstName, lastName, email);
        /* 
       
         
        // add shipment to the database
        int shipmentId = 2929;
        String senderName = "firstNamelastName";
        String senderAddress = "123 Main St";
        String senderCity = "New York";
        String senderState = "NY";
        String senderZipCode = "10001";
        String senderContactNumber = "555-123-4567";
        String receiverName = "Jane Smith";
        String receiverAddress = "456 Elm St";
        String receiverCity = "Los Angeles";
        String receiverState = "CA";
        String receiverZipCode = "90001";
        String receiverContactNumber = "555-987-6543";
        String weight = "10";
        String length = "5";
        String width = "5";
        String height = "5";
        String shipmentType = "Standard";
        String shipmentStatus = "Pending";
        LocalDate shipmentDate = LocalDate.now();
        LocalDate deliveryDate = LocalDate.now(); 
        String deliveryTime = "10:00 AM";
        String deliveryAddress = "789 Oak St";
        String deliveryCity = "San Francisco";
        String deliveryState = "CA";
        String deliveryZipCode = "94102";
        String deliveryContactNumber = "555-111-2222";
        String deliveryInstructions = "Leave the package at the front door";
        String deliveryStatus = "Pending";
        String senderId = Integer.toString(1);
        String senderAuth = "authToken";

        testing.addShipment(shipmentId,senderName, senderAddress, senderCity, senderState, senderZipCode, senderContactNumber,
                   receiverName, receiverAddress, receiverCity, receiverState, receiverZipCode, receiverContactNumber,
                   weight, length, width, height, shipmentType, shipmentStatus, shipmentDate, deliveryDate, deliveryTime,
                   deliveryAddress, deliveryCity, deliveryState, deliveryZipCode, deliveryContactNumber, deliveryInstructions,
                   deliveryStatus, senderId, senderAuth);
        
                   testing.addShipment(shipmentId,senderName, senderAddress, senderCity, senderState, senderZipCode, senderContactNumber,
                   receiverName, receiverAddress, receiverCity, receiverState, receiverZipCode, receiverContactNumber,
                   weight, length, width, height, shipmentType, "COMPLETED", shipmentDate, deliveryDate, deliveryTime,
                   deliveryAddress, deliveryCity, deliveryState, deliveryZipCode, deliveryContactNumber, deliveryInstructions,
                   deliveryStatus, senderId, senderAuth);

                   testing.addShipment(shipmentId,senderName, senderAddress, senderCity, senderState, senderZipCode, senderContactNumber,
                   receiverName, receiverAddress, receiverCity, receiverState, receiverZipCode, receiverContactNumber,
                   weight, length, width, height, shipmentType, "IN_TRANSIT", shipmentDate, deliveryDate, deliveryTime,
                   deliveryAddress, deliveryCity, deliveryState, deliveryZipCode, deliveryContactNumber, deliveryInstructions,
                   deliveryStatus, senderId, senderAuth);
                   testing.addShipment(shipmentId,senderName, senderAddress, senderCity, senderState, senderZipCode, senderContactNumber,
                   receiverName, receiverAddress, receiverCity, receiverState, receiverZipCode, receiverContactNumber,
                   weight, length, width, height, shipmentType, "CREATED", shipmentDate, deliveryDate, deliveryTime,
                   deliveryAddress, deliveryCity, deliveryState, deliveryZipCode, deliveryContactNumber, deliveryInstructions,
                   deliveryStatus, senderId, senderAuth);

                   testing.addShipment(shipmentId,senderName, senderAddress, senderCity, senderState, senderZipCode, senderContactNumber,
                   receiverName, receiverAddress, receiverCity, receiverState, receiverZipCode, receiverContactNumber,
                   weight, length, width, height, shipmentType, "CANCELLED", shipmentDate, deliveryDate, deliveryTime,
                   deliveryAddress, deliveryCity, deliveryState, deliveryZipCode, deliveryContactNumber, deliveryInstructions,
                   deliveryStatus, senderId, senderAuth);


       

    */
      
       

        

    }



   
}