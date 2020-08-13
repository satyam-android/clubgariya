package com.satyam.clubgariya.modals;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Transaction {
    @Exclude
    private String transId;
    private double transAmount;
    private String transMessage;
    private String timestamp;
    private List<String> transMediaList;
    private String deliveryStatus;
    private String settlementStatus;
    private String userId;

    public Transaction() {

    }

    public Transaction(String transId, double transAmount, String transMessage, String timestamp, List<String> transMediaList, String deliveryStatus, String settlementStatus, String userId) {
        this.transId = transId;
        this.transAmount = transAmount;
        this.transMessage = transMessage;
        this.timestamp = timestamp;
        this.transMediaList = transMediaList;
        this.deliveryStatus = deliveryStatus;
        this.settlementStatus = settlementStatus;
        this.userId = userId;
    }


    @Exclude
    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public double getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(double transAmount) {
        this.transAmount = transAmount;
    }

    public String getTransMessage() {
        return transMessage;
    }

    public void setTransMessage(String transMessage) {
        this.transMessage = transMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getTransMediaList() {
        return transMediaList;
    }

    public void setTransMediaList(List<String> transMediaList) {
        this.transMediaList = transMediaList;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(String settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
