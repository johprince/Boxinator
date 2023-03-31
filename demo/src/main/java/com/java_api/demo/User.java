package com.java_api.demo;


import java.time.LocalDate;


public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
    private String countryOfResidence;
    private String zipCode;
    private String contactNumber;
    private AccountType accountType;
    private String authToken;
    

    public User(int id, String firstName, String lastName, String email, String password,
                LocalDate dateOfBirth, String countryOfResidence, String zipCode, String contactNumber,
                AccountType accountType, String authToken) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.countryOfResidence = countryOfResidence;
        this.zipCode = zipCode;
        this.contactNumber = contactNumber;
        this.accountType = accountType;
        this.authToken = authToken;
    
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCountryOfResidence() {
        return countryOfResidence;
    }

    public void setCountryOfResidence(String countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

   public AccountType getAccountType() {
        return accountType;
    }

   /* public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }*/

    public void setAccountType(String accountType) {
        this.accountType = AccountType.fromValue(accountType);
    }

    // TODO: implement this to return auth token
    public String getAuthToken() {
        return authToken;
    }
    
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public enum AccountType {
        GUEST("GUEST"),
        REGISTERED_USER("REGISTERED_USER"),
        ADMINISTRATOR("ADMINISTRATOR");
    
        private final String value;
    
        AccountType(String value) {
            this.value = value;
        }
    
        public static AccountType fromValue(String value) {
            for (AccountType type : AccountType.values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid account type value: " + value);
        }
    
        public String getValue() {
            return value;
        }

    }
    
    
}
