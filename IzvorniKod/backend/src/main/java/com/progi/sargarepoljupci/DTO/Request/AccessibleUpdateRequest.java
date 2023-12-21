package com.progi.sargarepoljupci.DTO.Request;

import lombok.Data;

@Data
public class AccessibleUpdateRequest {
    private String parkingSpotId;
    private Boolean accessible;
}
