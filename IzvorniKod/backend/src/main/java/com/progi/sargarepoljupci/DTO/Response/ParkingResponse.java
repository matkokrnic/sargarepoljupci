package com.progi.sargarepoljupci.DTO.Response;

import com.progi.sargarepoljupci.Models.Parking;
import lombok.Data;
import org.apache.tomcat.util.codec.binary.Base64;

@Data
public class ParkingResponse {
    private Long parkingId;
    private String picture;
    private String parkingName;
    private String parkingDescription;
    private Double costPerHour;
    private Long voditeljId;

    public ParkingResponse(Parking parking) {
        this.parkingId = parking.getParkingId();

        byte[] photoBytes = parking.getPicture();
       //Blob photoBlob = parking.getPicture();
       //System.out.println(photoBlob);
       //if (photoBlob != null) {
       //    try {
       //        photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
       //    } catch (SQLException e) {
       //        throw new RequestDeniedException("Error retrieving photo");
       //    }
       //}
        this.picture = photoBytes != null ? Base64.encodeBase64String(photoBytes) : null;

        this.parkingName = parking.getParkingName();
        this.parkingDescription = parking.getParkingDescription();
        this.costPerHour = parking.getCostPerHour();
        this.voditeljId = (parking.getVoditelj() != null) ? parking.getVoditelj().getVoditeljId() : null;
    }

    public ParkingResponse() {

    }

    public ParkingResponse(Long parkingId, String picture, String parkingName, String parkingDescription, Double costPerHour, Long voditeljId) {
        this.parkingId = parkingId;
        this.picture = picture;
        this.parkingName = parkingName;
        this.parkingDescription = parkingDescription;
        this.costPerHour = costPerHour;
        this.voditeljId = voditeljId;
    }
}
