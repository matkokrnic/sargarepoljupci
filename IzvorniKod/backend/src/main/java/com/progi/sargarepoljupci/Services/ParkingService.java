package com.progi.sargarepoljupci.Services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.progi.sargarepoljupci.DTO.Request.ParkingInformationRequest;
import com.progi.sargarepoljupci.DTO.Response.TableResponse;
import com.progi.sargarepoljupci.Exceptions.RequestDeniedException;
import com.progi.sargarepoljupci.Models.BicycleParking;
import com.progi.sargarepoljupci.Models.ParkingAuto;
import com.progi.sargarepoljupci.Models.ParkingSpot;
import com.progi.sargarepoljupci.Repository.BicycleRepository;
import com.progi.sargarepoljupci.Repository.ParkingAutoRepository;
import com.progi.sargarepoljupci.Repository.ParkingSpotRepository;
import com.progi.sargarepoljupci.Repository.VoditeljRepository;
import com.progi.sargarepoljupci.Utilities.ParkingSpotReservable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParkingService {



    @Value("${osrm.api.url}")
    private String osrmApiUrl; // OSRM API endpoint URL

    private final ParkingSpotRepository parkingSpotRepository;

    private final ReservationService reservationService;
    private final BicycleRepository bicycleRepository;
    private final ParkingAutoRepository parkingAutoRepository;
    private final VoditeljRepository voditeljRepository;
    @Autowired
    public ParkingService(ParkingSpotRepository parkingSpotRepository, ParkingSpotService parkingSpotService, ReservationService reservationService, BicycleRepository bicycleRepository, ParkingAutoRepository parkingAutoRepository, VoditeljRepository voditeljRepository) {

        this.parkingSpotRepository = parkingSpotRepository;

        this.reservationService = reservationService;
        this.bicycleRepository = bicycleRepository;

        this.parkingAutoRepository = parkingAutoRepository;
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
        List<BicycleParking> bicycleParkings = bicycleRepository.findAll();
        List<Pair<Double, Double>> coordinates = bicycleParkings.stream()
                .map(parkingSpot -> Pair.of(parkingSpot.getLatitude(), parkingSpot.getLongitude()))
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


    public ParkingAuto createNewParking(ParkingInformationRequest request) {
        var parkingAuto = new ParkingAuto(request);
        var voditelj = voditeljRepository.findById(request.getVoditeljID());
        if(voditelj.isEmpty())
            throw new RequestDeniedException("Voditelj doesn't exist");
        parkingAuto.setVoditelj(voditelj.get());
        return parkingAutoRepository.save(parkingAuto);
    }

    public void markSpots(ParkingInformationRequest request, ParkingAuto parkingAuto) {
        var parkingSpotList = request.getParkingSpotList();
        for (ParkingSpotReservable spot : parkingSpotList) {
            if(spot.getReservable()!=null) {
                var parkingSpot = parkingSpotRepository.findById(spot.getSpotId()).orElseThrow(()->new RequestDeniedException("Parking Spot id doesn't exist"));
            if(parkingSpot.getParking()!=null){
                throw new RequestDeniedException("Spot " + spot.getSpotId() +  " belongs to another Parking Lot");
            }

                parkingSpot.setParking(parkingAuto);
                parkingSpot.setReservable(spot.getReservable());
                parkingSpotRepository.save(parkingSpot);
            }else{
                var bicycleSpot = bicycleRepository.findById(spot.getSpotId());
                if(bicycleSpot.isEmpty())
                    throw new RequestDeniedException("BicycleSpot with that id doesn't exist");
                //bicycleSpot.get().setParking(parkingAuto);
                bicycleRepository.save(bicycleSpot.get());

            }
        }

    }

    public List<ParkingSpot> getAllParkingSpotsForParkingAuto(Long parkingId) {
        ParkingAuto parkingAuto = parkingAutoRepository.findById(parkingId).orElseThrow(()->
                new RequestDeniedException("Parking Lot with that ID doesn't exist"));

        return parkingSpotRepository.findByParking(parkingAuto);
    }

    public List<BicycleParking> getAllBicycleSpotsForParking(Long parkingId) {
        ParkingAuto parkingAuto = parkingAutoRepository.findById(parkingId).orElseThrow(()->
                new RequestDeniedException("Parking Lot with that ID doesn't exist"));

        return bicycleRepository.findByParkingLot(parkingAuto);
    }





}

