package com.java_api.demo;


import java.sql.*;
import java.time.LocalDate;


public class Shipment {
    



    private int shipmentId;
    private String senderName;
    private String senderAddress;
    private String senderCity;
    private String senderState;
    private String senderZipCode;
    private String senderContactNumber;
    private String receiverName;
    private String receiverAddress;
    private String receiverCity;
    private String receiverState;
    private String receiverZipCode;
    private String receiverContactNumber;
    private String weight;
    private String length;
    private String width;
    private String height;
    private String shipmentType;
    private String shipmentStatus;
    private Date shipmentDate;
    private Date deliveryDate;
    private String deliveryTime;
    private String deliveryAddress;
    private String deliveryCity;
    private String deliveryState;
    private String deliveryZipCode;
    private String deliveryContactNumber;
    private String deliveryInstructions;
    private String deliveryStatus;
    private String senderID;
    private String senderAUTH;
    


 /*    public Shipment(int id, String status, String origin, String destination, Date date) {
        this.id = id;
        this.status = status;
        this.origin = origin;
        this.destination = destination;
        this.date = date;
    }*/

    public Shipment(int shipmentId, String senderName, 
    String senderAddress, String senderCity, String senderState, 
    String senderZipCode, String senderContactNumber, String receiverName,
     String receiverAddress, String receiverCity, String receiverState, 
     String receiverZipCode, String receiverContactNumber, String weight2, 
     String length2, String width2, String height2, String shipmentType,
      String shipmentStatus, LocalDate shipmentDate2, LocalDate deliveryDate2, String 
      deliveryTime, String deliveryAddress, String deliveryCity, String deliveryState, String deliveryZipCode, 
    String deliveryContactNumber, String deliveryInstructions, String deliveryStatus, String senderID, String senderAUTH) {

        
        this.shipmentId = shipmentId;
        this.senderName = senderName;
        this.senderAddress = senderAddress;
        this.senderCity = senderCity;
        this.senderState = senderState;
        this.senderZipCode = senderZipCode;
        this.senderContactNumber = senderContactNumber;
        this.receiverName = receiverName;
        this.receiverAddress = receiverAddress;
        this.receiverCity = receiverCity;
        this.receiverState = receiverState;
        this.receiverZipCode = receiverZipCode;
        this.receiverContactNumber = receiverContactNumber;
        this.weight = weight2;
        this.length = length2;
        this.width = width2;
        this.height = height2;
        this.shipmentType = shipmentType;
        this.shipmentStatus = shipmentStatus;
        this.shipmentDate =  null;
        this.deliveryDate =  null;
        this.deliveryTime = deliveryTime;
        this.deliveryAddress = deliveryAddress;
        this.deliveryCity = deliveryCity;
        this.deliveryState = deliveryState;
        this.deliveryZipCode = deliveryZipCode;
        this.deliveryContactNumber = deliveryContactNumber;
        this.deliveryInstructions = deliveryInstructions;
        this.deliveryStatus = deliveryStatus;
        this.senderID = senderID;
        this.senderAUTH = senderAUTH;

    }

    

    public void senderID(String senderID) {
        this.senderID = senderID;
    }

    public void senderAUTH(String senderAUTH) {
        this.senderAUTH = senderAUTH;
    }

    public String senderID() {
        return senderID;
    }

    public String senderAUTH() {
        return senderAUTH;
    }


    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getSenderCity() {
        return senderCity;
    }

    public void setSenderCity(String senderCity) {
        this.senderCity = senderCity;
    }

    public String getSenderState() {
        return senderState;
    }

    public void setSenderState(String senderState) {
        this.senderState = senderState;
    }

    public String getSenderZipCode() {
        return senderZipCode;
    }

    public void setSenderZipCode(String senderZipCode) {
        this.senderZipCode = senderZipCode;
    }

    public String getSenderContactNumber() {
        return senderContactNumber;
    }

    public void setSenderContactNumber(String senderContactNumber) {
        this.senderContactNumber = senderContactNumber;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverState() {
        return receiverState;
    }

    public void setReceiverState(String receiverState) {
        this.receiverState = receiverState;
    }

    public String getReceiverZipCode() {
        return receiverZipCode;
    }

    public void setReceiverZipCode(String receiverZipCode) {
        this.receiverZipCode = receiverZipCode;
    }

    public String getReceiverContactNumber() {
        return receiverContactNumber;
    }

    public void setReceiverContactNumber(String receiverContactNumber) {
        this.receiverContactNumber = receiverContactNumber;
    }

    public double getWeight() {
        return  Double.parseDouble(weight);
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public double getLength() {
        return Double.parseDouble(length);
    }

    public void setLength(String length) {
        this.length = length;
    }

    public double getWidth() {
        return Double.parseDouble(width);
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public double getHeight() {
        return Double.parseDouble(height);
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(String shipmentType) {
        this.shipmentType = shipmentType;
    }

    public String getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(String shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    public Date getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(Date shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public void setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
    }

    public String getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(String deliveryState) {
        this.deliveryState = deliveryState;
    }

    public String getDeliveryZipCode() {
        return deliveryZipCode;
    }

    public void setDeliveryZipCode(String deliveryZipCode) {
        this.deliveryZipCode = deliveryZipCode;
    }

    public String getDeliveryContactNumber() {
        return deliveryContactNumber;
    }

    public void setDeliveryContactNumber(String deliveryContactNumber) {
        this.deliveryContactNumber = deliveryContactNumber;
    }

    public String getDeliveryInstructions() {
        return deliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        this.deliveryInstructions = deliveryInstructions;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }


    public enum ShipmentStatus {
        CREATED,
        RECEIVED,
        IN_TRANSIT,
        COMPLETED,
        CANCELLED
    }

}
