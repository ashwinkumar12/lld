package commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

/**
 * Comprehensive unit tests for CabBookingService
 * Testing Framework: JUnit 5 with Mockito
 */
public class CabBookingServiceTest {

    @Mock
    private CabRepository cabRepository;
    
    @Mock
    private BookingRepository bookingRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private NotificationService notificationService;
    
    @Mock
    private PaymentService paymentService;
    
    private CabBookingService cabBookingService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cabBookingService = new CabBookingService(
            cabRepository, 
            bookingRepository, 
            userRepository,
            notificationService,
            paymentService
        );
    }

    @Nested
    @DisplayName("Cab Booking Tests")
    class CabBookingTests {
        
        @Test
        @DisplayName("Should successfully book available cab")
        void shouldBookAvailableCab() {
            // Given
            String userId = "user123";
            String startLocation = "Airport";
            String endLocation = "Downtown";
            User user = new User(userId, "John Doe", "john@example.com");
            Cab availableCab = new Cab("cab456", "Toyota Camry", "Available", startLocation);
            
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(cabRepository.findAvailableCabs(startLocation)).thenReturn(Arrays.asList(availableCab));
            when(bookingRepository.save(any(Booking.class))).thenReturn(new Booking());
            
            // When
            BookingResult result = cabBookingService.bookCab(userId, startLocation, endLocation);
            
            // Then
            assertNotNull(result);
            assertTrue(result.isSuccess());
            assertEquals("cab456", result.getCabId());
            verify(cabRepository).updateCabStatus("cab456", "Booked");
            verify(notificationService).sendBookingConfirmation(eq(userId), any(Booking.class));
        }
        
        @Test
        @DisplayName("Should fail booking when no cabs available")
        void shouldFailBookingWhenNoCabsAvailable() {
            // Given
            String userId = "user123";
            String startLocation = "Remote Area";
            String endLocation = "Downtown";
            User user = new User(userId, "John Doe", "john@example.com");
            
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(cabRepository.findAvailableCabs(startLocation)).thenReturn(Collections.emptyList());
            
            // When
            BookingResult result = cabBookingService.bookCab(userId, startLocation, endLocation);
            
            // Then
            assertNotNull(result);
            assertFalse(result.isSuccess());
            assertEquals("No cabs available", result.getErrorMessage());
            verify(cabRepository, never()).updateCabStatus(anyString(), anyString());
            verify(notificationService, never()).sendBookingConfirmation(anyString(), any(Booking.class));
        }
        
        @Test
        @DisplayName("Should fail booking when user not found")
        void shouldFailBookingWhenUserNotFound() {
            // Given
            String userId = "nonexistent";
            String startLocation = "Airport";
            String endLocation = "Downtown";
            
            when(userRepository.findById(userId)).thenReturn(Optional.empty());
            
            // When
            BookingResult result = cabBookingService.bookCab(userId, startLocation, endLocation);
            
            // Then
            assertNotNull(result);
            assertFalse(result.isSuccess());
            assertEquals("User not found", result.getErrorMessage());
            verify(cabRepository, never()).findAvailableCabs(anyString());
        }
        
        @Test
        @DisplayName("Should handle null parameters gracefully")
        void shouldHandleNullParametersGracefully() {
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> 
                cabBookingService.bookCab(null, "Airport", "Downtown"));
            
            assertThrows(IllegalArgumentException.class, () -> 
                cabBookingService.bookCab("user123", null, "Downtown"));
            
            assertThrows(IllegalArgumentException.class, () -> 
                cabBookingService.bookCab("user123", "Airport", null));
        }
        
        @Test
        @DisplayName("Should handle empty string parameters gracefully")
        void shouldHandleEmptyStringParametersGracefully() {
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> 
                cabBookingService.bookCab("", "Airport", "Downtown"));
            
            assertThrows(IllegalArgumentException.class, () -> 
                cabBookingService.bookCab("user123", "", "Downtown"));
            
            assertThrows(IllegalArgumentException.class, () -> 
                cabBookingService.bookCab("user123", "Airport", ""));
        }
    }

    @Nested
    @DisplayName("Booking Cancellation Tests")
    class BookingCancellationTests {
        
        @Test
        @DisplayName("Should successfully cancel existing booking")
        void shouldCancelExistingBooking() {
            // Given
            String bookingId = "booking123";
            String userId = "user123";
            Booking booking = new Booking(bookingId, userId, "cab456", "Active");
            
            when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
            when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
            
            // When
            CancellationResult result = cabBookingService.cancelBooking(bookingId, userId);
            
            // Then
            assertNotNull(result);
            assertTrue(result.isSuccess());
            verify(bookingRepository).save(argThat(b -> "Cancelled".equals(b.getStatus())));
            verify(cabRepository).updateCabStatus("cab456", "Available");
            verify(notificationService).sendCancellationConfirmation(userId, booking);
        }
        
        @Test
        @DisplayName("Should fail cancellation when booking not found")
        void shouldFailCancellationWhenBookingNotFound() {
            // Given
            String bookingId = "nonexistent";
            String userId = "user123";
            
            when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());
            
            // When
            CancellationResult result = cabBookingService.cancelBooking(bookingId, userId);
            
            // Then
            assertNotNull(result);
            assertFalse(result.isSuccess());
            assertEquals("Booking not found", result.getErrorMessage());
            verify(cabRepository, never()).updateCabStatus(anyString(), anyString());
        }
        
        @Test
        @DisplayName("Should fail cancellation when user not authorized")
        void shouldFailCancellationWhenUserNotAuthorized() {
            // Given
            String bookingId = "booking123";
            String userId = "user123";
            String unauthorizedUserId = "user456";
            Booking booking = new Booking(bookingId, userId, "cab456", "Active");
            
            when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
            
            // When
            CancellationResult result = cabBookingService.cancelBooking(bookingId, unauthorizedUserId);
            
            // Then
            assertNotNull(result);
            assertFalse(result.isSuccess());
            assertEquals("User not authorized to cancel this booking", result.getErrorMessage());
            verify(bookingRepository, never()).save(any(Booking.class));
        }
        
        @Test
        @DisplayName("Should fail cancellation when booking already cancelled")
        void shouldFailCancellationWhenBookingAlreadyCancelled() {
            // Given
            String bookingId = "booking123";
            String userId = "user123";
            Booking booking = new Booking(bookingId, userId, "cab456", "Cancelled");
            
            when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
            
            // When
            CancellationResult result = cabBookingService.cancelBooking(bookingId, userId);
            
            // Then
            assertNotNull(result);
            assertFalse(result.isSuccess());
            assertEquals("Booking already cancelled", result.getErrorMessage());
            verify(bookingRepository, never()).save(any(Booking.class));
        }
    }

    @Nested
    @DisplayName("Booking Retrieval Tests")
    class BookingRetrievalTests {
        
        @Test
        @DisplayName("Should retrieve user bookings successfully")
        void shouldRetrieveUserBookingsSuccessfully() {
            // Given
            String userId = "user123";
            List<Booking> expectedBookings = Arrays.asList(
                new Booking("booking1", userId, "cab1", "Active"),
                new Booking("booking2", userId, "cab2", "Completed")
            );
            
            when(bookingRepository.findByUserId(userId)).thenReturn(expectedBookings);
            
            // When
            List<Booking> result = cabBookingService.getUserBookings(userId);
            
            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(expectedBookings, result);
        }
        
        @Test
        @DisplayName("Should return empty list when user has no bookings")
        void shouldReturnEmptyListWhenUserHasNoBookings() {
            // Given
            String userId = "user123";
            
            when(bookingRepository.findByUserId(userId)).thenReturn(Collections.emptyList());
            
            // When
            List<Booking> result = cabBookingService.getUserBookings(userId);
            
            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
        
        @Test
        @DisplayName("Should handle null userId gracefully")
        void shouldHandleNullUserIdGracefully() {
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> 
                cabBookingService.getUserBookings(null));
        }
    }

    @Nested
    @DisplayName("Cab Availability Tests")
    class CabAvailabilityTests {
        
        @Test
        @DisplayName("Should find available cabs in location")
        void shouldFindAvailableCabsInLocation() {
            // Given
            String location = "Downtown";
            List<Cab> expectedCabs = Arrays.asList(
                new Cab("cab1", "Toyota Camry", "Available", location),
                new Cab("cab2", "Honda Civic", "Available", location)
            );
            
            when(cabRepository.findAvailableCabs(location)).thenReturn(expectedCabs);
            
            // When
            List<Cab> result = cabBookingService.getAvailableCabs(location);
            
            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(expectedCabs, result);
        }
        
        @Test
        @DisplayName("Should return empty list when no cabs available")
        void shouldReturnEmptyListWhenNoCabsAvailable() {
            // Given
            String location = "Remote Area";
            
            when(cabRepository.findAvailableCabs(location)).thenReturn(Collections.emptyList());
            
            // When
            List<Cab> result = cabBookingService.getAvailableCabs(location);
            
            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
        
        @Test
        @DisplayName("Should handle null location gracefully")
        void shouldHandleNullLocationGracefully() {
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> 
                cabBookingService.getAvailableCabs(null));
        }
    }

    @Nested
    @DisplayName("Payment Integration Tests")
    class PaymentIntegrationTests {
        
        @Test
        @DisplayName("Should process payment successfully")
        void shouldProcessPaymentSuccessfully() {
            // Given
            String bookingId = "booking123";
            String userId = "user123";
            double amount = 25.50;
            Booking booking = new Booking(bookingId, userId, "cab456", "Completed");
            
            when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
            when(paymentService.processPayment(userId, amount)).thenReturn(new PaymentResult(true, "txn123"));
            
            // When
            PaymentResult result = cabBookingService.processPayment(bookingId, userId, amount);
            
            // Then
            assertNotNull(result);
            assertTrue(result.isSuccess());
            assertEquals("txn123", result.getTransactionId());
            verify(bookingRepository).save(argThat(b -> "Paid".equals(b.getStatus())));
        }
        
        @Test
        @DisplayName("Should handle payment failure")
        void shouldHandlePaymentFailure() {
            // Given
            String bookingId = "booking123";
            String userId = "user123";
            double amount = 25.50;
            Booking booking = new Booking(bookingId, userId, "cab456", "Completed");
            
            when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
            when(paymentService.processPayment(userId, amount)).thenReturn(new PaymentResult(false, "Payment failed"));
            
            // When
            PaymentResult result = cabBookingService.processPayment(bookingId, userId, amount);
            
            // Then
            assertNotNull(result);
            assertFalse(result.isSuccess());
            assertEquals("Payment failed", result.getErrorMessage());
            verify(bookingRepository, never()).save(any(Booking.class));
        }
        
        @Test
        @DisplayName("Should validate payment amount")
        void shouldValidatePaymentAmount() {
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> 
                cabBookingService.processPayment("booking123", "user123", -1.0));
            
            assertThrows(IllegalArgumentException.class, () -> 
                cabBookingService.processPayment("booking123", "user123", 0.0));
        }
    }

    @Nested
    @DisplayName("Notification Tests")
    class NotificationTests {
        
        @Test
        @DisplayName("Should send booking notification")
        void shouldSendBookingNotification() {
            // Given
            String userId = "user123";
            Booking booking = new Booking("booking123", userId, "cab456", "Active");
            
            // When
            cabBookingService.sendBookingNotification(userId, booking);
            
            // Then
            verify(notificationService).sendBookingConfirmation(userId, booking);
        }
        
        @Test
        @DisplayName("Should handle notification service failure gracefully")
        void shouldHandleNotificationServiceFailureGracefully() {
            // Given
            String userId = "user123";
            Booking booking = new Booking("booking123", userId, "cab456", "Active");
            
            doThrow(new RuntimeException("Notification service unavailable"))
                .when(notificationService).sendBookingConfirmation(userId, booking);
            
            // When & Then
            assertDoesNotThrow(() -> cabBookingService.sendBookingNotification(userId, booking));
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandlingTests {
        
        @Test
        @DisplayName("Should handle repository exceptions gracefully")
        void shouldHandleRepositoryExceptionsGracefully() {
            // Given
            String userId = "user123";
            String startLocation = "Airport";
            String endLocation = "Downtown";
            
            when(userRepository.findById(userId)).thenThrow(new RuntimeException("Database connection failed"));
            
            // When & Then
            assertThrows(ServiceException.class, () -> 
                cabBookingService.bookCab(userId, startLocation, endLocation));
        }
        
        @Test
        @DisplayName("Should handle concurrent booking attempts")
        void shouldHandleConcurrentBookingAttempts() {
            // Given
            String userId1 = "user123";
            String userId2 = "user456";
            String startLocation = "Airport";
            String endLocation = "Downtown";
            User user1 = new User(userId1, "John Doe", "john@example.com");
            User user2 = new User(userId2, "Jane Smith", "jane@example.com");
            Cab availableCab = new Cab("cab456", "Toyota Camry", "Available", startLocation);
            
            when(userRepository.findById(userId1)).thenReturn(Optional.of(user1));
            when(userRepository.findById(userId2)).thenReturn(Optional.of(user2));
            when(cabRepository.findAvailableCabs(startLocation)).thenReturn(Arrays.asList(availableCab));
            
            // Simulate the cab being booked by first user
            when(cabRepository.updateCabStatus("cab456", "Booked"))
                .thenReturn(true)  // First call succeeds
                .thenReturn(false); // Second call fails (cab already booked)
            
            // When
            BookingResult result1 = cabBookingService.bookCab(userId1, startLocation, endLocation);
            BookingResult result2 = cabBookingService.bookCab(userId2, startLocation, endLocation);
            
            // Then
            assertTrue(result1.isSuccess());
            assertFalse(result2.isSuccess());
            assertEquals("Cab no longer available", result2.getErrorMessage());
        }
        
        @Test
        @DisplayName("Should handle very long location names")
        void shouldHandleVeryLongLocationNames() {
            // Given
            String userId = "user123";
            String veryLongLocation = "A".repeat(1000);
            String endLocation = "Downtown";
            
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> 
                cabBookingService.bookCab(userId, veryLongLocation, endLocation));
        }
        
        @Test
        @DisplayName("Should handle special characters in location names")
        void shouldHandleSpecialCharactersInLocationNames() {
            // Given
            String userId = "user123";
            String specialLocation = "O'Hare Airport & Terminal 1";
            String endLocation = "Downtown";
            User user = new User(userId, "John Doe", "john@example.com");
            
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(cabRepository.findAvailableCabs(specialLocation)).thenReturn(Collections.emptyList());
            
            // When
            BookingResult result = cabBookingService.bookCab(userId, specialLocation, endLocation);
            
            // Then
            assertNotNull(result);
            assertFalse(result.isSuccess());
            assertEquals("No cabs available", result.getErrorMessage());
        }
    }

    @Nested
    @DisplayName("Performance and Timeout Tests")
    class PerformanceAndTimeoutTests {
        
        @Test
        @DisplayName("Should handle slow repository response")
        void shouldHandleSlowRepositoryResponse() {
            // Given
            String userId = "user123";
            String startLocation = "Airport";
            String endLocation = "Downtown";
            
            when(userRepository.findById(userId)).thenAnswer(invocation -> {
                Thread.sleep(100); // Simulate slow response
                return Optional.of(new User(userId, "John Doe", "john@example.com"));
            });
            
            // When
            long startTime = System.currentTimeMillis();
            BookingResult result = cabBookingService.bookCab(userId, startLocation, endLocation);
            long endTime = System.currentTimeMillis();
            
            // Then
            assertTrue(endTime - startTime >= 100);
            assertNotNull(result);
        }
        
        @Test
        @DisplayName("Should handle large number of available cabs")
        void shouldHandleLargeNumberOfAvailableCabs() {
            // Given
            String userId = "user123";
            String startLocation = "Airport";
            String endLocation = "Downtown";
            User user = new User(userId, "John Doe", "john@example.com");
            
            // Create large list of available cabs
            List<Cab> manyCabs = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                manyCabs.add(new Cab("cab" + i, "Toyota Camry", "Available", startLocation));
            }
            
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(cabRepository.findAvailableCabs(startLocation)).thenReturn(manyCabs);
            when(bookingRepository.save(any(Booking.class))).thenReturn(new Booking());
            
            // When
            BookingResult result = cabBookingService.bookCab(userId, startLocation, endLocation);
            
            // Then
            assertNotNull(result);
            assertTrue(result.isSuccess());
            assertEquals("cab0", result.getCabId()); // Should select first available cab
        }
    }
}