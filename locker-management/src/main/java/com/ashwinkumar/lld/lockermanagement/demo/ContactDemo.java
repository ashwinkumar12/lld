package com.ashwinkumar.lld.lockermanagement.demo;

import com.ashwinkumar.lld.lockermanagement.models.*;
import com.ashwinkumar.lld.lockermanagement.service.NotificationService;

/**
 * Demo class to showcase the updated Contact model with email functionality
 */
public class ContactDemo {

    public static void main(String[] args) {
        System.out.println("=== Contact Model with Email Demo ===\n");

        // Create notification service
        NotificationService notificationService = new NotificationService();

        // Demo 1: Complete contact information
        System.out.println("1. Complete Contact Information:");
        Contact completeContact = new Contact("+1-555-0123", "john.doe@example.com");
        Buyer buyerComplete = new Buyer(completeContact);
        System.out.println("Contact: " + completeContact);
        System.out.println("Is Complete: " + completeContact.isComplete());
        System.out.println("Has Valid Phone: " + completeContact.hasValidPhone());
        System.out.println("Has Valid Email: " + completeContact.hasValidEmail());
        
        // Simulate notification
        Slot dummySlot = createDummySlot();
        notificationService.notifyUser(buyerComplete, "123456", dummySlot);
        System.out.println();

        // Demo 2: Phone only contact
        System.out.println("2. Phone Only Contact:");
        Contact phoneOnlyContact = new Contact("+1-555-0456", "");
        Buyer buyerPhoneOnly = new Buyer(phoneOnlyContact);
        System.out.println("Contact: " + phoneOnlyContact);
        System.out.println("Is Complete: " + phoneOnlyContact.isComplete());
        notificationService.notifyUser(buyerPhoneOnly, "789012", dummySlot);
        System.out.println();

        // Demo 3: Email only contact
        System.out.println("3. Email Only Contact:");
        Contact emailOnlyContact = new Contact("", "jane.smith@example.com");
        Buyer buyerEmailOnly = new Buyer(emailOnlyContact);
        System.out.println("Contact: " + emailOnlyContact);
        System.out.println("Is Complete: " + emailOnlyContact.isComplete());
        notificationService.notifyUser(buyerEmailOnly, "345678", dummySlot);
        System.out.println();

        // Demo 4: Invalid contact
        System.out.println("4. Invalid Contact Information:");
        Contact invalidContact = new Contact("", "invalid-email");
        Buyer buyerInvalid = new Buyer(invalidContact);
        System.out.println("Contact: " + invalidContact);
        System.out.println("Is Complete: " + invalidContact.isComplete());
        notificationService.notifyUser(buyerInvalid, "999999", dummySlot);
        System.out.println();

        // Demo 5: Specific notification methods
        System.out.println("5. Specific Notification Methods:");
        Contact testContact = new Contact("+1-555-9999", "test@example.com");
        Buyer testBuyer = new Buyer(testContact);
        
        System.out.println("SMS Only:");
        notificationService.notifyUserBySms(testBuyer, "111111", dummySlot);
        
        System.out.println("Email Only:");
        notificationService.notifyUserByEmail(testBuyer, "222222", dummySlot);
    }

    private static Slot createDummySlot() {
        // Create a dummy slot for demonstration
        Size size = new Size(10.0, 10.0);
        Locker locker = new Locker("DEMO-LOCKER-001");
        return new Slot("DEMO-SLOT-001", size, locker);
    }
} 