package com.progi.sargarepoljupci.DTO.Response;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class TimeSlotResponse {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String parkingSpotId;

}
