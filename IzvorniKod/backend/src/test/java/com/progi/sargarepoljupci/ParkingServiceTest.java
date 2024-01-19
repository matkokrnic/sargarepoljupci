package com.progi.sargarepoljupci;

import com.progi.sargarepoljupci.DTO.Request.MarkParkingRequest;
import com.progi.sargarepoljupci.Exceptions.RequestDeniedException;
import com.progi.sargarepoljupci.Models.Parking;
import com.progi.sargarepoljupci.Models.ParkingSpot;
import com.progi.sargarepoljupci.Repository.BicycleRepository;
import com.progi.sargarepoljupci.Repository.ParkingRepository;
import com.progi.sargarepoljupci.Repository.ParkingSpotRepository;
import com.progi.sargarepoljupci.Services.ParkingService;
import com.progi.sargarepoljupci.Utilities.ParkingSpotReservable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ParkingServiceTest {

    @Mock
    private ParkingRepository parkingRepository;

    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @Mock
    private BicycleRepository bicycleRepository;

    @InjectMocks
    private ParkingService parkingService;

    @Test
    public void markSpotsSuccess() {
        // Arrange
        Long parkingId = 1L;
        String parkingSpotId = "2";
        ParkingSpotReservable reservable = new ParkingSpotReservable();
        reservable.setReservable(true);
        reservable.setSpotId(parkingSpotId);

        Parking parking = new Parking(); // Assuming you have a Parking class
        parking.setParkingId(parkingId);
        ParkingSpot parkingSpot = new ParkingSpot(); // Assuming you have a ParkingSpot class
        parkingSpot.setId(parkingSpotId);

        MarkParkingRequest request = new MarkParkingRequest();
        request.setParkingId(parkingId);
        request.setParkingSpotReservableList(List.of(reservable));

        when(parkingRepository.findById(parkingId)).thenReturn(Optional.of(parking));
        when(parkingSpotRepository.findById(parkingSpotId)).thenReturn(Optional.of(parkingSpot));

        List<ParkingSpotReservable> unmarkableParkingSpots = parkingService.markSpots(request);

        verify(parkingRepository, times(1)).findById(parkingId);
        verify(parkingSpotRepository, times(1)).findById(parkingSpotId);
        verify(parkingSpotRepository, times(1)).save(any(ParkingSpot.class));

        assertNull(unmarkableParkingSpots, "No parking spots should be unmarkable");
    }

    @Test
    public void markSpotsParkingNotFound() {
        // Arrange
        Long parkingId = 1L;
        ParkingSpotReservable reservable = new ParkingSpotReservable();
        reservable.setReservable(true);
        reservable.setSpotId("2");
        MarkParkingRequest request = new MarkParkingRequest();

        request.setParkingId(parkingId);
        request.setParkingSpotReservableList(List.of(reservable));
        when(parkingRepository.findById(parkingId)).thenReturn(Optional.empty());

        RequestDeniedException exception = assertThrows(RequestDeniedException.class, () -> {
            parkingService.markSpots(request);
        });

        verify(parkingRepository, times(1)).findById(parkingId);
        verify(parkingSpotRepository, never()).findById(any());
        verify(parkingSpotRepository, never()).save(any(ParkingSpot.class));

        assertEquals("Parking with that id doesn't exist", exception.getMessage());

    }

    @Test
    public void markSpotAlreadyMarked() {
        // Arrange
        Long parkingId = 1L;
        String parkingSpotId = "2";
        ParkingSpotReservable reservable = new ParkingSpotReservable();
        reservable.setReservable(true);
        reservable.setSpotId(parkingSpotId);

        Parking parking = new Parking(); // Assuming you have a Parking class
        parking.setParkingId(parkingId);
        ParkingSpot parkingSpot = new ParkingSpot(); // Assuming you have a ParkingSpot class
        parkingSpot.setId(parkingSpotId);
        parkingSpot.setParking(parking);

        MarkParkingRequest request = new MarkParkingRequest();
        request.setParkingId(parkingId);
        request.setParkingSpotReservableList(List.of(reservable));

        when(parkingRepository.findById(parkingId)).thenReturn(Optional.of(parking));
        when(parkingSpotRepository.findById(parkingSpotId)).thenReturn(Optional.of(parkingSpot));

        List<ParkingSpotReservable> unmarkableParkingSpots = parkingService.markSpots(request);

        verify(parkingSpotRepository, times(0)).save(any(ParkingSpot.class));

        assertNotNull(unmarkableParkingSpots, "Return value should be unmarkedParking spots");
    }

    @Test
    public void testGetMinDistanceIndex() {
        // Test case 1: Normal case with positive distances
        List<Double> distances1 = Arrays.asList(3.0, 1.0, 4.0, 2.0);
        long result1 = ParkingService.getMinDistanceIndex(distances1);
        assertEquals("Minimum distance index should be 1", 1L, result1);

        // Test case 2: Normal case with negative distances
        List<Double> distances2 = Arrays.asList(-3.0, -1.0, -4.0, -2.0);
        int result2 = ParkingService.getMinDistanceIndex(distances2);
        assertEquals("Minimum distance index should be 1", 1L, result1);

        // Test case 3: Case with one element
        List<Double> distances3 = List.of(5.0);
        int result3 = ParkingService.getMinDistanceIndex(distances3);
        assertEquals("Minimum distance index should be 0 for a single element list", 0, result3);

        // Test case 4: Case with duplicate minimum distances
        List<Double> distances4 = Arrays.asList(1.0, 2.0, 1.0, 3.0);
        int result4 = ParkingService.getMinDistanceIndex(distances4);
        assertEquals("Minimum distance index should be 0 for duplicate minimum distances", 0, result4);
    }
}
