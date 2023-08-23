package com.signup.auth.authentication2.activities.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledDepartureRequest {
    private String travelNumber;
    private String travelStatus;
    private String remark;
    private String serviceType;
    private String destination;
    private String departureDate;
    private String departureTime;
    private String estimatedDepartureDate;
    private String estimatedDepartureTime;
    private String actualDepartureDate;
    private String actualDepartureTime;
    private String cancelDate;
    private String cancelTime;
}

