# Contact Model Update - Email Support

## Overview

The Contact model has been enhanced to support email addresses in addition to phone numbers. This update affects several components of the locker management system to provide better communication capabilities.

## Changes Made

### 1. Contact Model (`Contact.java`)

**Added:**
- `email` field to store email addresses
- `hasValidPhone()` method to validate phone numbers
- `hasValidEmail()` method to validate email addresses (basic validation)
- `isComplete()` method to check if both phone and email are provided
- Enhanced `toString()` method for better debugging

**Constructor:**
```java
public Contact(String phone, String email)
```

### 2. NotificationService (`NotificationService.java`)

**Enhanced Methods:**
- `notifyUser()` - Now supports both SMS and email notifications with fallback logic
- `notifyUserBySms()` - Validates phone number before sending SMS
- `notifyUserByEmail()` - Validates email address before sending email

**Notification Logic:**
1. If both phone and email are valid → Send to both channels
2. If only phone is valid → Send SMS only
3. If only email is valid → Send email only
4. If neither is valid → Log error message

### 3. Demo Class (`ContactDemo.java`)

A comprehensive demo showcasing:
- Complete contact information usage
- Phone-only contact scenarios
- Email-only contact scenarios
- Invalid contact handling
- Specific notification method testing

## Usage Examples

### Creating Contact Objects

```java
// Complete contact information
Contact completeContact = new Contact("+1-555-0123", "user@example.com");

// Phone only
Contact phoneOnly = new Contact("+1-555-0123", "");

// Email only
Contact emailOnly = new Contact("", "user@example.com");
```

### Validation

```java
Contact contact = new Contact("+1-555-0123", "user@example.com");

boolean hasPhone = contact.hasValidPhone();     // true
boolean hasEmail = contact.hasValidEmail();     // true
boolean isComplete = contact.isComplete();      // true
```

### Creating Users

```java
// Buyer with complete contact info
Contact contact = new Contact("+1-555-0123", "buyer@example.com");
Buyer buyer = new Buyer(contact);

// DeliveryPerson with email only
Contact deliveryContact = new Contact("", "delivery@company.com");
DeliveryPerson deliveryPerson = new DeliveryPerson(deliveryContact);
```

### Notifications

```java
NotificationService notificationService = new NotificationService();

// Automatic channel selection based on available contact info
notificationService.notifyUser(buyer, "123456", slot);

// Specific channel notifications
notificationService.notifyUserBySms(buyer, "123456", slot);
notificationService.notifyUserByEmail(buyer, "123456", slot);
```

## Running the Demo

To see the Contact model in action:

```bash
cd locker-management
./gradlew build
java -cp build/classes/java/main com.ashwinkumar.lld.lockermanagement.demo.ContactDemo
```

## Backward Compatibility

⚠️ **Breaking Change Notice:** The Contact constructor now requires both phone and email parameters. Any existing code that creates Contact objects will need to be updated.

**Migration:**
```java
// Old way
Contact oldContact = new Contact("+1-555-0123");

// New way
Contact newContact = new Contact("+1-555-0123", "user@example.com");
// or if no email is available
Contact newContact = new Contact("+1-555-0123", "");
```

## Validation Rules

### Phone Number Validation
- Must not be null or empty (after trimming)

### Email Validation
- Must not be null or empty (after trimming)
- Must contain "@" symbol (basic validation)
- For production use, consider implementing more robust email validation

## Future Enhancements

1. **Advanced Email Validation:** Implement regex-based email validation
2. **Phone Number Formatting:** Add phone number format validation and normalization
3. **Multiple Contact Methods:** Support for multiple phone numbers or email addresses
4. **Contact Preferences:** Allow users to specify preferred notification methods
5. **Contact Verification:** Add OTP-based verification for email addresses and phone numbers

## Testing

The demo class (`ContactDemo.java`) provides comprehensive testing scenarios:
- ✅ Complete contact information
- ✅ Phone-only scenarios
- ✅ Email-only scenarios
- ✅ Invalid contact handling
- ✅ Notification method validation

## Dependencies

No additional dependencies were added. The changes use only existing Lombok annotations and standard Java libraries. 