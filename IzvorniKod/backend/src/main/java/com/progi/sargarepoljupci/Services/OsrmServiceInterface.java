package com.progi.sargarepoljupci.Services;

import com.progi.sargarepoljupci.DTO.Request.ClientRequest;
import com.progi.sargarepoljupci.DTO.Response.LocationResponse;

public interface OsrmServiceInterface {
    public LocationResponse minDistanceToParkingSpot(ClientRequest request);
}
