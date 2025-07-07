package com.ashwinkumar.lld.lockermanagement.service;

import com.ashwinkumar.lld.lockermanagement.model.Locker;
import com.ashwinkumar.lld.lockermanagement.model.LockerSize;
import com.ashwinkumar.lld.lockermanagement.model.LockerStatus;
import com.ashwinkumar.lld.lockermanagement.model.Package;
import com.ashwinkumar.lld.lockermanagement.exception.LockerNotFoundException;
import com.ashwinkumar.lld.lockermanagement.exception.LockerNotAvailableException;
import com.ashwinkumar.lld.lockermanagement.exception.InvalidPackageSizeException;
import com.ashwinkumar.lld.lockermanagement.exception.LockerAlreadyOccupiedException;
import com.ashwinkumar.lld.lockermanagement.repository.LockerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoExtension;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LockerService Tests")
class LockerServiceTest {

    @Mock
    private LockerRepository lockerRepository;

    @InjectMocks
    private LockerService lockerService;

    private Locker smallLocker;
    private Locker mediumLocker;
    private Locker largeLocker;
    private Package smallPackage;
    private Package mediumPackage;
    private Package largePackage;

    @BeforeEach
    void setUp() {
        smallLocker = new Locker("L001", LockerSize.SMALL, LockerStatus.AVAILABLE);
        mediumLocker = new Locker("L002", LockerSize.MEDIUM, LockerStatus.AVAILABLE);
        largeLocker = new Locker("L003", LockerSize.LARGE, LockerStatus.AVAILABLE);
        
        smallPackage = new Package("P001", LockerSize.SMALL, "customer1@example.com");
        mediumPackage = new Package("P002", LockerSize.MEDIUM, "customer2@example.com");
        largePackage = new Package("P003", LockerSize.LARGE, "customer3@example.com");
    }

    @Nested
    @DisplayName("Package Assignment Tests")
    class PackageAssignmentTests {

        @Test
        @DisplayName("Should successfully assign package to available locker of exact size")
        void shouldAssignPackageToAvailableLocker() {
            // Given
            when(lockerRepository.findAvailableLockerBySize(LockerSize.SMALL))
                .thenReturn(Optional.of(smallLocker));
            when(lockerRepository.save(any(Locker.class))).thenReturn(smallLocker);

            // When
            String lockerId = lockerService.assignLocker(smallPackage);

            // Then
            assertEquals("L001", lockerId);
            verify(lockerRepository).findAvailableLockerBySize(LockerSize.SMALL);
            
            ArgumentCaptor<Locker> lockerCaptor = ArgumentCaptor.forClass(Locker.class);
            verify(lockerRepository).save(lockerCaptor.capture());
            
            Locker savedLocker = lockerCaptor.getValue();
            assertEquals(LockerStatus.OCCUPIED, savedLocker.getStatus());
            assertEquals(smallPackage, savedLocker.getAssignedPackage());
            assertNotNull(savedLocker.getAssignedAt());
        }

        @Test
        @DisplayName("Should assign package to larger locker when exact size not available")
        void shouldAssignPackageToLargerLocker() {
            // Given
            when(lockerRepository.findAvailableLockerBySize(LockerSize.SMALL))
                .thenReturn(Optional.empty());
            when(lockerRepository.findAvailableLockerBySize(LockerSize.MEDIUM))
                .thenReturn(Optional.of(mediumLocker));
            when(lockerRepository.save(any(Locker.class))).thenReturn(mediumLocker);

            // When
            String lockerId = lockerService.assignLocker(smallPackage);

            // Then
            assertEquals("L002", lockerId);
            verify(lockerRepository).findAvailableLockerBySize(LockerSize.SMALL);
            verify(lockerRepository).findAvailableLockerBySize(LockerSize.MEDIUM);
            verify(lockerRepository).save(mediumLocker);
        }

        @Test
        @DisplayName("Should throw exception when no suitable locker available")
        void shouldThrowExceptionWhenNoLockerAvailable() {
            // Given
            when(lockerRepository.findAvailableLockerBySize(any(LockerSize.class)))
                .thenReturn(Optional.empty());

            // When & Then
            assertThrows(LockerNotAvailableException.class, 
                () -> lockerService.assignLocker(smallPackage));
            
            verify(lockerRepository).findAvailableLockerBySize(LockerSize.SMALL);
            verify(lockerRepository).findAvailableLockerBySize(LockerSize.MEDIUM);
            verify(lockerRepository).findAvailableLockerBySize(LockerSize.LARGE);
        }

        @Test
        @DisplayName("Should throw exception when package is null")
        void shouldThrowExceptionWhenPackageIsNull() {
            // When & Then
            assertThrows(IllegalArgumentException.class, 
                () -> lockerService.assignLocker(null));
            
            verifyNoInteractions(lockerRepository);
        }

        @Test
        @DisplayName("Should throw exception when package size is null")
        void shouldThrowExceptionWhenPackageSizeIsNull() {
            // Given
            Package packageWithNullSize = new Package("P004", null, "customer@example.com");

            // When & Then
            assertThrows(InvalidPackageSizeException.class, 
                () -> lockerService.assignLocker(packageWithNullSize));
            
            verifyNoInteractions(lockerRepository);
        }

        @Test
        @DisplayName("Should not assign large package to smaller locker")
        void shouldNotAssignLargePackageToSmallerLocker() {
            // Given
            when(lockerRepository.findAvailableLockerBySize(LockerSize.LARGE))
                .thenReturn(Optional.empty());

            // When & Then
            assertThrows(LockerNotAvailableException.class, 
                () -> lockerService.assignLocker(largePackage));
            
            verify(lockerRepository).findAvailableLockerBySize(LockerSize.LARGE);
            verify(lockerRepository, never()).findAvailableLockerBySize(LockerSize.SMALL);
            verify(lockerRepository, never()).findAvailableLockerBySize(LockerSize.MEDIUM);
        }
    }

    @Nested
    @DisplayName("Package Retrieval Tests")
    class PackageRetrievalTests {

        @Test
        @DisplayName("Should successfully retrieve package from occupied locker")
        void shouldRetrievePackageFromOccupiedLocker() {
            // Given
            smallLocker.setStatus(LockerStatus.OCCUPIED);
            smallLocker.setAssignedPackage(smallPackage);
            smallLocker.setAssignedAt(LocalDateTime.now().minusHours(1));
            
            when(lockerRepository.findById("L001")).thenReturn(Optional.of(smallLocker));
            when(lockerRepository.save(any(Locker.class))).thenReturn(smallLocker);

            // When
            Package retrievedPackage = lockerService.retrievePackage("L001");

            // Then
            assertEquals(smallPackage, retrievedPackage);
            
            ArgumentCaptor<Locker> lockerCaptor = ArgumentCaptor.forClass(Locker.class);
            verify(lockerRepository).save(lockerCaptor.capture());
            
            Locker savedLocker = lockerCaptor.getValue();
            assertEquals(LockerStatus.AVAILABLE, savedLocker.getStatus());
            assertNull(savedLocker.getAssignedPackage());
            assertNull(savedLocker.getAssignedAt());
        }

        @Test
        @DisplayName("Should throw exception when locker not found")
        void shouldThrowExceptionWhenLockerNotFound() {
            // Given
            when(lockerRepository.findById("INVALID")).thenReturn(Optional.empty());

            // When & Then
            assertThrows(LockerNotFoundException.class, 
                () -> lockerService.retrievePackage("INVALID"));
            
            verify(lockerRepository).findById("INVALID");
            verify(lockerRepository, never()).save(any(Locker.class));
        }

        @Test
        @DisplayName("Should throw exception when locker is empty")
        void shouldThrowExceptionWhenLockerIsEmpty() {
            // Given
            when(lockerRepository.findById("L001")).thenReturn(Optional.of(smallLocker));

            // When & Then
            assertThrows(LockerNotFoundException.class, 
                () -> lockerService.retrievePackage("L001"));
            
            verify(lockerRepository).findById("L001");
            verify(lockerRepository, never()).save(any(Locker.class));
        }

        @Test
        @DisplayName("Should throw exception when lockerId is null")
        void shouldThrowExceptionWhenLockerIdIsNull() {
            // When & Then
            assertThrows(IllegalArgumentException.class, 
                () -> lockerService.retrievePackage(null));
            
            verifyNoInteractions(lockerRepository);
        }

        @Test
        @DisplayName("Should throw exception when lockerId is empty")
        void shouldThrowExceptionWhenLockerIdIsEmpty() {
            // When & Then
            assertThrows(IllegalArgumentException.class, 
                () -> lockerService.retrievePackage(""));
            
            verifyNoInteractions(lockerRepository);
        }
    }

    @Nested
    @DisplayName("Locker Status Query Tests")
    class LockerStatusQueryTests {

        @Test
        @DisplayName("Should return correct locker status when locker exists")
        void shouldReturnLockerStatusWhenExists() {
            // Given
            when(lockerRepository.findById("L001")).thenReturn(Optional.of(smallLocker));

            // When
            LockerStatus status = lockerService.getLockerStatus("L001");

            // Then
            assertEquals(LockerStatus.AVAILABLE, status);
            verify(lockerRepository).findById("L001");
        }

        @Test
        @DisplayName("Should throw exception when querying status of non-existent locker")
        void shouldThrowExceptionWhenQueryingNonExistentLocker() {
            // Given
            when(lockerRepository.findById("INVALID")).thenReturn(Optional.empty());

            // When & Then
            assertThrows(LockerNotFoundException.class, 
                () -> lockerService.getLockerStatus("INVALID"));
            
            verify(lockerRepository).findById("INVALID");
        }

        @Test
        @DisplayName("Should return occupied status for locker with package")
        void shouldReturnOccupiedStatusForLockerWithPackage() {
            // Given
            smallLocker.setStatus(LockerStatus.OCCUPIED);
            smallLocker.setAssignedPackage(smallPackage);
            when(lockerRepository.findById("L001")).thenReturn(Optional.of(smallLocker));

            // When
            LockerStatus status = lockerService.getLockerStatus("L001");

            // Then
            assertEquals(LockerStatus.OCCUPIED, status);
        }
    }

    @Nested
    @DisplayName("Locker Availability Tests")
    class LockerAvailabilityTests {

        @Test
        @DisplayName("Should return available lockers by size")
        void shouldReturnAvailableLockersBySize() {
            // Given
            List<Locker> availableSmallLockers = Arrays.asList(
                new Locker("L001", LockerSize.SMALL, LockerStatus.AVAILABLE),
                new Locker("L004", LockerSize.SMALL, LockerStatus.AVAILABLE)
            );
            when(lockerRepository.findAvailableLockersBySize(LockerSize.SMALL))
                .thenReturn(availableSmallLockers);

            // When
            List<Locker> result = lockerService.getAvailableLockers(LockerSize.SMALL);

            // Then
            assertEquals(2, result.size());
            assertTrue(result.stream().allMatch(locker -> locker.getSize() == LockerSize.SMALL));
            assertTrue(result.stream().allMatch(locker -> locker.getStatus() == LockerStatus.AVAILABLE));
        }

        @Test
        @DisplayName("Should return empty list when no lockers available")
        void shouldReturnEmptyListWhenNoLockersAvailable() {
            // Given
            when(lockerRepository.findAvailableLockersBySize(LockerSize.LARGE))
                .thenReturn(Collections.emptyList());

            // When
            List<Locker> result = lockerService.getAvailableLockers(LockerSize.LARGE);

            // Then
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should throw exception when size is null")
        void shouldThrowExceptionWhenSizeIsNull() {
            // When & Then
            assertThrows(IllegalArgumentException.class, 
                () -> lockerService.getAvailableLockers(null));
            
            verifyNoInteractions(lockerRepository);
        }
    }

    @Nested
    @DisplayName("Package Lookup Tests")
    class PackageLookupTests {

        @Test
        @DisplayName("Should find locker by package id")
        void shouldFindLockerByPackageId() {
            // Given
            smallLocker.setAssignedPackage(smallPackage);
            when(lockerRepository.findByPackageId("P001")).thenReturn(Optional.of(smallLocker));

            // When
            Optional<Locker> result = lockerService.findLockerByPackageId("P001");

            // Then
            assertTrue(result.isPresent());
            assertEquals(smallLocker, result.get());
        }

        @Test
        @DisplayName("Should return empty when package not found")
        void shouldReturnEmptyWhenPackageNotFound() {
            // Given
            when(lockerRepository.findByPackageId("INVALID")).thenReturn(Optional.empty());

            // When
            Optional<Locker> result = lockerService.findLockerByPackageId("INVALID");

            // Then
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should find lockers by customer email")
        void shouldFindLockersByCustomerEmail() {
            // Given
            List<Locker> customerLockers = Arrays.asList(smallLocker, mediumLocker);
            when(lockerRepository.findByCustomerEmail("customer1@example.com"))
                .thenReturn(customerLockers);

            // When
            List<Locker> result = lockerService.findLockersByCustomerEmail("customer1@example.com");

            // Then
            assertEquals(2, result.size());
            assertEquals(customerLockers, result);
        }
    }

    @Nested
    @DisplayName("Locker Timeout Tests")
    class LockerTimeoutTests {

        @Test
        @DisplayName("Should identify expired lockers")
        void shouldIdentifyExpiredLockers() {
            // Given
            LocalDateTime expiredTime = LocalDateTime.now().minusDays(2);
            smallLocker.setAssignedAt(expiredTime);
            smallLocker.setStatus(LockerStatus.OCCUPIED);
            
            List<Locker> expiredLockers = Arrays.asList(smallLocker);
            when(lockerRepository.findExpiredLockers(any(LocalDateTime.class)))
                .thenReturn(expiredLockers);

            // When
            List<Locker> result = lockerService.getExpiredLockers();

            // Then
            assertEquals(1, result.size());
            assertEquals(smallLocker, result.get(0));
        }

        @Test
        @DisplayName("Should release expired lockers")
        void shouldReleaseExpiredLockers() {
            // Given
            LocalDateTime expiredTime = LocalDateTime.now().minusDays(2);
            smallLocker.setAssignedAt(expiredTime);
            smallLocker.setStatus(LockerStatus.OCCUPIED);
            smallLocker.setAssignedPackage(smallPackage);
            
            List<Locker> expiredLockers = Arrays.asList(smallLocker);
            when(lockerRepository.findExpiredLockers(any(LocalDateTime.class)))
                .thenReturn(expiredLockers);

            // When
            int releasedCount = lockerService.releaseExpiredLockers();

            // Then
            assertEquals(1, releasedCount);
            verify(lockerRepository).saveAll(expiredLockers);
            
            assertEquals(LockerStatus.AVAILABLE, smallLocker.getStatus());
            assertNull(smallLocker.getAssignedPackage());
            assertNull(smallLocker.getAssignedAt());
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {

        @Test
        @DisplayName("Should handle concurrent access gracefully")
        void shouldHandleConcurrentAccessGracefully() {
            // Given
            when(lockerRepository.findAvailableLockerBySize(LockerSize.SMALL))
                .thenReturn(Optional.of(smallLocker))
                .thenReturn(Optional.empty()); // Simulate concurrent access
            
            when(lockerRepository.save(any(Locker.class)))
                .thenThrow(new RuntimeException("Optimistic locking failure"));

            // When & Then
            assertThrows(RuntimeException.class, 
                () -> lockerService.assignLocker(smallPackage));
        }

        @Test
        @DisplayName("Should validate locker state before assignment")
        void shouldValidateLockerStateBeforeAssignment() {
            // Given
            smallLocker.setStatus(LockerStatus.OCCUPIED);
            when(lockerRepository.findAvailableLockerBySize(LockerSize.SMALL))
                .thenReturn(Optional.of(smallLocker));

            // When & Then
            assertThrows(LockerAlreadyOccupiedException.class, 
                () -> lockerService.assignLocker(smallPackage));
        }

        @Test
        @DisplayName("Should handle database connection failures")
        void shouldHandleDatabaseConnectionFailures() {
            // Given
            when(lockerRepository.findAvailableLockerBySize(any(LockerSize.class)))
                .thenThrow(new RuntimeException("Database connection failed"));

            // When & Then
            assertThrows(RuntimeException.class, 
                () -> lockerService.assignLocker(smallPackage));
        }

        @Test
        @DisplayName("Should handle package with invalid email format")
        void shouldHandlePackageWithInvalidEmailFormat() {
            // Given
            Package invalidPackage = new Package("P004", LockerSize.SMALL, "invalid-email");
            when(lockerRepository.findAvailableLockerBySize(LockerSize.SMALL))
                .thenReturn(Optional.of(smallLocker));

            // When & Then
            assertThrows(IllegalArgumentException.class, 
                () -> lockerService.assignLocker(invalidPackage));
        }
    }

    @Nested
    @DisplayName("Performance and Capacity Tests")
    class PerformanceAndCapacityTests {

        @Test
        @DisplayName("Should handle bulk operations efficiently")
        void shouldHandleBulkOperationsEfficiently() {
            // Given
            List<Package> packages = Arrays.asList(
                new Package("P001", LockerSize.SMALL, "customer1@example.com"),
                new Package("P002", LockerSize.MEDIUM, "customer2@example.com"),
                new Package("P003", LockerSize.LARGE, "customer3@example.com")
            );
            
            when(lockerRepository.findAvailableLockerBySize(LockerSize.SMALL))
                .thenReturn(Optional.of(smallLocker));
            when(lockerRepository.findAvailableLockerBySize(LockerSize.MEDIUM))
                .thenReturn(Optional.of(mediumLocker));
            when(lockerRepository.findAvailableLockerBySize(LockerSize.LARGE))
                .thenReturn(Optional.of(largeLocker));

            // When
            List<String> assignedLockers = lockerService.assignLockersInBulk(packages);

            // Then
            assertEquals(3, assignedLockers.size());
            verify(lockerRepository, times(3)).save(any(Locker.class));
        }

        @Test
        @DisplayName("Should get capacity utilization statistics")
        void shouldGetCapacityUtilizationStatistics() {
            // Given
            when(lockerRepository.countByStatus(LockerStatus.AVAILABLE)).thenReturn(10L);
            when(lockerRepository.countByStatus(LockerStatus.OCCUPIED)).thenReturn(5L);
            when(lockerRepository.countByStatus(LockerStatus.OUT_OF_SERVICE)).thenReturn(2L);

            // When
            Map<LockerStatus, Long> statistics = lockerService.getCapacityStatistics();

            // Then
            assertEquals(10L, statistics.get(LockerStatus.AVAILABLE));
            assertEquals(5L, statistics.get(LockerStatus.OCCUPIED));
            assertEquals(2L, statistics.get(LockerStatus.OUT_OF_SERVICE));
        }
    }
}