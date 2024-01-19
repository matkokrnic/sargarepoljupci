package com.progi.sargarepoljupci.DTO.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TableResponse {

    private String code;
    private List<List<Double>> distances;

    public List<Double> getFlattenedDistances() {
        // Flatten the list of lists into a single list of Double

        return distances.stream()
                .flatMap(List::stream)
                .toList();
    }

}