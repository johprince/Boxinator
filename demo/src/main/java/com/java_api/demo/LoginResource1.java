package com.java_api.demo;


import com.warrenstrange.googleauth.GoogleAuthenticator;
import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentHashMap;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;

@RestController
public class LoginResource1 {

    public static final int MAX_LOGIN_ATTEMPTS = 3;
    public static final Map<String, Integer> LOGIN_ATTEMPTS = new HashMap<>();
    private static final PolicyFactory POLICY = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
    private Logger logger = Logger.getLogger(LoginResource1.class.getName());
    private static final long T0 = 0L;
    private static final long X = 30L;
    private ConcurrentHashMap<String, GoogleAuthenticatorKey> userKeys = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, LocalDateTime> userKeyTimestamps = new ConcurrentHashMap<>();

    private DatabaseManager dbDatabaseManager = new DatabaseManager();
    private String passString;
    private int userID;

    private String test_secret;
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateUser(@RequestBody LoginData loginData, @RequestHeader("X-Forwarded-For") Optional<String> xForwardedFor) {
        // Extract the credentials from the request body
        //String username =  sanitize(loginData.getUsername()); // email
        String username =  loginData.getUsername().replaceAll("[^a-zA-Z0-9@._-]", ""); // sanitize email with regeg expression
        String password = sanitize(loginData.getPassword()); // password
        passString = password;
       
    
        // Check if the user has exceeded the maximum login attempts
        String ipAddress = getIpAddress(xForwardedFor);
        
        if (ipAddress == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer attempts = LOGIN_ATTEMPTS.get(ipAddress);
        logger.info("attempts: " + attempts + " by this ip");
        if (attempts != null && attempts >= MAX_LOGIN_ATTEMPTS) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
    
        // Authenticate the user                                // Sanatizing user input currupts the email address and 
                                                                // posible password
        boolean authenticated = DatabaseManager.authenticateUser(username, password);
        logger.info("authenticated: " + authenticated);
        
        //authenticated = true; // REMOVE
        if (!authenticated) {
            // Increment the number of login attempts for the current IP address
            if (attempts == null) {
                attempts = 0;
            }
            LOGIN_ATTEMPTS.put(ipAddress, attempts + 1);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

            userID =  dbDatabaseManager.getUserByEmail(username);


            // Generate a secret key for the user
            GoogleAuthenticator gAuth_test = new GoogleAuthenticator();
            GoogleAuthenticatorKey key_test = gAuth_test.createCredentials();
            String yourOwnSecret = key_test.getKey();
            test_secret = yourOwnSecret;





        
                
            // Generate a QR code that the user can scan to set up Google Authenticator
            String issuer = "My App"; 
            String accountName = username;
            // Generate a QR code URL for the Google Charts API
            String test_qrCodeUrl = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(issuer, accountName, key_test);
          
            // generate a QR code image from the URL
            ByteArrayOutputStream qrCodeStream = new ByteArrayOutputStream();
            try {
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrCodeWriter.encode(test_qrCodeUrl, BarcodeFormat.QR_CODE, 250, 250);
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", qrCodeStream);
            } catch (WriterException | IOException e) {
                logger.severe("Error generating QR code: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
                    
            // convert the QR code image to a data URL
            byte[] qrCodeBytes = qrCodeStream.toByteArray();
            String qrCodeDataUrl = "data:image/png;base64," + Base64.getEncoder().encodeToString(qrCodeBytes);
                    
            // Generate the OTP for the user
            int test_otp;
            
            try{
                // Test, WORKS
                GoogleAuthenticator gAuths = new GoogleAuthenticator();
                test_otp = gAuths.getTotpPassword(yourOwnSecret);
                
                
                //otp = Integer.toString(gAuth.getTotpPassword(key.getKey()));
            }catch(Exception e){
                logger.severe("Error generating OTP: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
             
            // Test logg otp, WOKRS
            String otp=Integer.toString(test_otp);
           

            

            // Return a successful response with the user's QR code and OTP
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("qr_code_data_url", qrCodeDataUrl);
            //responseMap.put("otp", otp);
            return ResponseEntity.ok(responseMap);
            }

   
    
    // Get the IP address of the client making the request
    private String getIpAddress(Optional<String> xForwardedFor) {
        String ipAddress = xForwardedFor.orElse(null);
        if (ipAddress == null) {
            ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = sra.getRequest();
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

   


@PostMapping(value = "/verify_otp", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpData verifyOtpData) {    
    String otp = sanitize(verifyOtpData.getOtp());
    logger.info("otp ==" +" "+otp );
    

    GoogleAuthenticator gAuth = new GoogleAuthenticator();
    boolean isOtpValid = gAuth.authorize(test_secret, Integer.parseInt(verifyOtpData.getOtp()));
    logger.info("is otp valid" +" "+ isOtpValid);

    
    if (!isOtpValid) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // If the OTP is valid, generate a new auth token and return it to the client
     
     User current_user = dbDatabaseManager.getUserById(userID);
     String authToken;
     try{
     authToken = DatabaseManager.generateAuthToken(current_user.getId(), passString);
       
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("auth_token", authToken);
        responseMap.put("User_id", Integer.toString(current_user.getId()));
        return ResponseEntity.ok(responseMap);
    }catch(Exception e){
        logger.severe("Error generating auth token: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}



    // Data class representing the request body for the /verify_otp endpoint
    public static class VerifyOtpData {
        private String username;
        private String otp;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }
    }


    // Data class representing the request body for the /login endpoint
    public static class LoginData {
        private String username;
        private String password;
    
        public String getUsername() {
            return username;
        }
    
        public void setUsername(String username) {
            this.username = username;
        }
    
        public String getPassword() {
            return password;
        }
    
        public void setPassword(String password) {
            this.password = password;
        }
    }



    private static byte[] hmacSha1(byte[] keyBytes, byte[] text) throws GeneralSecurityException {
        Mac hmac = Mac.getInstance("HmacSHA1");
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "RAW");
        hmac.init(keySpec);
        return hmac.doFinal(text);
    }

    private static int truncate(byte[] hash) {
        int offset = hash[hash.length - 1] & 0xF;
        return ((hash[offset] & 0x7F) << 24) |
                ((hash[offset + 1] & 0xFF) << 16) |
                ((hash[offset + 2] & 0xFF) << 8) |
                (hash[offset + 3] & 0xFF);
    }

    public static String generateTotpPassword(byte[] secretKey) throws GeneralSecurityException {
        byte[] key = Base64.getDecoder().decode(secretKey);
        long currentTimeMillis = System.currentTimeMillis();
        long currentTimeSeconds = currentTimeMillis / 1000;
        long timeStepSeconds = X;
        long T = (currentTimeSeconds - T0) / timeStepSeconds;
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(T);
        byte[] timeBytes = buffer.array();
        byte[] hmac = hmacSha1(key, timeBytes);
        //int offset = hmac[hmac.length - 1] & 0xF;
        int truncatedHash = truncate(hmac);
        int otp = truncatedHash % 1000000;
        return String.format("%06d", otp);
    }

    @Scheduled(fixedRate = 60000) // Run every minute
    public void cleanupUserKeys() {
        LocalDateTime currentTime = LocalDateTime.now();
        Iterator<Entry<String, LocalDateTime>> iterator = userKeyTimestamps.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<String, LocalDateTime> entry = iterator.next();
            if (entry.getValue().plusMinutes(5).isBefore(currentTime)) { // Remove keys older than 5 minutes
                iterator.remove();
                userKeys.remove(entry.getKey());
            }
        }
    }


    public static String sanitize(String input) {
        return POLICY.sanitize(input);
    }

}