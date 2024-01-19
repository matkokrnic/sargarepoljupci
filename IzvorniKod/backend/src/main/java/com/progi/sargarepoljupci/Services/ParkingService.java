package com.progi.sargarepoljupci.Services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.progi.sargarepoljupci.DTO.Request.MarkParkingRequest;
import com.progi.sargarepoljupci.DTO.Request.ParkingInformationRequest;
import com.progi.sargarepoljupci.DTO.Response.TableResponse;
import com.progi.sargarepoljupci.Exceptions.RequestDeniedException;
import com.progi.sargarepoljupci.Models.BicycleParking;
import com.progi.sargarepoljupci.Models.Parking;
import com.progi.sargarepoljupci.Models.ParkingSpot;
import com.progi.sargarepoljupci.Repository.BicycleRepository;
import com.progi.sargarepoljupci.Repository.ParkingRepository;
import com.progi.sargarepoljupci.Repository.ParkingSpotRepository;
import com.progi.sargarepoljupci.Repository.VoditeljRepository;
import com.progi.sargarepoljupci.Utilities.ParkingSpotReservable;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingService {



    @Value("${osrm.api.url}")
    private String osrmApiUrl; // OSRM API endpoint URL

    private final ParkingSpotRepository parkingSpotRepository;

    private final ReservationService reservationService;
    private final BicycleRepository bicycleRepository;
    private final ParkingRepository parkingRepository;
    private final VoditeljRepository voditeljRepository;
    @Autowired
    public ParkingService(ParkingSpotRepository parkingSpotRepository, ReservationService reservationService, BicycleRepository bicycleRepository, ParkingRepository parkingRepository, VoditeljRepository voditeljRepository) {

        this.parkingSpotRepository = parkingSpotRepository;

        this.reservationService = reservationService;
        this.bicycleRepository = bicycleRepository;

        this.parkingRepository = parkingRepository;
        this.voditeljRepository = voditeljRepository;
    }

    public Pair<Double, Double> findNearestAvailableParking(String destination, LocalDateTime currentTime) {
        List<ParkingSpot> freeParkingSpots = reservationService.findAvailableParkingSpotsForTimeSlot(currentTime, currentTime);
        List<Pair<Double, Double>> coordinates = freeParkingSpots.stream()
                .map(parkingSpot -> Pair.of(parkingSpot.getLongitude(),parkingSpot.getLatitude()))
                .toList();
        //List<Pair<Double, Double>> coordinates = parkingSpotService.getCoordinatesOfFreeParkingSpots();
        RestTemplate restTemplate = new RestTemplate();
        String api = buildOSRMRequest(coordinates, destination);
        ResponseEntity<String> response = restTemplate.getForEntity(api, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TableResponse tableResponse = objectMapper.readValue(response.getBody(), TableResponse.class);
            // ovo radimo jer je JSON response bio oblik List<List<Double>> pa samo pretvaramo u List<Double> jer je svakako
            // bio samo samo jedan clan u svakoj unutarnjoj listi, jer smo postavili tako api request
            List<Double> distances = tableResponse.getFlattenedDistances();
            var minDistIndex = getMinDistanceIndex(distances);
            System.out.println("Code: " + tableResponse.getCode());
            System.out.println("Distances: " + distances);
            return coordinates.get(minDistIndex);

        } catch (Exception e) {

            throw new RequestDeniedException("ERROR in requesting the nearest available parking");
        }

    }

    public Pair<Double, Double> findNearestBicycleSpot(String destination) {
        List<BicycleParking> bicycleParkings = bicycleRepository.findByParkingLotIsNotNull();
        List<Pair<Double, Double>> coordinates = bicycleParkings.stream()
                .map(parkingSpot -> Pair.of(parkingSpot.getLongitude(),parkingSpot.getLatitude()))
                .toList();

        RestTemplate restTemplate = new RestTemplate();
        String api = buildOSRMRequest(coordinates, destination);
        ResponseEntity<String> response = restTemplate.getForEntity(api, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TableResponse tableResponse = objectMapper.readValue(response.getBody(), TableResponse.class);
            // ovo radimo jer je JSON response bio oblik List<List<Double>> pa samo pretvaramo u List<Double> jer je svakako
            // bio samo samo jedan clan u svakoj unutarnjoj listi, jer smo postavili tako api request
            List<Double> distances = tableResponse.getFlattenedDistances();
            var minDistIndex = getMinDistanceIndex(distances);
            System.out.println("Code: " + tableResponse.getCode());
            System.out.println("Distances: " + distances);
            return coordinates.get(minDistIndex);

        } catch (Exception e) {
            // TODO
            // bacit neki prikladniji error?
            // ne da mi se sad
            throw new RequestDeniedException("ERROR in requesting the nearest available parking");
        }
    }

    // zelimo http://router.project-osrm.org/table/v1/driving/13.397634,52.529407;13.388860,52.517037;13.428555,52.523219?sources=0;1&destinations=2&annotations=distance
    // ali jasno za puno vise podataka, i onda dobijemo distance. Indeks distancea je isti kao i indeks koordinata.
    //npr., ovdje je 2845.5 distance od 1. koordinata u requestu do odredista (13.397634,52.529407)
    // sa sources cu odrediti sve osim zadnjeg da je pocetak, jer bi u suprotnom racunalo sve parove, a to mi nije bitno
    // za vise informacija procitati dokumentaciju za table service: http://project-osrm.org/docs/v5.5.1/api/#table-service
    // "distances": [
    //        [
    //            2845.5
    //        ],
    //        [
    //            3804.3
    //        ]
    //    ]
    // testirao sam malo, i najveci request je oko 320 koordinata i trebalo je oko 330ms sto je ok valjda?,
    // ali trebam uzet u obzir da je max oko 320 koordinata, zaokruzimo 300, pa onda mogu samo rascijepit i poslat sto nije problem
    //
    private String
    buildOSRMRequest(List<Pair<Double, Double>> coordinates, String destination) {


        StringBuilder stringBuilder = new StringBuilder(osrmApiUrl);
        for (Pair<Double, Double> coordinate : coordinates) {
            stringBuilder.append(coordinate.getFirst()).append(",").append(coordinate.getSecond()).append(";");
        }
        if(stringBuilder.isEmpty())
            throw new RequestDeniedException("There are no available parking spots");
        stringBuilder.append(destination).append("?");


        stringBuilder.append("sources=");
        // svi osim zadnjeg indeksa ce biti source
        // format: sources=0;1;2
        var size= coordinates.size();
        for (int i = 0; i < size; i++) {
            stringBuilder.append(i).append(";");
        }

        stringBuilder.setCharAt(stringBuilder.length()-1, '&');
        stringBuilder.append("destinations=").append(size).append("&annotations=distance");




        return stringBuilder.toString();
    }

    public static int getMinDistanceIndex(List<Double> distances) {
        if (distances == null || distances.isEmpty()) {
            throw new IllegalArgumentException("Distances list is empty or null.");
        }

        double minDistance = distances.get(0);
        int minIndex = 0;

        for (int i = 1; i < distances.size(); i++) {
            if (distances.get(i) < minDistance) {
                minDistance = distances.get(i);
                minIndex = i;
            }
        }

        return minIndex;
    }


    public Parking createNewParking(ParkingInformationRequest request, MultipartFile photo) throws SQLException, IOException {
        if (!photo.isEmpty()) {
            byte[] photoBytes = photo.getBytes();
            var parking = new Parking(request, photoBytes);
            var voditelj = voditeljRepository.findById(request.getVoditeljID());
            if (voditelj.isEmpty())
                throw new RequestDeniedException("Voditelj doesn't exist");
            parking.setVoditelj(voditelj.get());
            return parkingRepository.save(parking);
        }
        else
            throw new  RequestDeniedException("Photo can't be empty");
    }









    public List<ParkingSpotReservable> markSpots(MarkParkingRequest request) {
        List<ParkingSpotReservable> unmarkableParkingSpots = new ArrayList<>();
        var parkingId = request.getParkingId();
        var parkingSpotList = request.getParkingSpotReservableList();
        var parkingOptional = parkingRepository.findById(parkingId);
        if(parkingOptional.isEmpty()){
            throw new RequestDeniedException("Parking with that id doesn't exist");
        }
        var parking = parkingOptional.get();
        for (ParkingSpotReservable spot : parkingSpotList) {
            if(spot.getReservable()!=null) {
                var parkingSpot = parkingSpotRepository.findById(spot.getSpotId()).orElseThrow(()->new RequestDeniedException("Parking Spot id doesn't exist"));
            if(parkingSpot.getParking()!=null){
                //throw new RequestDeniedException("Spot " + spot.getSpotId() +  " belongs to another Parking Lot");
                unmarkableParkingSpots.add(spot);
                continue;
            }

                parkingSpot.setParking(parking);
                parkingSpot.setReservable(spot.getReservable());
                parkingSpotRepository.save(parkingSpot);
            }else{
                var bicycleSpot = bicycleRepository.findById(spot.getSpotId());
                if(bicycleSpot.isEmpty()) {
                    //throw new RequestDeniedException("BicycleSpot with that id doesn't exist");
                    unmarkableParkingSpots.add(spot);
                    continue;
                }
                bicycleSpot.get().setParkingLot(parking);
                bicycleRepository.save(bicycleSpot.get());

            }
        }
        if(unmarkableParkingSpots.isEmpty()){
            return null;
        }
        else return unmarkableParkingSpots;

    }

    public List<ParkingSpot> getAllParkingSpotsForParking(Long parkingId) {
        Parking parking = parkingRepository.findById(parkingId).orElseThrow(()->
                new RequestDeniedException("Parking Lot with that ID doesn't exist"));

        return parkingSpotRepository.findByParking(parking);
    }

    public List<BicycleParking> getAllBicycleSpotsForParking(Long parkingId) {
        Parking parking = parkingRepository.findById(parkingId).orElseThrow(()->
                new RequestDeniedException("Parking Lot with that ID doesn't exist"));

        return bicycleRepository.findByParkingLot(parking);
    }

    @Nonnull
    public List<Parking> findAll() {
        return parkingRepository.findAll();
    }

    @Nonnull
    public Optional<Parking> findById(@Nonnull Long id) {
        return parkingRepository.findById(id);
    }







}

